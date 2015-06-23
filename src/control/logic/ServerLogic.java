package control.logic;

import control.event.*;
import control.event.action.SetProxyTypeAction;
import control.event.action.StartServerAction;
import control.event.steep.StringToIntegerSteep;

/**
 * Created by xlo on 15-6-23.
 * it's server's logic including starting and stopping.
 */
public class ServerLogic {
    public static void startHttp (String port) {
        NormalEvent event = new NormalEvent();
        event.addSteep(new StringToIntegerSteep("client port"));
        event.addSteep(new SetProxyTypeAction());
        event.addSteep(new StartServerAction());
        event.putValue("client port", port);
        event.putValue("proxy type", "http");
        event.runEvent();
    }

    public static void startTransfer (String port, String serverIP, String serverPort) {
        NormalEvent event = new NormalEvent();
        event.addSteep(new StringToIntegerSteep("client port"));
        event.addSteep(new StringToIntegerSteep("server port"));
        event.addSteep(new SetProxyTypeAction());
        event.addSteep(new StartServerAction());
        event.putValue("client port", port);
        event.putValue("proxy type", "transfer");
        event.putValue("server ip", serverIP);
        event.putValue("server port", serverPort);
        event.runEvent();
    }
}
