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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by xlo on 2015/12/7.
 * it's the download client solver
 */
public class DownloadClientSolver extends AbstractServer {

    protected String path, ip, aimPath;
    private String[] message;
    private int port;

    protected ConnectionStatus aimStatus;

    protected volatile PackageReader packageReader;
    protected volatile PackageWriter packageWriter;


    public DownloadClientSolver(String ip, int port, String path, String aimPath) {
        super(new ConnectionMessageImpl());
        this.aimStatus = this.getConnectionStatus();
        this.path = path;
        this.message = null;

        this.ip = ip;
        this.port = port;
        this.aimPath = aimPath;
    }

    @Override
    public ConnectionStatus whenInit() {
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
                            .setCommand("POST")
                            .setHost("client")
                            .setUrl(URLEncoder.encode(path, "UTF-8"))
                            .setVersion("HTTP/1.1").getHttpPackageWriter(this.getConnectionMessage().getSocket());
                    toWriting();
                    return ConnectionStatus.WAITING;
                } else {
                    return ConnectionStatus.WAITING;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return ConnectionStatus.ERROR;
            }
        } else {
            return ConnectionStatus.WAITING;
        }
    }

    @Override
    public ConnectionStatus whenReading() {
        try {
            PackageStatus packageStatus = packageReader.read();
            if (packageStatus.equals(PackageStatus.END)) {
                this.message = new String(this.packageReader.getBody()).split("\r\n");
                long allSize = 0;
                for (int i = 0; i < message.length; i += 2) {
                    allSize += Long.valueOf(message[i]);
                }
                MainPage.getMainPage().setAllSize(allSize);
                MainPage.getMainPage().zeroNowSize();
                for (int i = 0; i < message.length; i += 2) {
                    Client client = Client.getClient();
                    client.connect(ip, port, new DownloadFileClientSolver(message[i + 1], path, aimPath, Long.valueOf(message[i])));
                }
                return ConnectionStatus.CLOSE;
            } else if (packageStatus.equals(PackageStatus.WAITING)) {
                return ConnectionStatus.WAITING;
            } else if (packageStatus.equals(PackageStatus.ERROR)) {
                return ConnectionStatus.ERROR;
            } else if (packageStatus.equals(PackageStatus.CLOSED)) {
                return ConnectionStatus.CLOSE;
            } else {
                return ConnectionStatus.READING;
            }
        } catch (Exception e) {
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
