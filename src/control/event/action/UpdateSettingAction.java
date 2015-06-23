package control.event.action;

import control.event.AbstractAction;
import control.event.tool.ValueChecker;
import control.listener.ListenerManager;
import javafx.util.Pair;
import values.Settings;

/**
 * Created by xlo on 15-6-23.
 * it's updating setting's action
 */
public class UpdateSettingAction extends AbstractAction {
    @Override
    protected void run() {
        Settings.bufferSize = (int) this.eventCallBack.getValue("buffer size");
        Settings.connectPause = (int) this.eventCallBack.getValue("connect pause");
        Settings.retryTimes = (int) this.eventCallBack.getValue("retry time");
        Settings.talkRetryTimes = (int) this.eventCallBack.getValue("talk retry time");
        Settings.timeLimit = (int) this.eventCallBack.getValue("time limit");
    }

    @Override
    protected boolean checkNeedData() {
        ValueChecker valueChecker = new ValueChecker();
        valueChecker.setEventCallBack(this.eventCallBack);
        valueChecker.addItem(new Pair<>("buffer size", Integer.class));
        valueChecker.addItem(new Pair<>("connect pause", Integer.class));
        valueChecker.addItem(new Pair<>("retry time", Integer.class));
        valueChecker.addItem(new Pair<>("talk retry time", Integer.class));
        valueChecker.addItem(new Pair<>("time limit", Integer.class));
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
