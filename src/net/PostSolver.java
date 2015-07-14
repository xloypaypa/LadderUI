package net;

import net.io.WriteFileIO;
import server.io.FileIOBuilder;
import server.server.solver.NormalSolver;
import tool.connector.Connector;
import tool.connector.NormalConnector;
import tool.io.IO;
import tool.io.LengthLimitIO;

import java.io.File;
import java.io.IOException;

/**
 * Created by xlo on 15-7-14.
 * it's check get or post
 */
public class PostSolver extends NormalSolver {
    protected File file;
    protected FileIOBuilder fileIOBuilder;

    @Override
    protected boolean checkRequestExist() {
        this.file = new File(this.requestSolver.getUrl().getFile());
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
        this.fileIOBuilder.setFile(this.file);
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
        IO io = new LengthLimitIO(Long.valueOf(this.requestSolver.getMessage("Content-Length")));
        io.setInputStream(this.requestSolver.getInputStream());
        io.addOutputStream(this.fileIOBuilder.getOutputStream());

        Connector connector = new NormalConnector();
        connector.addMember(io);
        connector.connect();
    }

    @Override
    public void disConnect() {
        super.disConnect();
        if (this.fileIOBuilder != null) {
            this.fileIOBuilder.closeFile();
        }
    }
}
