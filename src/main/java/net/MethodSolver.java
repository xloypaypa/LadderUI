package net;

import model.config.ConfigResourceManager;
import net.server.normal.NormalServer;
import net.tool.connectionSolver.ConnectionMessageImpl;
import net.tool.connectionSolver.ConnectionStatus;
import net.tool.packageSolver.PackageStatus;
import net.tool.packageSolver.headSolver.HttpRequestHeadSolver;
import net.tool.packageSolver.packageWriter.PackageWriter;
import net.tool.packageSolver.packageWriter.packageWriterFactory.HttpReplyPackageWriterFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;

/**
 * Created by xlo on 2015/10/27.
 * it's the method server
 */
public class MethodSolver extends NormalServer {
    protected File file;
    protected PackageWriter packageWriter;
    protected FileChannel fileChannel;
    protected long start, count;
    protected volatile int status;

    public MethodSolver() {
        super(new ConnectionMessageImpl());
        this.status = 1;
    }

    @Override
    public ConnectionStatus whenWriting() {
        if (this.status == 1) {
            return solveHead();
        } else if (this.status == 2) {
            return buildReply();
        } else if (this.status == 3) {
            return sendHead();
        } else if (this.status == 4) {
            return sendFile();
        } else {
            return ConnectionStatus.ERROR;
        }
    }

    @Override
    public ConnectionStatus whenClosing() {
        if (this.fileChannel != null && this.fileChannel.isOpen()) {
            try {
                this.fileChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.whenClosing();
    }

    @Override
    public ConnectionStatus whenError() {
        return super.whenError();
    }

    protected ConnectionStatus solveHead() {
        try {
            HttpRequestHeadSolver httpRequestHeadSolver = (HttpRequestHeadSolver) this.packageReader.getHeadPart();
            this.file = new File(URLDecoder.decode(httpRequestHeadSolver.getUrl().getPath(), "UTF-8"));
            this.status = 2;
            return ConnectionStatus.WRITING;
        } catch (UnsupportedEncodingException e) {
            return ConnectionStatus.ERROR;
        }
    }

    protected ConnectionStatus buildReply() {
        if (!this.file.exists()) {
            byte[] page = ConfigResourceManager.getConfigResourceManager().getResource("/404.html");
            this.packageWriter = HttpReplyPackageWriterFactory
                    .getHttpReplyPackageWriterFactory()
                    .setReply(404)
                    .setMessage("not found")
                    .addMessage("Content-Length", page.length + "")
                    .setBody(page)
                    .getHttpPackageWriter(this.getConnectionMessage().getSocket());
            this.status = 3;
            return ConnectionStatus.WRITING;
        } else if (this.file.isDirectory()) {
            byte[] page;
            try {
                page = getPage();
            } catch (UnsupportedEncodingException e) {
                return ConnectionStatus.ERROR;
            }
            this.packageWriter = HttpReplyPackageWriterFactory
                    .getHttpReplyPackageWriterFactory()
                    .setReply(200)
                    .setMessage("ok")
                    .addMessage("Content-Length", page.length + "")
                    .setBody(page)
                    .getHttpPackageWriter(this.getConnectionMessage().getSocket());
            this.status = 3;
            return ConnectionStatus.WRITING;
        } else {
            String range = ((HttpRequestHeadSolver) this.packageReader.getHeadPart()).getMessage("Range");
            if (range == null) {
                this.start = 0;
                this.count = this.file.length();
                this.packageWriter = HttpReplyPackageWriterFactory.getHttpReplyPackageWriterFactory()
                        .addMessage("Content-Type", "application/octet-stream")
                        .addMessage("Content-Length", this.file.length() + "")
                        .addMessage("Content-Disposition", "attachment;filename=" + this.file.getName())
                        .getHttpPackageWriter(this.getConnectionMessage().getSocket());
            } else {
                solveRange(range);
            }
            this.status = 3;
            return ConnectionStatus.WRITING;
        }
    }

    protected void solveRange(String range) {
        System.out.println("range: " + range);
        int start = range.indexOf("=");
        int half = range.indexOf("-");
        this.start = Long.valueOf(range.substring(start + 1, half));
        try {
            this.count = Long.valueOf(range.substring(half + 1)) - start + 1;
        } catch (Exception e) {
            this.count = this.file.length() - this.start;
        }
        this.packageWriter = HttpReplyPackageWriterFactory.getHttpReplyPackageWriterFactory()
                .setReply(206)
                .addMessage("Content-Type", "application/octet-stream")
                .addMessage("Content-Length", this.count + "")
                .addMessage("Content-Disposition", "attachment;filename=" + this.file.getName())
                .addMessage("Content-Range", "bytes " + this.start + "-" + (this.start + this.count - 1) + "/" + this.file.length())
                .getHttpPackageWriter(this.getConnectionMessage().getSocket());
    }

    private byte[] getPage() throws UnsupportedEncodingException {
        String page = "<!DOCTYPE html>\r\n" +
                "<html>\r\n" +
                "<head>\r\n";
        page += "    <meta charset=\"" + System.getProperty("file.encoding") + "\">\r\n";
        page += "    <title>page</title>\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "<div id = \"folder\">\r\n";
        File[] files = this.file.listFiles();

        try {
            page += "<a href=\"/" + URLEncoder.encode(this.file.getParentFile().getAbsolutePath(), "UTF-8") + "\"> parent folder </a> <br><br>";
        } catch (NullPointerException ignored) {
            page += "<br><br><br>\r\n";
        }


        if (files != null) {
            for (File now : files) {
                String name = now.getName();
                if (now.isFile()) name += " (file)";
                else name += " (folder)";
                String nowPage = "<a href=\"/" + URLEncoder.encode(now.getAbsolutePath(), "UTF-8") + "\"> " + name + " </a> <br>\r\n";
                page += nowPage;
            }
        }
        page += "</div>\r\n" +
                "</body>\r\n" +
                "</html>";
        return page.getBytes();
    }

    protected ConnectionStatus sendHead() {
        try {
            PackageStatus packageStatus = PackageWriter.writePackage(this.packageWriter);
            if (packageStatus.equals(PackageStatus.WAITING)) {
                return ConnectionStatus.WAITING;
            } else if (packageStatus.equals(PackageStatus.END)) {
                if (this.file.exists() && this.file.isFile()) {
                    this.status = 4;
                    RandomAccessFile randomAccessFile = new RandomAccessFile(this.file, "r");
                    this.fileChannel = randomAccessFile.getChannel();
                    return ConnectionStatus.WRITING;
                } else {
                    this.status = 1;
                    this.toReading();
                    return ConnectionStatus.WAITING;
                }
            } else {
                return ConnectionStatus.ERROR;
            }
        } catch (IOException e) {
            return ConnectionStatus.ERROR;
        }
    }

    protected ConnectionStatus sendFile() {
        try {
            long len = this.fileChannel.transferTo(this.start, this.count, this.getConnectionMessage().getSocket());
            this.start += len;
            this.count -= len;
            if (this.count == 0L) {
                this.fileChannel.close();
                this.toReading();
                this.status = 1;
                return ConnectionStatus.WAITING;
            } else {
                return len == 0L ? ConnectionStatus.WAITING : ConnectionStatus.WRITING;
            }
        } catch (IOException e) {
            return ConnectionStatus.ERROR;
        }
    }
}
