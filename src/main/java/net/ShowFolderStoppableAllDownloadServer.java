package net;

import model.pageReader.PageReader;
import net.io.StringIOBuilder;
import tool.ioAble.NormalFileIO;
import tool.streamConnector.NormalStreamConnector;
import tool.streamConnector.StreamConnector;
import tool.streamConnector.io.NormalStreamIONode;
import tool.streamConnector.io.StreamIONode;

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
            this.file = new File(URLDecoder.decode(this.requestSolver.getRequestHeadReader().getUrl().getFile(), "utf-8"));
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
                this.fileIOBuilder.setFile(new File(URLDecoder.decode(this.requestSolver.getRequestHeadReader().getUrl().getFile(), "utf-8")).getAbsolutePath());
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
            this.requestSolver.getReplyHeadWriter().setMessage("OK");
            this.requestSolver.getReplyHeadWriter().setVersion("HTTP/1.1");
            this.requestSolver.getReplyHeadWriter().setReply(200);
            buildPage();
            this.requestSolver.getReplyHeadWriter().addMessage("Content-Length", this.page.getBytes().length + "");
            return this.requestSolver.getReplyHeadWriter().sendHead();
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

            StreamIONode io = new NormalStreamIONode();
            io.setInputStream(stringIOBuilder.getInputStream());
            io.addOutputStream(this.requestSolver.getSocketIoBuilder().getOutputStream());

            StreamConnector connector = new NormalStreamConnector();
            connector.addMember(io);
            connector.connect();
        } else {
            super.connect();
        }
    }

    protected void buildPage() {
        page = PageReader.readPage("/page.html");
    }

    protected String getPath(String message) {
        String path = message;
        if (message == null) return null;
        if (path.charAt(0) != '/') path = '/' + path;
        return path;
    }
}
