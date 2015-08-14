package net;

import server.serverSolver.RequestSolver;
import server.serverSolver.normalServer.AbstractServerSolver;
import server.serverSolver.normalServer.NormalServerSolver;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventManager;

/**
 * Created by xlo on 15-7-14.
 * it's solver check post or get
 */
public class MethodSolver extends AbstractServerSolver {
    protected NormalServerSolver aimSolver;
    protected RequestSolver requestSolver;

    public MethodSolver() {
        ConnectionEventManager.getConnectionEventManager().addEventHandlerToItem(ConnectionEvent.connectEnd, this,
                (event, solver) -> {
                    solver.closeSocket();
                    MethodSolver.this.aimSolver.closeSocket();
                });
    }

    @Override
    public boolean sendReply() {
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
        return this.aimSolver.sendReply();
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
    public boolean readRequest() {
        return this.requestSolver.readHead();
    }

    @Override
    public boolean checkAccept() {
        return true;
    }
}
