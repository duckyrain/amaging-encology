package cn.amaging.encology.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by DuQiyu on 2018/11/15 9:34.
 */
public class BIOServer {

    private static Executor executor = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors());

    public void listen(int port) {
        ServerSocket server = null;
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(port), 128);
            server.setReuseAddress(true);
            System.out.println("Server is started!");
            while (true) {
                Socket socket = server.accept();
                executor.execute(new BIOServerHandler(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != server) {
                try {
                    server.close();
                    System.out.println("Server is closed.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
