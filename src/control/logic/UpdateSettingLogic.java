package control.logic;

import control.event.NormalEvent;
import control.event.action.UpdateSettingAction;
import control.event.steep.StringToIntegerSteep;

/**
 * Created by xlo on 15-6-23.
 * it's the logic of updating setting
 */
public class UpdateSettingLogic {
    public static void updateSetting(String bufferSize, String connectPause, String retryTimes,
                                     String talkRetryTimes, String timeLimit) {
        NormalEvent event = new NormalEvent();
        event.addSteep(new StringToIntegerSteep("buffer size"));
        event.addSteep(new StringToIntegerSteep("connect pause"));
        event.addSteep(new StringToIntegerSteep("retry time"));
        event.addSteep(new StringToIntegerSteep("talk retry time"));
        event.addSteep(new StringToIntegerSteep("time limit"));
        event.addSteep(new UpdateSettingAction());

        event.putValue("buffer size", bufferSize);
        event.putValue("connect pause", connectPause);
        event.putValue("retry time", retryTimes);
        event.putValue("talk retry time", talkRetryTimes);
        event.putValue("time limit", timeLimit);

        event.runEvent();
    }
}
