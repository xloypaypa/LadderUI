package net;

import net.io.StringIOBuilder;
import server.io.NormalFileIO;
import tool.CustomEdgeConnector;
import tool.IOAble;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

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
    protected void connect() {
        if (this.file.isDirectory()) {
            StringIOBuilder stringIOBuilder = new StringIOBuilder();
            stringIOBuilder.setMessage(this.page);
            stringIOBuilder.buildIO();

            CustomEdgeConnector connector = new CustomEdgeConnector();
            connector.addMember(stringIOBuilder);
            connector.addMember(this.requestSolver);
            Set<IOAble> to = new HashSet<>();
            to.add(this.requestSolver);
            connector.setEdge(stringIOBuilder, to);
            connector.setSync(stringIOBuilder, to);
            connector.connect();
        } else {
            super.connect();
        }
    }

    private void buildPage() {
        page = "";
        page += "<html>\r\n";
        page += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + System.getProperty("file.encoding") + "\" />\r\n";
        page += "<body>\r\n";
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
        page += "</body>\r\n";
        page += "</html>\r\n";
    }

    private String getPath(String message) {
        String path = message;
        if (message == null) return null;
        if (path.charAt(0) != '/') path = '/' + path;
        return path;
    }
}
