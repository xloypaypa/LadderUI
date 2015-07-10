package control.event.action;

import control.event.AbstractAction;
import control.event.tool.ValueChecker;
import control.listener.ListenerManager;
import javafx.util.Pair;
import proxy.ProxyServer;

/**
 * Created by xlo on 15-6-23.
 * it's the action to start server
 */
public class StartServerAction extends AbstractAction {
    @Override
    protected void run() {
        ProxyServer.getInstance((Integer) this.eventCallBack.getValue("client port"));
        ProxyServer.accept();
    }

    @Override
    protected boolean checkNeedData() {
        ValueChecker valueChecker = new ValueChecker();
        valueChecker.setEventCallBack(this.eventCallBack);
        valueChecker.addItem(new Pair<>("client port", Integer.class));
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
