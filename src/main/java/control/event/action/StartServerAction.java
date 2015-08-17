package control.event.action;

import control.event.AbstractAction;
import control.event.tool.ValueChecker;
import control.listener.ListenerManager;
import javafx.util.Pair;
import server.Server;

/**
 * Created by xlo on 15-6-23.
 * it's the action to start server
 */
public class StartServerAction extends AbstractAction {
    @Override
    protected void run() {
        Server server = (Server) this.eventCallBack.getValue("server");
        server.getInstance((Integer) this.eventCallBack.getValue("client port"));
        server.accept();
    }

    @Override
    protected boolean checkNeedData() {
        ValueChecker valueChecker = new ValueChecker();
        valueChecker.setEventCallBack(this.eventCallBack);
        valueChecker.addItem(new Pair<>("client port", Integer.class));
        valueChecker.addItem(new Pair<>("server", Server.class));
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
