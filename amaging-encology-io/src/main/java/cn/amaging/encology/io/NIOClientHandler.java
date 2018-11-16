package cn.amaging.encology.io;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Created by DuQiyu on 2018/11/16 11:01.
 */
public class NIOClientHandler {

    private ByteBuffer readBuffer = ByteBuffer.allocate(4096);

    private ByteBuffer writeBuffer = ByteBuffer.allocate(4096);

    public void handleKey(SelectionKey key, String message) {
        if (!key.isValid()) {
            throw new RuntimeException("selectionKey is invalid.");
        }
        try {
            if (key.isConnectable()) {
                SocketChannel socketChannel = (SocketChannel)key.channel();
                if (socketChannel.isConnectionPending()) {
                    socketChannel.finishConnect();
                }
                System.out.println("server is connected.");
                socketChannel.register(key.selector(), SelectionKey.OP_READ);
                writeBuffer.clear();
                writeBuffer.put(message.getBytes());
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
            }
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel)key.channel();
                readBuffer.clear();
                socketChannel.read(readBuffer);
                readBuffer.flip();
                System.out.println(new String(readBuffer.array(), StandardCharsets.UTF_8));
                // 注意：这里只是演示需要，读取完成后，关闭socket通道
                key.cancel();
                socketChannel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            key.cancel();
            try {
                key.channel().close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
