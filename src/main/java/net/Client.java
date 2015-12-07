package net;

import net.server.AbstractServer;
import net.tool.connectionManager.ConnectionManager;
import net.tool.connectionSolver.ConnectionSolver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xlo on 2015/11/3.
 * it's the client
 */
public class Client extends Thread {
    protected volatile Selector selector;

    private static volatile Client client;

    private final List<SocketChannel> next = new LinkedList<>();
    private final List<AbstractServer> abstractServers = new LinkedList<>();

    public static Client getClient() {
        if (client == null) {
            synchronized (Client.class) {
                if (client == null) {
                    client = new Client();
                }
            }
        }
        return client;
    }

    public void startSelector() throws IOException {
        this.selector = Selector.open();
    }

    public void connect(String host, int port, AbstractServer downloadClientSolver) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(host, port));

        downloadClientSolver.getConnectionMessage().setSocket(socketChannel);

        ConnectionManager.getSolverManager().putSolver(socketChannel.socket(), downloadClientSolver);
        synchronized (next) {
            next.add(socketChannel);
            abstractServers.add(downloadClientSolver);
        }
        this.selector.wakeup();
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                this.selector.select();
                for (SelectionKey selectionKey : this.selector.keys()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ConnectionSolver connectionSolver = ConnectionManager.getSolverManager().getSolver(socketChannel.socket());
                    connectionSolver.run();
                }
                this.selector.selectedKeys().clear();
                synchronized (next) {
                    for (int i=0;i<next.size();i++) {
                        SocketChannel socketChannel = next.get(i);
                        AbstractServer abstractServer = abstractServers.get(i);
                        abstractServer.getConnectionMessage().setSelectionKey(socketChannel.register(this.selector, SelectionKey.OP_CONNECT));
                    }
                    next.clear();
                    abstractServers.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
