package main;

import net.*;
import net.server.Server;
import net.server.proxy.TransferProxyReadServer;
import net.tool.connectionSolver.ConnectionMessageImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Clock;

/**
 * Created by xlo on 15-11-14.
 * it's the main page
 */
public class MainPage {
    private static MainPage mainPage;
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
    private JTextField downloadServerPort;
    private JTextField downloadServerNum;
    private JButton startDownloadServerButton;
    private JTextField downloadClientIp;
    private JTextField downloadAimPath;
    private JTextField downloadFilePath;
    private JButton startDownloadButton;
    private JProgressBar downloadStatus;
    private JTextField downloadClientPort;

    private long allSize, nowSize;

    public static MainPage getMainPage() {
        return mainPage;
    }

    public static void main(String[] args) {

        new Thread() {
            @Override
            public void run() {
                try {
                    Client client = Client.getClient();
                    client.startSelector();
                    client.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        JFrame frame = new JFrame("MainPage");
        mainPage = new MainPage();
        frame.setContentPane(mainPage.panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        if (System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowIconified(WindowEvent e) {
                    frame.setVisible(false);
                }
            });
            try {
                SystemTray tray = SystemTray.getSystemTray();
                TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit()
                        .getImage(Clock.class.getResource("/javax/swing/plaf/basic/icons/JavaCup16.png")));
                trayIcon.setImageAutoSize(true);
                trayIcon.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() >= 2) {
                            frame.setVisible(true);
                        }
                    }
                });
                tray.add(trayIcon);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
        }

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

        this.downloadServerPort.setText("9999");
        this.downloadServerNum.setText("5");

        this.downloadClientPort.setText("9999");

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
        startDownloadServerButton.addActionListener(e -> {
            try {
                downloadServerPort.setEnabled(false);
                downloadServerNum.setEnabled(false);
                startDownloadServerButton.setEnabled(false);
                startDownloadServer(getValue(downloadServerPort), getValue(downloadServerNum));
            } catch (Exception e1) {
                showError(e1);
            }
        });
        startDownloadButton.addActionListener(e -> {
            enableClient(false);
            try {
                startDownload(this.downloadClientIp.getText(), Integer.valueOf(this.downloadClientPort.getText()), this.downloadAimPath.getText(), this.downloadFilePath.getText());
            } catch (Exception e1) {
                showError(e1);
            }
        });
    }

    public void setAllSize(long allSize) {
        this.allSize = allSize;
    }

    public void zeroNowSize() {
        setNowSize(0);
    }

    public void addNowSize(long value) {
        setNowSize(nowSize + value);
    }

    protected void setNowSize(long nowSize) {
        this.nowSize = nowSize;
        this.downloadStatus.setValue((int) (nowSize * 100 / allSize));
        if (nowSize == allSize) {
            enableClient(true);
        }
    }

    private int getValue(JTextField jTextField) {
        return Integer.valueOf(jTextField.getText());
    }

    private static void showError(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
    }

    private void enableClient(boolean enabled) {
        this.downloadClientPort.setEnabled(enabled);
        this.downloadClientIp.setEnabled(enabled);
        this.downloadAimPath.setEnabled(enabled);
        this.downloadFilePath.setEnabled(enabled);
        this.startDownloadButton.setEnabled(enabled);
    }

    private static void startDownload(String ip, int port, String aim, String path) {
        Client client = Client.getClient();
        try {
            client.connect(ip, port, new DownloadClientSolver(ip, port, aim, path));
        } catch (IOException e) {
            showError(e);
        }
    }

    private static void startDownloadServer(int port, int selectNum) {
        new Thread(() -> {
            Server server = Server.getNewServer("download", () -> new DownloadServerSolver(new ConnectionMessageImpl()));
            server.getInstance(port, selectNum);
            server.accept();
        }).start();
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
