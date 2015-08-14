package control.event.action;

import control.event.AbstractAction;
import control.event.tool.ValueChecker;
import control.listener.ListenerManager;
import javafx.util.Pair;
import server.serverSolver.proxyServer.proxyThirdServer.TransferServer;

/**
 * Created by xlo on 15-6-23.
 * it's the action to set transfer server
 */
public class SetTransferServerAction extends AbstractAction {
    @Override
    protected void run() {
        TransferServer.setHost((String) this.eventCallBack.getValue("server ip"));
        TransferServer.setPort((Integer) this.eventCallBack.getValue("server port"));
    }

    @Override
    protected boolean checkNeedData() {
        ValueChecker valueChecker = new ValueChecker();
        valueChecker.setEventCallBack(this.eventCallBack);
        valueChecker.addItem(new Pair<>("server ip", String.class));
        valueChecker.addItem(new Pair<>("server port", Integer.class));
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
        return SetProxyTypeAction.class.getSimpleName();
    }
}
