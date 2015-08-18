package control.event.action;

import control.action.DirectRunAction;
import server.Server;
import server.serverSolver.proxyServer.HttpProxyServerSolver;
import server.serverSolver.proxyServer.TransferProxyServerSolver;

/**
 * Created by xlo on 15-6-23.
 * it's the action of set proxy type.
 */
public class SetProxyTypeAction extends DirectRunAction {
    Server server;
    String type;
    int port;

    public SetProxyTypeAction(String type, int port) {
        this.type = type;
        this.port = port;
    }

    @Override
    protected void loadNextEvents() {
        this.addDoneEvent(new StartServerAction(server, port));
    }

    @Override
    public Boolean call() throws Exception {
        server = Server.getNewServer();
        if (this.type.equals("http")) {
            server.setSolverBuilder(HttpProxyServerSolver::new);
        } else {
            server.setSolverBuilder(TransferProxyServerSolver::new);
        }
        return true;
    }
}
