package cn.amaging.encology.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by DuQiyu on 2018/11/6 14:07.
 */
public class Consumer {

    public static void main(String[] args) {
        HelloService service = RPCClient.getRemoteProxyObj(HelloService.class, new InetSocketAddress("127.0.0.1", 9527));
        System.out.println("client received message:" + service.sayHello("amaging"));
    }
}
