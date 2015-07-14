package net;

import server.server.requestSolver.RequestSolver;
import server.server.solver.AbstractSolver;
import server.server.solver.NormalSolver;

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
        } else {
            this.aimSolver = new ShowFolderStoppableAllDownloadServer();
        }
        this.aimSolver.setRequestSolver(this.requestSolver);
        return this.aimSolver.sendPreMessage();
    }

    @Override
    public void connect() {
        this.aimSolver.connect();
    }

    @Override
    public void setRequestSolver(RequestSolver requestSolver) {
        this.requestSolver = requestSolver;
    }

    @Override
    public boolean buildIO() {
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
