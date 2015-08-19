package control.event.action;

import control.action.DirectRunAction;
import server.serverSolver.proxyServer.proxyThirdServer.TransferThirdServer;

/**
 * Created by xlo on 15-6-23.
 * it's the action to set transfer server
 */
public class SetTransferServerAction extends DirectRunAction {
    String serverIp;
    int serverPort;
    int port;

    public SetTransferServerAction(String serverIp, int serverPort, int port) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.port = port;
    }

    @Override
    protected void loadNextEvents() {
        this.addDoneEvent(new SetProxyTypeAction("transfer", port));
    }

    @Override
    public Boolean call() throws Exception {
        TransferThirdServer.setHost(serverIp);
        TransferThirdServer.setPort(serverPort);
        return true;
    }
}
