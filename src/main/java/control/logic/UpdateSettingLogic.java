package control.logic;

import control.event.action.UpdateSettingAction;
import control.event.steep.StringToIntegerSteep;

/**
 * Created by xlo on 15-6-23.
 * it's the logic of updating setting
 */
public class UpdateSettingLogic {
    public static void updateSetting(String bufferSize, String connectPause, String retryTimes,
                                     String talkRetryTimes, String timeLimit) {
        new StringToIntegerSteep(bufferSize, connectPause, retryTimes, talkRetryTimes, timeLimit) {
            @Override
            protected void loadNextEvents() {
                this.addDoneEvent(new UpdateSettingAction(this.result[0], this.result[1], this.result[2], this.result[3], this.result[4]));
            }
        }.run();
    }
}
