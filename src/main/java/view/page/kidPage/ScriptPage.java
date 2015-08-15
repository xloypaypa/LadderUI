package view.page.kidPage;

import control.Operator;
import view.interfaceTool.button.HorizontalButtons;
import view.interfaceTool.title.FileChooseWithTitle;
import view.interfaceTool.title.InputWithTitle;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by xlo on 15-8-4.
 * load, run script
 */
public class ScriptPage extends TabbedPageKidPage {
    protected FileChooseWithTitle path;
    protected InputWithTitle code;
    protected HorizontalButtons horizontalButtons;

    public ScriptPage() {
        super("script");
    }

    @Override
    public void getInstance() {
        this.path = new FileChooseWithTitle();
        this.path.setBounds(10, 10, 250, 40);
        this.path.setTitle("script path");
        this.show.add(path);

        this.code = new InputWithTitle();
        this.code.setBounds(10, 60, 250, 40);
        this.code.setTitle("script code");
        this.show.add(this.code);

        horizontalButtons = new HorizontalButtons();
        horizontalButtons.setBounds(10, 500, 250, 40);
        horizontalButtons.addButton("load file", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Operator.loadScript(path.getWords());
                path.clearWords();
            }
        });

        horizontalButtons.addBreak();

        horizontalButtons.addButton("run script", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Operator.runScript(code.getWords());
                code.clearWords();
            }
        });
        show.add(horizontalButtons);
    }

    @Override
    public void repaint() {

    }
}
