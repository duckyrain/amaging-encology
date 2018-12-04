package cn.amaging.encology.bio;


import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by DuQiyu on 2018/11/15 9:33.
 */
public class BIOClient {

    private String host;

    private int port;

    public BIOClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void send(String message) {
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            socket = new Socket(host, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 注意：由于TCP的全双工特性，如果没有提前约定，客户端并不知道服务端何时将数据发送完成
            // 这里只做简单的原理演示，所以只读取一行数据，实际的基于TCP协议的数据传输过程要比这个复杂得多
            System.out.println(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != writer) {
                    writer.close();
                }
                if (null != reader) {
                    reader.close();
                }
                if (null != socket) {
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
