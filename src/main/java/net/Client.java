package net;

import net.server.AbstractServer;
import net.tool.connectionManager.ConnectionManager;
import net.tool.connectionSolver.ConnectionSolver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by xlo on 2015/11/3.
 * it's the client
 */
public class Client extends Thread {
    protected SocketChannel socketChannel;
    protected Selector selector;

    private static Client client;

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
        this.socketChannel = SocketChannel.open();
        this.socketChannel.configureBlocking(false);
        this.socketChannel.connect(new InetSocketAddress(host, port));

        downloadClientSolver.getConnectionMessage().setSocket(this.socketChannel);

        ConnectionManager.getSolverManager().putSolver(this.socketChannel.socket(), downloadClientSolver);
        downloadClientSolver.getConnectionMessage().setSelectionKey(this.socketChannel.register(this.selector, SelectionKey.OP_CONNECT));
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
