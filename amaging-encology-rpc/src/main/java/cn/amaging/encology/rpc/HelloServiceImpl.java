package cn.amaging.encology.rpc;

import cn.amaging.encology.rpc.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by DuQiyu on 2018/11/6 11:13.
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        System.out.println("server message:" + name);
        return "hello," + name;
    }
}
