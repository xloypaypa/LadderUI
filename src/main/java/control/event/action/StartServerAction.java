package control.event.action;

import control.action.DirectRunAction;
import server.Server;

/**
 * Created by xlo on 15-6-23.
 * it's the action to start server
 */
public class StartServerAction extends DirectRunAction {
    Server server;
    int port;

    public StartServerAction(Server server, int port) {
        this.server = server;
        this.port = port;
    }

    @Override
    protected void loadNextEvents() {

    }

    @Override
    public Boolean call() throws Exception {
        server.getInstance(port);
        server.accept();
        return true;
    }
}
