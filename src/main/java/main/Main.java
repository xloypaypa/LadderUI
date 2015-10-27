package main;

import net.MethodSolver;
import net.server.Server;
import net.server.proxy.TransferProxyReadServer;
import net.tool.connectionSolver.ConnectionMessageImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.setBounds(0, 0, 300, 600);
        frame.add(jTabbedPane);

        try {
            proxyPage(jTabbedPane);
            transferProxyPage(jTabbedPane);
            fileServerPage(jTabbedPane);
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }

    protected static void fileServerPage(JTabbedPane jTabbedPane) throws UnknownHostException {
        JPanel panel = new JPanel(null, true);
        panel.setBounds(0, 0, 300, 600);
        jTabbedPane.add("file", panel);

        showIp(panel);

        JTextField port = addThreadNum(panel, "port:", "8081", 10, 30);
        JTextField num = addThreadNum(panel, "work thread:", "5", 150, 30);

        JButton button = new JButton("start");
        button.setBounds(10, 500, 200, 20);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            startServer(Integer.valueOf(port.getText()), Integer.valueOf(num.getText()));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.start();
                button.setEnabled(false);
                port.setEnabled(false);
                num.setEnabled(false);
                panel.updateUI();
            }
        });
        panel.add(button);
        panel.updateUI();
    }

    protected static void transferProxyPage(JTabbedPane jTabbedPane) throws UnknownHostException {
        JPanel panel = new JPanel(null, true);
        panel.setBounds(0, 0, 300, 600);
        jTabbedPane.add("transfer", panel);

        showIp(panel);

        JTextField port = addThreadNum(panel, "port:", "8000", 10, 30);
        JTextField num = addThreadNum(panel, "work thread:", "5", 150, 30);

        JTextField serverHost = addThreadNum(panel, "server host:", "127.0.0.1", 10, 80);
        JTextField serverPort = addThreadNum(panel, "server port:", "8080", 150, 80);

        JButton button = new JButton("start");
        button.setBounds(10, 500, 200, 20);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            startTransfer(Integer.valueOf(port.getText()), serverHost.getText(), Integer.valueOf(serverPort.getText()), Integer.valueOf(num.getText()));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.start();
                button.setEnabled(false);
                port.setEnabled(false);
                serverHost.setEnabled(false);
                serverPort.setEnabled(false);
                num.setEnabled(false);
                panel.updateUI();
            }
        });
        panel.add(button);
        panel.updateUI();
    }

    protected static void proxyPage(JTabbedPane jTabbedPane) throws UnknownHostException {
        JPanel panel = new JPanel(null, true);
        panel.setBounds(0, 0, 300, 600);
        jTabbedPane.add("proxy", panel);

        showIp(panel);

        JTextField port = addThreadNum(panel, "port:", "8080", 10, 30);
        JTextField num = addThreadNum(panel, "work thread:", "5", 150, 30);

        JButton button = new JButton("start");
        button.setBounds(10, 500, 200, 20);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            startProxy(Integer.valueOf(port.getText()), Integer.valueOf(num.getText()));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.start();
                button.setEnabled(false);
                port.setEnabled(false);
                num.setEnabled(false);
                panel.updateUI();
            }
        });
        panel.add(button);
        panel.updateUI();
    }

    private static void showIp(JPanel panel) throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        JLabel label = new JLabel("your ip:");
        label.setBounds(10, 10, 50, 20);
        panel.add(label);

        JTextField jLabel = new JTextField(address.getHostAddress());
        jLabel.setBounds(60, 10, 200, 20);
        jLabel.setEditable(false);
        panel.add(jLabel);
    }

    private static JTextField addThreadNum(JPanel panel, String title, String value, int x, int height) {
        JLabel numLabel = new JLabel(title);
        numLabel.setBounds(x, height, 100, 20);
        panel.add(numLabel);

        JTextField num = new JTextField(value);
        num.setBounds(x, height + 20, 100, 20);
        panel.add(num);
        return num;
    }

    private static void startServer(int port, int selectorNum) {
        Server server = Server.getNewServer(MethodSolver::new);
        server.getInstance(port, selectorNum);
        server.accept();
    }

    private static void startTransfer(int port, String serverHost, int serverPort, int selectorNum) {
        Server server = Server.getNewServer(() -> new TransferProxyReadServer(new ConnectionMessageImpl(), serverHost, serverPort));
        server.getInstance(port, selectorNum);
        server.accept();
    }

    private static void startProxy(int port, int selectorNum) {
        Server server = Server.getNewServer();
        server.getInstance(port, selectorNum);
        server.accept();
    }
}
