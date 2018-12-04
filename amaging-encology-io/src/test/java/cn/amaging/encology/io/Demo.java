package cn.amaging.encology.io;

import cn.amaging.encology.aio.AIOServer;
import cn.amaging.encology.bio.BIOClient;
import cn.amaging.encology.bio.BIOServer;
import cn.amaging.encology.netty.NettyClient;
import cn.amaging.encology.netty.NettyServer;
import cn.amaging.encology.nio.NIOClient;
import cn.amaging.encology.nio.NIOServer;
import org.junit.Test;

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
        try {
            new Thread(() -> new AIOServer().listen(9529)).start();
            Thread.sleep(1000L);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void NettyDemo() {
        try {
            new Thread(() -> new NettyServer().listen(9530)).start();
            Thread.sleep(1000L);
            new NettyClient("127.0.0.1", 9530).send("[Client] hello netty.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
