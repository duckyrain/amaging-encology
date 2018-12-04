package cn.amaging.encology.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Created by DuQiyu on 2018/11/16 10:04.
 */
public class NIOServerHandler {

    private ByteBuffer readBuffer = ByteBuffer.allocate(4096);

    private ByteBuffer writeBuffer = ByteBuffer.allocate(4096);

    public void handleKey(SelectionKey key) {
        if (!key.isValid()) {
            throw new RuntimeException("selectionKey is invalid.");
        }
        try {
            // 处理新的连接请求
            if (key.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
                // 完成握手，正式建立连接
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                // 注册读操作
                socketChannel.register(key.selector(), SelectionKey.OP_READ);
            }
            // 数据读
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel)key.channel();
                readBuffer.clear();
                int readBytes = socketChannel.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    String message = new String(readBuffer.array(), StandardCharsets.UTF_8);
                    System.out.println(message);
                    String result = handle(message);
                    writeBuffer.clear();
                    writeBuffer.put(result.getBytes());
                    writeBuffer.flip();
                    socketChannel.write(writeBuffer);
                } else {
                    // 注意，这里只是为了演示需要，未读取到数据不一定需要关闭当前连接。
                    key.cancel();
                    socketChannel.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            key.cancel();
            try {
                key.channel().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private String handle(String message) {
        // handle client message here
        return "[Server] Hi, i'm server. i received your message.";
    }
}
