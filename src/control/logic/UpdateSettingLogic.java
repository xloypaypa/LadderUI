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
        event.addSteep("buffer", new StringToIntegerSteep("buffer size", "connect"));
        event.addSteep("connect", new StringToIntegerSteep("connect pause", "retry"));
        event.addSteep("retry", new StringToIntegerSteep("retry time", "talk"));
        event.addSteep("talk", new StringToIntegerSteep("talk retry time", "time"));
        event.addSteep("time", new StringToIntegerSteep("time limit", UpdateSettingAction.class.getSimpleName()));
        event.addSteep(new UpdateSettingAction());

        event.putValue("buffer size", bufferSize);
        event.putValue("connect pause", connectPause);
        event.putValue("retry time", retryTimes);
        event.putValue("talk retry time", talkRetryTimes);
        event.putValue("time limit", timeLimit);

        event.runEvent("buffer");
    }
}
