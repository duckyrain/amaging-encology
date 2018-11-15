package cn.amaging.encology.rpc;

/**
 * Created by DuQiyu on 2018/11/6 14:21.
 */
public class Provider {

    public static void main(String[] args) {
        try {
            RPCServer rpcServer = new RPCServer(9527);
            rpcServer.register(HelloService.class, HelloServiceImpl.class);
            rpcServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
