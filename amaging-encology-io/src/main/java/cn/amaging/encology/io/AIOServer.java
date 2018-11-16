package cn.amaging.encology.io;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DuQiyu on 2018/11/16 15:13.
 */
public class AIOServer {

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void listen(int port) {
        AsynchronousChannelGroup group = null;
        AsynchronousServerSocketChannel serverSocketChannel = null;
        try {
            group = AsynchronousChannelGroup.withThreadPool(executorService);
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port), 128);
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4096);
            System.out.println("AIO server is started.");
            // 等待连接
            serverSocketChannel.accept(null, new AIOServerHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
