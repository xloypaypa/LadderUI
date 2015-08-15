package view.page.kidPage;

import control.Operator;
import values.SystemCount;
import view.interfaceTool.button.HorizontalButtons;
import view.interfaceTool.button.HorizontalRadioButtons;
import view.interfaceTool.title.InputWithTitle;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by xlo on 15-6-22.
 * it's the status page.
 */
public class StatusPage extends TabbedPageKidPage {
    protected HorizontalRadioButtons horizontalRadioButtons;
    protected InputWithTitle clientPort;
    protected InputWithTitle serverIP, serverPort;
    protected HorizontalButtons horizontalButtons;
    protected JLabel io;

    public StatusPage() {
        super("status");
    }

    @Override
    public void getInstance() {
        clientPort = new InputWithTitle();
        clientPort.setBounds(10, 30, 250, 40);
        clientPort.setTitle("client port");
        clientPort.setWords("8000");
        show.add(clientPort);

        this.horizontalButtons = new HorizontalButtons();
        this.horizontalButtons.setBounds(10, 500, 250, 40);
        this.horizontalButtons.addButton("start", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (StatusPage.this.horizontalRadioButtons.getChoosen().getName().equals("http")) {
                    Operator.startHttp(clientPort.getWords());
                } else if (StatusPage.this.horizontalRadioButtons.getChoosen().getName().equals("server")) {
                    Operator.startServer(clientPort.getWords());
                } else {
                    Operator.startTransfer(clientPort.getWords(), serverIP.getWords(), serverPort.getWords());
                }
                io.setText("0");
                StatusPage.this.horizontalButtons.setVisible(false);
            }
        });
        show.add(this.horizontalButtons);

        io = new JLabel();
        io.setBounds(10, 0, 250, 20);
        io.setText("-1");
        show.add(io);

        this.serverIP = new InputWithTitle();
        this.serverIP.setBounds(10, 110, 250, 40);
        this.serverIP.setTitle("server ip:");
        show.add(this.serverIP);
        this.serverIP.setVisible(false);

        this.serverPort = new InputWithTitle();
        this.serverPort.setBounds(10, 160, 250, 40);
        this.serverPort.setTitle("server port:");
        show.add(this.serverPort);
        this.serverPort.setVisible(false);

        this.horizontalRadioButtons = new HorizontalRadioButtons();
        this.horizontalRadioButtons.setBounds(10, 80, 250, 20);
        this.horizontalRadioButtons.addButton("http", 75).addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatusPage.this.serverIP.setVisible(false);
                StatusPage.this.serverPort.setVisible(false);
            }
        });
        this.horizontalRadioButtons.addButton("server", 75).addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatusPage.this.serverIP.setVisible(false);
                StatusPage.this.serverPort.setVisible(false);
            }
        });
        this.horizontalRadioButtons.addButton("transfer").addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StatusPage.this.serverIP.setVisible(true);
                StatusPage.this.serverPort.setVisible(true);
            }
        });
        this.horizontalRadioButtons.setChoosen("http");
        show.add(this.horizontalRadioButtons);

        SystemCount.action = () -> StatusPage.this.io.setText(SystemCount.ioNumber + "");
    }

    @Override
    public void repaint() {

    }
}
