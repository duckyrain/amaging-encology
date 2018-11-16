package cn.amaging.encology.io;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by DuQiyu on 2018/11/15 9:34.
 */
public class BIOServerHandler implements Runnable {

    private Socket socket;

    public BIOServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 注意：由于TCP的全双工特性，如果没有提前约定，服务端并不知道客户端何时将数据发送完成
            // 这里只做简单的原理演示，所以只读取一行数据，实际的基于TCP协议的数据传输过程要比这个复杂得多
            String message = reader.readLine();
            System.out.println(message);
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(handle(message));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != writer) {
                    writer.close();
                }
                if (null != socket) {
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String handle(String message) {
        // handle client message here
        return "[Server] Hi, i'm server. i received your message:["+ message +"]";
    }
}
