package net;

import server.solver.RequestSolver;
import server.solver.fileServer.AbstractSolver;
import server.solver.fileServer.NormalSolver;

import java.io.IOException;

/**
 * Created by xlo on 15-7-14.
 * it's solver check post or get
 */
public class MethodSolver extends AbstractSolver {
    protected NormalSolver aimSolver;
    protected RequestSolver requestSolver;

    @Override
    public boolean sendPreMessage() {
        if (this.requestSolver.getCommand().equals("POST")) {
            this.aimSolver = new PostSolver();
        } else if (this.requestSolver.getMessage("Get-folder-list") != null && this.requestSolver.getMessage("Get-folder-list").equals("true")) {
            this.aimSolver = new ListFolderSolver() {
                public ListFolderSolver setRequestSolver(RequestSolver requestSolver) {
                    this.requestSolver = requestSolver;
                    return this;
                }
            }.setRequestSolver(this.requestSolver);
        } else {
            this.aimSolver = new ShowFolderStoppableAllDownloadServer() {
                public ShowFolderStoppableAllDownloadServer setRequestSolver(RequestSolver requestSolver) {
                    this.requestSolver = requestSolver;
                    return this;
                }
            }.setRequestSolver(this.requestSolver);
        }
        return this.aimSolver.sendPreMessage();
    }

    @Override
    public void connect() {
        this.aimSolver.connect();
    }

    @Override
    public boolean checkIP() {
        return true;
    }

    @Override
    public boolean buildIO() {
        this.requestSolver = this.requestSolverBuilder.getNewRequestSolver();
        this.requestSolver.setSocket(this.socket);
        return this.requestSolver.buildIO();
    }

    @Override
    public boolean readHead() {
        return this.requestSolver.readHead();
    }

    @Override
    public boolean checkAccept() {
        return true;
    }

    @Override
    public void disConnect() {
        if (socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.aimSolver != null) {
            this.aimSolver.disConnect();
        }
    }
}
