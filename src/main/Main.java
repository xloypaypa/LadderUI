package main;

import control.listener.ListenerManager;
import log.LogManager;
import model.ErrorLog;
import model.listener.NormalListener;
import values.SystemStrings;
import view.MainWindow;
import view.main.WindowManager;
import view.page.MainPage;
import view.page.kidPage.SettingPage;
import view.page.kidPage.StatusPage;

/**
 * Created by xlo on 15-6-21.
 * it's the main
 */
public class Main {

    public static void main(String[] args) {
        setUpLog();
        setUpWindow();
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
