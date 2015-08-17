package control.logic;

import client.Client;
import control.event.NormalEvent;
import control.event.action.StartClientAction;
import control.event.steep.StringToIntegerSteep;

/**
 * Created by xlo on 2015/8/7.
 * it's the client logic
 */
public class ClientLogic {
    public static void startClient(String ip, String port, String robotName) {
        NormalEvent event = new NormalEvent();
        event.addSteep(new StringToIntegerSteep("port", StartClientAction.class.getSimpleName()));
        event.addSteep(new StartClientAction());
        event.putValue("client", Client.getNewClient());
        event.putValue("port", port);
        event.putValue("host", ip);
        event.putValue("name", robotName);
        event.runEvent(StringToIntegerSteep.class.getSimpleName());
    }
}
