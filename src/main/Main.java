package main;

import control.listener.ListenerManager;
import log.ConsoleLog;
import log.LogManager;
import model.log.ErrorLog;
import model.listener.NormalListener;
import values.SystemStrings;
import view.MainWindow;
import view.main.WindowManager;
import view.page.MainPage;
import view.page.kidPage.ClientPage;
import view.page.kidPage.ScriptPage;
import view.page.kidPage.SettingPage;
import view.page.kidPage.StatusPage;

/**
 * Created by xlo on 15-6-21.
 * it's the main
 */
public class Main {

    public static void main(String[] args) {
        LogManager.getLogManager().putLog(SystemStrings.readHead, new ConsoleLog());
        LogManager.getLogManager().putLog(SystemStrings.sendHead, new ConsoleLog());

        try {
            setUpLog();
            setUpWindow();
        } catch (Exception e) {
            LogManager.getLogManager().writeLog(SystemStrings.exception, e.getMessage());
        }
    }

    private static void setUpWindow() {
        ListenerManager.addListener("main", new NormalListener());
        ListenerManager.useListenser("main");
        LogManager.getLogManager().putLog(SystemStrings.exception, new ErrorLog());

        MainWindow mainWindow = new MainWindow();

        mainWindow.getInstance("main");
        MainPage page = new MainPage();
        page.addPage(new StatusPage());
        page.addPage(new SettingPage());
        page.addPage(new ClientPage());
        page.addPage(new ScriptPage());
        mainWindow.addPage(page);

        WindowManager.addWindow(mainWindow);
        WindowManager.createWindow("main");
    }

    private static void setUpLog() {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setPath("./error.log");
        LogManager.getLogManager().putLog(SystemStrings.exception, errorLog);
    }
}
