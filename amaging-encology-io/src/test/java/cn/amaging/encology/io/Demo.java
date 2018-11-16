package cn.amaging.encology.io;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by DuQiyu on 2018/11/15 14:33.
 */
public class Demo {

    @Test
    public void BIODemo() {
        try {
            new Thread(() -> new BIOServer().listen(9527)).start();
            Thread.sleep(1000L);
            for (int i = 0; i < 10; ++i) {
                new BIOClient("127.0.0.1", 9527).send("[Client] hello bio" + i);
                Thread.sleep(500L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void NIODemo() {
        try {
            new Thread(() -> new NIOServer().listen(9528)).start();
            Thread.sleep(1000L);
            new NIOClient("127.0.0.1", 9528).send("[Client] hello nio");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void AIODemo() {

    }

    @Test
    public void NettyDemo() {

    }
}
