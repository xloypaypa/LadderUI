package net;

import net.server.normal.NormalServer;
import net.tool.connectionSolver.ConnectionMessage;
import net.tool.connectionSolver.ConnectionStatus;
import net.tool.packageSolver.PackageStatus;
import net.tool.packageSolver.headSolver.HttpRequestHeadSolver;
import net.tool.packageSolver.packageWriter.PackageWriter;
import net.tool.packageSolver.packageWriter.packageWriterFactory.HttpReplyPackageWriterFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xlo on 2015/12/7.
 * it's the download server solver
 */
public class DownloadServerSolver extends NormalServer {

    private HttpRequestHeadSolver httpRequestHeadSolver;
    private long now, len;
    private FileChannel fileChannel;

    public DownloadServerSolver(ConnectionMessage connectionMessage) {
        super(connectionMessage);
    }

    @Override
    public ConnectionStatus whenWriting() {
        httpRequestHeadSolver = (HttpRequestHeadSolver) this.packageReader.getHeadPart();
        if (httpRequestHeadSolver.getCommand().equals("POST")) {
            return sendFilesMessage();
        } else {
            return sendFile();
        }
    }

    private ConnectionStatus sendFile() {
        try {
            String path = URLDecoder.decode(httpRequestHeadSolver.getUrl().getHost(), "UTF-8");
            if (fileChannel == null) {
                System.out.println("get " + path);
                File file = new File(path);
                len = file.length();
                now = 0;
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                fileChannel = randomAccessFile.getChannel();
            }

            long once = fileChannel.transferTo(now, len - now, this.getConnectionMessage().getSocket());
            now += once;
            if (now >= len) {
                fileChannel.close();
                toReading();
            }
            return ConnectionStatus.WAITING;
        } catch (Exception e) {
            return ConnectionStatus.ERROR;
        }
    }

    private ConnectionStatus sendFilesMessage() {
        try {
            String path = URLDecoder.decode(httpRequestHeadSolver.getUrl().getHost(), "UTF-8");
            File file = new File(path);
            if (!file.exists()) {
                PackageWriter packageWriter = HttpReplyPackageWriterFactory.getHttpReplyPackageWriterFactory()
                        .setReply(404).setMessage("not found").getHttpPackageWriter(this.getConnectionMessage().getSocket());
                return sendFileMessagePackage(packageWriter);
            } else if (file.isFile()) {
                String message = "";
                message += file.length() + "\r\n";
                message += file.getAbsolutePath() + "\r\n";
                PackageWriter packageWriter = HttpReplyPackageWriterFactory.getHttpReplyPackageWriterFactory()
                        .addMessage("Content-Length", message.getBytes().length + "")
                        .setBody(message.getBytes())
                        .getHttpPackageWriter(this.getConnectionMessage().getSocket());
                return sendFileMessagePackage(packageWriter);
            } else {
                String message = "";
                ArrayList<String> strings = new ArrayList<>();
                solveFolder(file, strings);
                for (String now : strings) {
                    message += now;
                }
                PackageWriter packageWriter = HttpReplyPackageWriterFactory.getHttpReplyPackageWriterFactory()
                        .addMessage("Content-Length", message.getBytes().length + "")
                        .setBody(message.getBytes())
                        .getHttpPackageWriter(this.getConnectionMessage().getSocket());
                return sendFileMessagePackage(packageWriter);
            }
        } catch (Exception e) {
            return ConnectionStatus.ERROR;
        }
    }

    private ConnectionStatus sendFileMessagePackage(PackageWriter packageWriter) {
        try {
            PackageStatus packageStatus = packageWriter.write();
            if (packageStatus.equals(PackageStatus.WAITING)) {
                return ConnectionStatus.WAITING;
            } else if (packageStatus.equals(PackageStatus.END)) {
                toReading();
                return ConnectionStatus.WAITING;
            } else if (packageStatus.equals(PackageStatus.ERROR) || packageStatus.equals(PackageStatus.CLOSED)) {
                return ConnectionStatus.ERROR;
            } else {
                return ConnectionStatus.WAITING;
            }
        } catch (Exception e) {
            return ConnectionStatus.ERROR;
        }
    }

    private long solveFolder(File file, List<String> path) {
        long len = 0;
        File[] files = file.listFiles();
        assert files != null;
        for (File now : files) {
            if (now.isDirectory()) {
                len += solveFolder(now, path);
            } else {
                path.add(now.length() + "\r\n");
                path.add(now.getAbsolutePath() + "\r\n");
                len += now.length();
            }
        }
        return len;
    }
}
