package model;

import control.listener.ListenerManager;
import log.NormalLog;

/**
 * Created by xlo on 15-6-23.
 * it's the error log
 */
public class ErrorLog extends NormalLog {

    @Override
    public void writeLog(String event, String message) {
        ListenerManager.setErrorMessage(message);
        ListenerManager.UIAction();
    }
}
