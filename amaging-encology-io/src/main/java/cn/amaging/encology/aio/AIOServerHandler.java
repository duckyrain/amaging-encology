package cn.amaging.encology.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * Created by DuQiyu on 2018/11/16 15:35.
 */
public class AIOServerHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

    private ByteBuffer readBuffer = ByteBuffer.allocate(4096);

    private ByteBuffer writeBuffer = ByteBuffer.allocate(4096);

    @Override
    public void completed(AsynchronousSocketChannel result, Object attachment) {
        try {
            readBuffer.clear();
            result.read(readBuffer);
            readBuffer.flip();
            String message = new String(readBuffer.array(), StandardCharsets.UTF_8);
            System.out.println(message);
            writeBuffer.clear();
            writeBuffer.put(handle(message).getBytes());
            writeBuffer.flip();
            result.write(writeBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != result) {
                    result.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        exc.printStackTrace();
    }

    private String handle(String message) {
        // handle client message here
        return "[Server] Hi, i'm server. i received your message.";
    }
}
