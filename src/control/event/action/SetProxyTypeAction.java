package control.event.action;

import control.event.AbstractAction;
import control.event.tool.ValueChecker;
import control.listener.ListenerManager;
import javafx.util.Pair;
import proxy.proxy.ProxyManager;

/**
 * Created by xlo on 15-6-23.
 * it's the action of set proxy type.
 */
public class SetProxyTypeAction extends AbstractAction {
    @Override
    protected void run() {
        ProxyManager.setNowProxy((String) this.eventCallBack.getValue("proxy type"));
    }

    @Override
    protected boolean checkNeedData() {
        ValueChecker valueChecker = new ValueChecker();
        valueChecker.setEventCallBack(this.eventCallBack);
        valueChecker.addItem(new Pair<>("proxy type", String.class));
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
}
