package control;

import control.logic.ServerLogic;
import control.logic.UpdateSettingLogic;

/**
 * Created by xlo on 15-6-23.
 * it's the main logic
 */
public class Operator {
    public static boolean isOpen;
    private static Thread thread;
    private static String port;

    public static void startHttp(String port) {
        isOpen = true;
        Operator.port = port;
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                ServerLogic.startHttp(port);
            }
        };
        thread.start();
    }

    public static void startTransfer(String port, String serverIP, String serverPort) {
        isOpen = true;
        Operator.port = port;
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                ServerLogic.startTransfer(port, serverIP, serverPort);
            }
        };
        thread.start();
    }

    public static void updateSetting(String bufferSize, String connectPause, String retryTimes,
                                     String talkRetryTimes, String timeLimit) {
        UpdateSettingLogic.updateSetting(bufferSize, connectPause, retryTimes, talkRetryTimes, timeLimit);
    }
}
