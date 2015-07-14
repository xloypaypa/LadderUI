package net;

import model.pageReader.PageReader;
import net.io.StringIOBuilder;
import server.io.NormalFileIO;
import tool.Connector;
import tool.NormalConnector;
import tool.io.IO;
import tool.io.NormalIO;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by xlo on 15-7-10.
 * it's add showing folder's feature
 */
public class ShowFolderStoppableAllDownloadServer extends StoppableAllDownloadServer {
    protected String page;

    @Override
    protected boolean checkRequestExist() {
        try {
            this.file = new File(URLDecoder.decode(this.requestSolver.getUrl().getFile(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        if (this.file.exists() && this.file.isFile()) {
            return true;
        } else if (this.file.exists() && this.file.isDirectory()) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean checkRequestVisitable() {
        if (this.file.isDirectory()) {
            return true;
        } else if (this.file.isFile()) {
            this.fileIOBuilder = new NormalFileIO();
            try {
                this.fileIOBuilder.setFile(new File(URLDecoder.decode(this.requestSolver.getUrl().getFile(), "utf-8")));
            } catch (UnsupportedEncodingException e) {
                return false;
            }
            return this.fileIOBuilder.buildIO();
        } else {
            return false;
        }
    }

    @Override
    protected boolean sendingHead() {
        if (this.file.isDirectory()) {
            this.requestSolver.setMessage("OK");
            this.requestSolver.setVersion("HTTP/1.1");
            this.requestSolver.setReply(200);
            buildPage();
            this.requestSolver.getReplyHeadWriter().addMessage("Content-Length", this.page.getBytes().length + "");
            return this.requestSolver.sendHead();
        } else {
            return super.sendingHead();
        }
    }

    @Override
    public void connect() {
        if (this.file.isDirectory()) {
            StringIOBuilder stringIOBuilder = new StringIOBuilder();
            stringIOBuilder.setMessage(this.page);
            stringIOBuilder.buildIO();

            IO io = new NormalIO();
            io.setInputStream(stringIOBuilder.getInputStream());
            io.addOutputStream(this.requestSolver.getOutputStream());

            Connector connector = new NormalConnector();
            connector.addMember(io);
            connector.connect();
        } else {
            super.connect();
        }
    }

    private void buildPage() {
        page = PageReader.readPage("/page.html");
        page += "<h2>\r\n";
        page += this.file.getPath() + "\r\n";
        page += "</h2>\r\n";
        page += "<a href=\"" + getPath(this.file.getParent()) + "\">" + "parent folder</a><br><br>\r\n";
        String extra;
        File[] kids = this.file.listFiles();
        if (kids != null) {
            for (File now : kids) {
                if (now.isFile()) {
                    extra = " (file)";
                } else if (now.isDirectory()) {
                    extra = " (folder)";
                } else {
                    extra = " (unknown)";
                }
                page += "<a href=\"" + getPath(now.getPath()) + "\">" + now.getPath() + extra + "</a><br>\r\n";
            }
        }
        page += PageReader.readPage("/page2.html");
    }

    private String getPath(String message) {
        String path = message;
        if (message == null) return null;
        if (path.charAt(0) != '/') path = '/' + path;
        return path;
    }
}
