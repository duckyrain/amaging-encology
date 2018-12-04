package cn.amaging.encology.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by DuQiyu on 2018/11/16 15:14.
 */
public class AIOClient implements CompletionHandler<Void, Object> {

    private static ByteBuffer writeBuffer = ByteBuffer.allocate(4096);

    private String host;

    private int port;

    public AIOClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void send(String message) {
        AsynchronousSocketChannel socketChannel = null;
        try {
            socketChannel = AsynchronousSocketChannel.open();
            socketChannel.connect(new InetSocketAddress(host, port));
            writeBuffer.clear();
            writeBuffer.put(message.getBytes());
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != socketChannel) {
                    socketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void completed(Void result, Object attachment) {
        System.out.println("connected to server.");
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        System.out.println("failed to connect server.");
        exc.printStackTrace();
    }
}
