package control.logic;

import control.event.NormalEvent;
import control.event.action.SetProxyTypeAction;
import control.event.action.SetTransferServerAction;
import control.event.action.StartFileServerAction;
import control.event.action.StartServerAction;
import control.event.steep.StringToIntegerSteep;
import server.Server;

/**
 * Created by xlo on 15-6-23.
 * it's server's logic including starting and stopping.
 */
public class ServerLogic {
    public static void startHttp(String port) {
        NormalEvent event = new NormalEvent();
        event.addSteep(new StringToIntegerSteep("client port", SetProxyTypeAction.class.getSimpleName()));
        event.addSteep(new SetProxyTypeAction());
        event.addSteep(new StartServerAction());
        event.putValue("server", Server.getNewServer());
        event.putValue("client port", port);
        event.putValue("proxy type", "http");
        event.runEvent(StringToIntegerSteep.class.getSimpleName());
    }

    public static void startServer(String port) {
        NormalEvent event = new NormalEvent();
        event.addSteep(new StringToIntegerSteep("client port", StartFileServerAction.class.getSimpleName()));
        event.addSteep(new StartFileServerAction());
        event.putValue("server", Server.getNewServer());
        event.putValue("client port", port);
        event.runEvent(StringToIntegerSteep.class.getSimpleName());
    }

    public static void startTransfer(String port, String serverIP, String serverPort) {
        NormalEvent event = new NormalEvent();
        event.addSteep("string to integer client", new StringToIntegerSteep("client port", "string to integer server"));
        event.addSteep("string to integer server", new StringToIntegerSteep("server port", SetTransferServerAction.class.getSimpleName()));
        event.addSteep(new SetTransferServerAction());
        event.addSteep(new SetProxyTypeAction());
        event.addSteep(new StartServerAction());
        event.putValue("server", Server.getNewServer());
        event.putValue("client port", port);
        event.putValue("proxy type", "transfer");
        event.putValue("server ip", serverIP);
        event.putValue("server port", serverPort);
        event.runEvent("string to integer client");
    }
}
