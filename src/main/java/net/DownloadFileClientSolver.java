package net;

import main.MainPage;
import net.server.AbstractServer;
import net.tool.connectionManager.ConnectionManager;
import net.tool.connectionSolver.ConnectionMessageImpl;
import net.tool.connectionSolver.ConnectionStatus;
import net.tool.packageSolver.PackageStatus;
import net.tool.packageSolver.packageReader.HttpPackageReader;
import net.tool.packageSolver.packageReader.PackageReader;
import net.tool.packageSolver.packageWriter.PackageWriter;
import net.tool.packageSolver.packageWriter.packageWriterFactory.HttpRequestPackageWriterFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by xlo on 2015/12/7.
 * it's the download client solver
 */
public class DownloadFileClientSolver extends AbstractServer {

    private long fileLen, now;
    private String path;
    private File file;
    private FileChannel fileChannel;

    protected ConnectionStatus aimStatus;

    protected volatile PackageReader packageReader;
    protected volatile PackageWriter packageWriter;

    public DownloadFileClientSolver(String path, String from, String aimPath, long fileLen) {
        super(new ConnectionMessageImpl());
        this.aimStatus = this.getConnectionStatus();
        this.fileLen = fileLen;
        now = 0;
        this.path = path;
        this.file = new File(aimPath + path.substring(from.length()));
    }

    @Override
    public ConnectionStatus whenInit() {
        try {
            if (this.file.exists()) {
                while (!file.delete()) {
                    Thread.sleep(500);
                }
            }
            File parentFile = this.file.getParentFile();
            if (!parentFile.exists()) {
                while (!parentFile.mkdirs()) {
                    Thread.sleep(500);
                }
            }
            while (!this.file.createNewFile()) {
                Thread.sleep(500);
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            fileChannel = randomAccessFile.getChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.packageReader = new HttpPackageReader(this.getConnectionMessage().getSocket());
        return ConnectionStatus.CONNECTING;
    }

    @Override
    public ConnectionStatus whenConnecting() {
        this.aimStatus = this.getConnectionStatus();
        if (this.getConnectionMessage().getSelectionKey().isConnectable()) {
            SocketChannel socket = this.getConnectionMessage().getSocket();
            try {
                if (socket.finishConnect()) {
                    this.packageWriter = HttpRequestPackageWriterFactory.getHttpReplyPackageWriterFactory()
                            .setCommand("GET")
                            .setHost("client")
                            .setUrl(URLEncoder.encode(path, "UTF-8"))
                            .setVersion("HTTP/1.1").getHttpPackageWriter(this.getConnectionMessage().getSocket());
                    toWriting();
                    return ConnectionStatus.WAITING;
                } else {
                    return ConnectionStatus.WAITING;
                }
            } catch (IOException e) {
                return ConnectionStatus.ERROR;
            }
        } else {
            return ConnectionStatus.WAITING;
        }
    }

    @Override
    public ConnectionStatus whenReading() {
        try {
            long once = fileChannel.transferFrom(this.getConnectionMessage().getSocket(), now, fileLen - now);
            MainPage.getMainPage().addNowSize(once);
            now += once;
            if (now < fileLen) {
                return ConnectionStatus.WAITING;
            } else {
                return ConnectionStatus.CLOSE;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ConnectionStatus.ERROR;
        }
    }

    @Override
    public ConnectionStatus whenWriting() {
        try {
            PackageStatus packageStatus = this.packageWriter.write();
            if (packageStatus.equals(PackageStatus.END)) {
                toReading();
                return ConnectionStatus.WAITING;
            } else if (packageStatus.equals(PackageStatus.WAITING)) {
                return ConnectionStatus.WAITING;
            } else if (packageStatus.equals(PackageStatus.ERROR)) {
                return ConnectionStatus.ERROR;
            } else if (packageStatus.equals(PackageStatus.CLOSED)) {
                return ConnectionStatus.CLOSE;
            } else {
                return ConnectionStatus.READING;
            }
        } catch (IOException e) {
            return ConnectionStatus.ERROR;
        }
    }

    @Override
    public ConnectionStatus whenClosing() {
        if (this.getConnectionMessage() != null) {
            ConnectionManager.getSolverManager().removeConnection(this.getConnectionMessage().getSocket().socket());
            this.getConnectionMessage().closeSocket();
        }
        if (fileChannel != null) {
            try {
                fileChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ConnectionStatus whenError() {
        return ConnectionStatus.CLOSE;
    }

    @Override
    public ConnectionStatus whenWaiting() {
        return this.aimStatus;
    }

    protected void toReading() {
        this.aimStatus = ConnectionStatus.READING;
        this.getConnectionMessage().getSelectionKey().interestOps(SelectionKey.OP_READ);
    }

    protected void toWriting() {
        this.aimStatus = ConnectionStatus.WRITING;
        this.getConnectionMessage().getSelectionKey().interestOps(SelectionKey.OP_WRITE);
    }
}
