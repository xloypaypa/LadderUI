package view.page.kidPage;

import control.Operator;
import view.interfaceTool.button.HorizontalButtons;
import view.interfaceTool.title.InputWithTitle;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by xlo on 2015/8/7.
 * it's the client page
 */
public class ClientPage extends TabbedPageKidPage {
    protected InputWithTitle robotName, ip, port;
    protected HorizontalButtons horizontalButtons;

    public ClientPage() {
        super("client");
    }

    @Override
    public void getInstance() {
        this.ip = new InputWithTitle();
        this.ip.setBounds(10, 10, 250, 40);
        this.ip.setTitle("ip");
        this.show.add(this.ip);

        this.port = new InputWithTitle();
        this.port.setBounds(10, 60, 250, 40);
        this.port.setTitle("port");
        this.show.add(this.port);

        this.robotName = new InputWithTitle();
        this.robotName.setBounds(10, 110, 250, 40);
        this.robotName.setTitle("robot name");
        this.show.add(this.robotName);

        horizontalButtons = new HorizontalButtons();
        horizontalButtons.setBounds(10, 500, 250, 40);
        horizontalButtons.addButton("run client", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Operator.runClient(ip.getWords(), port.getWords(), robotName.getWords());
            }
        });
        show.add(horizontalButtons);
    }

    @Override
    public void repaint() {

    }
}
