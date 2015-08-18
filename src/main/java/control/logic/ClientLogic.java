package control.logic;

import control.event.action.StartClientAction;
import control.event.steep.StringToIntegerSteep;

/**
 * Created by xlo on 2015/8/7.
 * it's the client logic
 */
public class ClientLogic {
    public static void startClient(String ip, String port, String robotName) {
        new StringToIntegerSteep(port) {
            @Override
            protected void loadNextEvents() {
                this.addDoneEvent(new StartClientAction(robotName, ip, this.result[0]));
            }
        }.run();
    }
}
