package cn.amaging.encology.rpc;


import java.lang.reflect.Proxy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by DuQiyu on 2018/11/6 10:28.
 */
public class RPCClient<T> {

    public static <T> T getRemoteProxyObj(final Class<?> serviceInterface, final InetSocketAddress addr) {
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface},
                (proxy, method, args) -> {
                    Socket socket = null;
                    ObjectOutputStream outputStream = null;
                    ObjectInputStream inputStream = null;
                    try {
                        socket = new Socket();
                        socket.connect(addr);

                        outputStream = new ObjectOutputStream(socket.getOutputStream());
                        outputStream.writeUTF(serviceInterface.getName());
                        outputStream.writeUTF(method.getName());
                        outputStream.writeObject(method.getParameterTypes());
                        outputStream.writeObject(args);

                        inputStream = new ObjectInputStream(socket.getInputStream());
                        return inputStream.readObject();
                    } finally {
                        if (null != outputStream) {
                            outputStream.close();
                        }
                        if (null != inputStream) {
                            inputStream.close();
                        }
                        if (null != socket) {
                            socket.close();
                        }
                    }
                });
    }
}
