package control.event.action;

import client.Client;
import client.computerRobot.ComputerRobot;
import client.computerRobot.RobotOperationManager;
import control.action.DirectRunAction;
import tool.head.reader.NormalReplyHeadReader;
import tool.head.writer.CustomRequestHeadWriter;

/**
 * Created by xlo on 2015/8/7.
 * it's the action start client
 */
public class StartClientAction extends DirectRunAction {
    String name, host;
    Client client;
    int port;

    public StartClientAction(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    @Override
    protected void loadNextEvents() {

    }

    @Override
    public Boolean call() throws Exception {
        client = Client.getNewClient();
        ComputerRobot computerRobot = new ComputerRobot();
        computerRobot.setRobotOperation(RobotOperationManager.getRobotOperationManager().get(name));
        computerRobot.setReplyHeadReader(new NormalReplyHeadReader());
        computerRobot.setRequestHeadWriter(new CustomRequestHeadWriter());
        client.connect(host, port, computerRobot);
        return true;
    }
}
