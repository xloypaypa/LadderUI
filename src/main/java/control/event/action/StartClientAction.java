package control.event.action;

import client.Client;
import client.computerRobot.ComputerRobot;
import client.computerRobot.RobotOperationManager;
import control.event.AbstractAction;
import control.event.tool.ValueChecker;
import control.listener.ListenerManager;
import javafx.util.Pair;
import tool.head.reader.NormalReplyHeadReader;
import tool.head.writer.CustomRequestHeadWriter;

/**
 * Created by xlo on 2015/8/7.
 * it's the action start client
 */
public class StartClientAction extends AbstractAction {
    @Override
    protected void run() {
        ComputerRobot computerRobot = new ComputerRobot();
        computerRobot.setRobotOperation(RobotOperationManager.getRobotOperationManager().get((String) this.eventCallBack.getValue("name")));
        computerRobot.setReplyHeadReader(new NormalReplyHeadReader());
        computerRobot.setRequestHeadWriter(new CustomRequestHeadWriter());
        Client.connect((String) this.eventCallBack.getValue("host"), (Integer) this.eventCallBack.getValue("port"), computerRobot);
    }

    @Override
    protected boolean checkNeedData() {
        ValueChecker valueChecker = new ValueChecker();
        valueChecker.setEventCallBack(this.eventCallBack);
        valueChecker.addItem(new Pair<>("port", Integer.class));
        valueChecker.addItem(new Pair<>("host", String.class));
        valueChecker.addItem(new Pair<>("name", String.class));
        if (valueChecker.checkAllItem()) {
            return true;
        } else {
            ListenerManager.setErrorMessage("Data not found!");
            ListenerManager.UIAction();
            return false;
        }
    }

    @Override
    protected void putData() {

    }

    @Override
    protected String getNextSteep() {
        return null;
    }
}
