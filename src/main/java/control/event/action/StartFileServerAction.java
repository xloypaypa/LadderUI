package control.event.action;

import control.action.DirectRunAction;
import net.MethodSolver;
import server.Server;

public class StartFileServerAction extends DirectRunAction {
    Server server;
    int port;

    public StartFileServerAction(int port) {
        this.port = port;
    }

    @Override
    protected void loadNextEvents() {

    }

    @Override
    public Boolean call() throws Exception {
        server = Server.getNewServer();
        server.setSolverBuilder(MethodSolver::new);
        server.getInstance(port);
        server.accept();
        return true;
    }
}
