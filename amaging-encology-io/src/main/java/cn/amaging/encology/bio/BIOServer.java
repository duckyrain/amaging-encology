package cn.amaging.encology.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
            System.out.println("BIO server is started.");
            while (true) {
                Socket socket = server.accept();
                // 每个socket都需要启一个线程去处理，造成了资源浪费
                executor.execute(new BIOServerHandler(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != server) {
                try {
                    server.close();
                    System.out.println("BIO server is stopped.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
