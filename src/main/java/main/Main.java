package main;

import net.MethodSolver;
import net.server.Server;

/**
 * Created by xlo on 15-6-21.
 * it's the main
 */
public class Main {

    public static void main(String[] args) {
//        new Thread() {
//            @Override
//            public void run() {
//                startProxy(8000);
//            }
//        }.start();
        new Thread() {
            @Override
            public void run() {
                startServer(8080);
            }
        }.start();
    }

    private static void startServer(int port) {
        Server server = Server.getNewServer(MethodSolver::new);
        server.getInstance(port, 5);
        server.accept();
    }

    private static void startProxy(int port) {
        Server server = Server.getNewServer();
        server.getInstance(port, 5);
        server.accept();
    }
}
