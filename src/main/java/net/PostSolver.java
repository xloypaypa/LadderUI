package net;

import net.io.WriteFileIO;
import server.serverSolver.normalServer.NormalServerSolver;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventManager;
import tool.ioAble.FileIOBuilder;
import tool.streamConnector.NormalStreamConnector;
import tool.streamConnector.StreamConnector;
import tool.streamConnector.io.LengthLimitStreamIONode;
import tool.streamConnector.io.NormalStreamIONode;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by xlo on 15-7-14.
 * it's check get or post
 */
public class PostSolver extends NormalServerSolver {
    protected File file;
    protected FileIOBuilder fileIOBuilder;

    public PostSolver() {
        ConnectionEventManager.getConnectionEventManager().addEventHandlerToItem(ConnectionEvent.connectEnd, this,
                (event, solver) -> {
                    solver.closeSocket();
                    PostSolver.this.fileIOBuilder.close();
                });
    }

    @Override
    protected boolean checkRequestExist() {
        try {
            this.file = new File(URLDecoder.decode(this.requestSolver.getUrl().getFile(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        return !this.file.exists() && this.file.getParentFile().exists() && this.file.getParentFile().isDirectory();
    }

    @Override
    protected boolean checkRequestVisitable() {
        try {
            if (!this.file.createNewFile()) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }

        this.fileIOBuilder = new WriteFileIO();
        this.fileIOBuilder.setFile(this.file.getAbsolutePath());
        return this.fileIOBuilder.buildIO();
    }

    @Override
    protected boolean sendingHead() {
        this.requestSolver.setReply(200);
        this.requestSolver.setMessage("OK");
        this.requestSolver.setVersion("HTTP/1.1");
        this.requestSolver.getReplyHeadWriter().addMessage("Content-Length", "0");
        this.requestSolver.getReplyHeadWriter().addMessage("Content-Type", "text/html;charset=ISO-8859-1");
        return this.requestSolver.sendHead();
    }

    @Override
    protected boolean afterSendHead() {
        return true;
    }

    @Override
    public void connect() {
        NormalStreamIONode io = new LengthLimitStreamIONode(Long.valueOf(this.requestSolver.getMessage("Content-Length")));
        io.setInputStream(this.requestSolver.getInputStream());
        io.addOutputStream(this.fileIOBuilder.getOutputStream());

        StreamConnector connector = new NormalStreamConnector();
        connector.addMember(io);
        connector.connect();
    }
}
