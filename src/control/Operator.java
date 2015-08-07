package control;

import control.listener.ListenerManager;
import control.logic.ClientLogic;
import control.logic.ServerLogic;
import control.logic.UpdateSettingLogic;
import script.ForceCacheScriptManager;

import java.io.IOException;

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

    public static void startServer(String port) {
        isOpen = true;
        Operator.port = port;
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                ServerLogic.startServer(port);
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

    public static void loadScript(String path) {
        try {
            ForceCacheScriptManager.getForceCacheScriptManager().runScript(path);
            ListenerManager.setOKMessage();
            ListenerManager.UIAction();
        } catch (Exception e) {
            ListenerManager.setErrorMessage(e.getMessage());
            ListenerManager.UIAction();
        }
    }

    public static void runScript(String code) {
        try {
            Object object = ForceCacheScriptManager.getForceCacheScriptManager().runCommand(code);
            ListenerManager.setInfoMessage(object.toString());
            ListenerManager.UIAction();
        } catch (Exception e) {
            ListenerManager.setErrorMessage(e.getMessage());
            ListenerManager.UIAction();
        }
    }

    public static void runClient(String ip, String port, String robotName, String code) {
        try {
            ForceCacheScriptManager.getForceCacheScriptManager().runScript(code);
        } catch (IOException e) {
            ListenerManager.setErrorMessage(e.getMessage());
            ListenerManager.UIAction();
            return ;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                ClientLogic.startClient(ip, port, robotName);
            }
        }.start();
    }
}
