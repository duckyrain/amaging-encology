package cn.amaging.encology.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * Created by DuQiyu on 2018/11/16 10:04.
 */
public class NIOServer {

    public void listen(int port) {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(port), 128);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("NIO server is started.");
            // 与BIO的区别是1个线程即可批量处理多个socket连接，即不会因为线程资源限制导致新的socket请求无法连接，但IO过程仍然是同步阻塞
            while (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    new NIOServerHandler().handleKey(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != serverSocketChannel) {
                    serverSocketChannel.close();
                }
                if (null != selector) {
                    selector.close();
                }
                System.out.println("NIO server is stopped.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
