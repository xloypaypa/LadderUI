package control.logic;

import control.event.action.SetProxyTypeAction;
import control.event.action.SetTransferServerAction;
import control.event.action.StartFileServerAction;
import control.event.steep.StringToIntegerSteep;

/**
 * Created by xlo on 15-6-23.
 * it's server's logic including starting and stopping.
 */
public class ServerLogic {
    public static void startHttp(String port) {
        new StringToIntegerSteep(port) {
            @Override
            protected void loadNextEvents() {
                this.addDoneEvent(new SetProxyTypeAction("http", this.result[0]));
            }
        }.run();
    }

    public static void startServer(String port) {
        new StringToIntegerSteep(port) {
            @Override
            protected void loadNextEvents() {
                this.addDoneEvent(new StartFileServerAction(this.result[0]));
            }
        }.run();
    }

    public static void startTransfer(String port, String serverIP, String serverPort) {
        new StringToIntegerSteep(serverPort, port) {
            @Override
            protected void loadNextEvents() {
                this.addDoneEvent(new SetTransferServerAction(serverIP, this.result[0], this.result[1]));
            }
        }.run();
    }
}
