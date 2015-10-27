package main;

import net.MethodSolver;
import net.server.Server;
import net.server.proxy.TransferProxyReadServer;
import net.tool.connectionSolver.ConnectionMessageImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by xlo on 15-6-21.
 * it's the main
 */
public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("LADDER");
        frame.setSize(300, 600);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.setBounds(0, 0, 300, 600);
        frame.add(jTabbedPane);

        proxyPage(jTabbedPane);
        transferProxyPage(jTabbedPane);
        fileServerPage(jTabbedPane);
    }

    protected static void fileServerPage(JTabbedPane jTabbedPane) {
        JPanel panel = new JPanel(null, true);
        panel.setBounds(0, 0, 300, 600);
        jTabbedPane.add("file", panel);

        JLabel portLabel = new JLabel("port:");
        portLabel.setBounds(10, 10, 70, 20);
        panel.add(portLabel);

        JTextField port = new JTextField("8081");
        port.setBounds(80, 10, 100, 20);
        panel.add(port);

        JButton button = new JButton("start");
        button.setBounds(10, 500, 200, 20);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            startServer(Integer.valueOf(port.getText()));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.start();
                button.setEnabled(false);
                port.setEnabled(false);
                panel.updateUI();
            }
        });
        panel.add(button);
        panel.updateUI();
    }

    protected static void transferProxyPage(JTabbedPane jTabbedPane) {
        JPanel panel = new JPanel(null, true);
        panel.setBounds(0, 0, 300, 600);
        jTabbedPane.add("transfer", panel);

        JLabel portLabel = new JLabel("port:");
        portLabel.setBounds(10, 10, 70, 20);
        panel.add(portLabel);

        JTextField port = new JTextField("8000");
        port.setBounds(80, 10, 100, 20);
        panel.add(port);

        JLabel serverHostLabel = new JLabel("server host:");
        serverHostLabel.setBounds(10, 40, 70, 20);
        panel.add(serverHostLabel);

        JTextField serverHost = new JTextField("127.0.0.1");
        serverHost.setBounds(80, 40, 100, 20);
        panel.add(serverHost);

        JLabel serverPortLabel = new JLabel("server port:");
        serverPortLabel.setBounds(10, 70, 70, 20);
        panel.add(serverPortLabel);

        JTextField serverPort = new JTextField("8080");
        serverPort.setBounds(80, 70, 100, 20);
        panel.add(serverPort);

        JButton button = new JButton("start");
        button.setBounds(10, 500, 200, 20);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            startTransfer(Integer.valueOf(port.getText()), serverHost.getText(), Integer.valueOf(serverPort.getText()));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.start();
                button.setEnabled(false);
                port.setEnabled(false);
                serverHost.setEnabled(false);
                serverPort.setEnabled(false);
                panel.updateUI();
            }
        });
        panel.add(button);
        panel.updateUI();
    }

    protected static void proxyPage(JTabbedPane jTabbedPane) {
        JPanel panel = new JPanel(null, true);
        panel.setBounds(0, 0, 300, 600);
        jTabbedPane.add("proxy", panel);

        JLabel portLabel = new JLabel("port:");
        portLabel.setBounds(10, 10, 70, 20);
        panel.add(portLabel);

        JTextField port = new JTextField("8080");
        port.setBounds(80, 10, 100, 20);
        panel.add(port);

        JButton button = new JButton("start");
        button.setBounds(10, 500, 200, 20);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            startProxy(Integer.valueOf(port.getText()));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.start();
                button.setEnabled(false);
                port.setEnabled(false);
                panel.updateUI();
            }
        });
        panel.add(button);
        panel.updateUI();
    }

    private static void startServer(int port) {
        Server server = Server.getNewServer(MethodSolver::new);
        server.getInstance(port, 5);
        server.accept();
    }

    private static void startTransfer(int port, String serverHost, int serverPort) {
        Server server = Server.getNewServer(() -> new TransferProxyReadServer(new ConnectionMessageImpl(), serverHost, serverPort));
        server.getInstance(port, 5);
        server.accept();
    }

    private static void startProxy(int port) {
        Server server = Server.getNewServer();
        server.getInstance(port, 5);
        server.accept();
    }
}
