package cn.amaging.encology.aio;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DuQiyu on 2018/11/16 15:13.
 */
public class AIOServer {

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final Object lock = new Object();

    public void listen(int port) {
        AsynchronousServerSocketChannel serverSocketChannel = null;
        try {
            AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(executorService);
            serverSocketChannel = AsynchronousServerSocketChannel.open(group);
            serverSocketChannel.bind(new InetSocketAddress(port), 128);
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4096);
            System.out.println("AIO server is started.");
            // 等待连接
            serverSocketChannel.accept(null, new AIOServerHandler());
            synchronized (lock) {
                lock.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != serverSocketChannel) {
                    serverSocketChannel.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
