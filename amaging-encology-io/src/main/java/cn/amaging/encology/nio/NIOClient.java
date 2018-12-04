package cn.amaging.encology.nio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by DuQiyu on 2018/11/16 10:04.
 */
public class NIOClient {

    private String host;

    private int port;

    public NIOClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void send(String message) {
        Selector selector = null;
        SocketChannel socketChannel = null;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(host, port));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            while(selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    new NIOClientHandler().handleKey(key, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != socketChannel) {
                    socketChannel.close();
                }
                if (null != selector) {
                    selector.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
