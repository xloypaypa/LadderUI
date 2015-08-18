package control.event.action;

import control.action.DirectRunAction;
import values.Settings;

/**
 * Created by xlo on 15-6-23.
 * it's updating setting's action
 */
public class UpdateSettingAction extends DirectRunAction {
    int bufferSize, connectPause, retryTime, talkRetryTime, timeLimit;

    public UpdateSettingAction(int bufferSize, int connectPause, int retryTime, int talkRetryTime, int timeLimit) {
        this.bufferSize = bufferSize;
        this.connectPause = connectPause;
        this.retryTime = retryTime;
        this.talkRetryTime = talkRetryTime;
        this.timeLimit = timeLimit;
    }

    @Override
    protected void loadNextEvents() {

    }

    @Override
    public Boolean call() throws Exception {
        Settings.bufferSize = this.bufferSize;
        Settings.connectPause = this.connectPause;
        Settings.retryTimes = this.retryTime;
        Settings.talkRetryTimes = this.talkRetryTime;
        Settings.timeLimit = this.timeLimit;
        return true;
    }
}
