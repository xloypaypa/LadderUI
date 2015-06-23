package view;

import view.main.NormalWindow;

import javax.swing.*;

/**
 * Created by xlo on 15-6-21.
 * it's the main window of this app
 */
public class MainWindow extends NormalWindow {

    final static String windowName = "main";

    @Override
    public void createWindow() {
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.window.setBounds(300, 100, 300, 600);
        this.window.setLayout(null);
        this.window.setResizable(false);
        this.window.setVisible(true);

        this.showPage("main");
    }

    @Override
    public void dispose() {
        this.window.setVisible(false);
    }

    @Override
    public boolean isThisName(String s) {
        return s.equals(windowName);
    }

    @Override
    public String getClassName() {
        return windowName;
    }
}
