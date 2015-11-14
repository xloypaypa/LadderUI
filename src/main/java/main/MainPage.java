package main;

import net.MethodSolver;
import net.server.Server;
import net.server.proxy.TransferProxyReadServer;
import net.tool.connectionSolver.ConnectionMessageImpl;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by xlo on 15-11-14.
 * it's the main page
 */
public class MainPage {
    private JPanel panel;
    private JTextField serverIp;
    private JTextField proxyPort;
    private JTextField proxyThread;
    private JButton startProxyButton;
    private JTextField transferPort;
    private JTextField transferThread;
    private JTextField transferServerIp;
    private JTextField transferServerPort;
    private JButton startTransferButton;
    private JTextField filePort;
    private JTextField fileThread;
    private JButton startFileServerButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainPage");
        frame.setContentPane(new MainPage().panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public MainPage() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            this.serverIp.setText(address.getHostAddress());
        } catch (UnknownHostException e) {
            showError(e);
        }

        this.proxyPort.setText("8080");
        this.proxyThread.setText("5");

        this.transferPort.setText("8000");
        this.transferServerPort.setText("8080");
        this.transferServerIp.setText("127.0.0.1");
        this.transferThread.setText("5");

        this.filePort.setText("8888");
        this.fileThread.setText("5");
        startProxyButton.addActionListener(e -> {
            try {
                proxyPort.setEnabled(false);
                proxyThread.setEnabled(false);
                startProxyButton.setEnabled(false);
                startProxy(getValue(proxyPort), getValue(proxyThread));
            } catch (Exception e1) {
                showError(e1);
            }
        });
        startTransferButton.addActionListener(e -> {
            try {
                transferPort.setEnabled(false);
                transferThread.setEnabled(false);
                transferServerPort.setEnabled(false);
                transferServerIp.setEnabled(false);
                startTransferButton.setEnabled(false);
                startTransfer(getValue(transferPort), transferServerIp.getText(), getValue(transferServerPort), getValue(transferThread));
            } catch (Exception e1) {
                showError(e1);
            }
        });
        startFileServerButton.addActionListener(e -> {
            try {
                filePort.setEnabled(false);
                fileThread.setEnabled(false);
                startFileServerButton.setEnabled(false);
                startFileServer(getValue(filePort), getValue(fileThread));
            } catch (Exception e1) {
                showError(e1);
            }
        });
    }

    private int getValue(JTextField jTextField) {
        return Integer.valueOf(jTextField.getText());
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
    }

    private static void startFileServer(int port, int selectorNum) {
        new Thread(() -> {
            Server server = Server.getNewServer("file", MethodSolver::new);
            server.getInstance(port, selectorNum);
            server.accept();
        }).start();
    }

    private static void startTransfer(int port, String serverHost, int serverPort, int selectorNum) {
        new Thread(() -> {
            Server server = Server.getNewServer("transfer", () -> new TransferProxyReadServer(new ConnectionMessageImpl(), serverHost, serverPort));
            server.getInstance(port, selectorNum);
            server.accept();
        }).start();
    }

    private static void startProxy(int port, int selectorNum) {
        new Thread(() -> {
            Server server = Server.getNewServer("proxy");
            server.getInstance(port, selectorNum);
            server.accept();
        }).start();
    }
}
