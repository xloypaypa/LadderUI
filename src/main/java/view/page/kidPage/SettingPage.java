package view.page.kidPage;

import control.Operator;
import values.Settings;
import view.interfaceTool.button.HorizontalButtons;
import view.interfaceTool.title.InputWithTitle;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by xlo on 15-6-21.
 * it's settings page.
 */
public class SettingPage extends TabbedPageKidPage {
    protected InputWithTitle bufferSize, connectPause, retryTimes, talkRetryTimes, timeLimit;
    protected HorizontalButtons horizontalButtons;

    public SettingPage() {
        super("setting");
    }

    @Override
    public void getInstance() {
        bufferSize = new InputWithTitle();
        bufferSize.setBounds(10, 10, 250, 40);
        bufferSize.setTitle("buffer size");
        this.show.add(bufferSize);

        connectPause = new InputWithTitle();
        connectPause.setBounds(10, 60, 250, 40);
        connectPause.setTitle("connect Pause");
        this.show.add(connectPause);

        retryTimes = new InputWithTitle();
        retryTimes.setBounds(10, 110, 250, 40);
        retryTimes.setTitle("retry times");
        this.show.add(retryTimes);

        talkRetryTimes = new InputWithTitle();
        talkRetryTimes.setBounds(10, 160, 250, 40);
        talkRetryTimes.setTitle("talk retry times");
        this.show.add(talkRetryTimes);

        timeLimit = new InputWithTitle();
        timeLimit.setBounds(10, 210, 250, 40);
        timeLimit.setTitle("time limit");
        this.show.add(timeLimit);

        horizontalButtons = new HorizontalButtons();
        horizontalButtons.setBounds(10, 500, 250, 40);
        horizontalButtons.addButton("update setting", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Operator.updateSetting(bufferSize.getWords(), connectPause.getWords(), retryTimes.getWords(),
                        talkRetryTimes.getWords(), timeLimit.getWords());
                loadData();
            }
        });
        show.add(horizontalButtons);

        loadData();
    }

    @Override
    public void repaint() {

    }

    private void loadData() {
        bufferSize.setWords(Settings.bufferSize + "");
        connectPause.setWords(Settings.connectPause + "");
        retryTimes.setWords(Settings.retryTimes + "");
        talkRetryTimes.setWords(Settings.talkRetryTimes + "");
        timeLimit.setWords(Settings.timeLimit + "");
    }
}
