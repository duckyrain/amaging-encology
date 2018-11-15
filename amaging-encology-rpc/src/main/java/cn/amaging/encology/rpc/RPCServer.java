package cn.amaging.encology.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DuQiyu on 2018/11/6 11:16.
 */
public class RPCServer {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private static final Map<String, Class<?>> registry = new HashMap<>();

    private boolean isRunning = false;

    private int port = 80;

    public RPCServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1", port));
        try {
            isRunning = true;
            System.out.println("服务启动，IP：127.0.0.1, 端口：" + port);
            while (true) {
                executorService.execute(new ServiceTask(serverSocket.accept()));
            }
        } finally {
            serverSocket.close();
            isRunning = false;
        }
    }

    public void stop() {
        if (isRunning) {
            executorService.shutdown();
            isRunning = false;
        }
    }

    public void register(Class<?> serviceInterface, Class<?> impl) {
        registry.put(serviceInterface.getName(), impl);
    }

    private static class ServiceTask implements Runnable {

        Socket client;

        ServiceTask(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;
            try {
                input = new ObjectInputStream(client.getInputStream());
                String serviceName = input.readUTF();
                String methodName = input.readUTF();
                Class<?>[] parameterTypes = (Class<?>[])input.readObject();
                Object[] args = (Object[])input.readObject();
                Class<?> serviceClass = registry.get(serviceName);
                if (null == serviceClass) {
                    throw new ClassNotFoundException(serviceName + " not found.");
                }
                Method method = serviceClass.getMethod(methodName, parameterTypes);
                Object result = method.invoke(serviceClass.newInstance(), args);

                output = new ObjectOutputStream(client.getOutputStream());
                output.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != output) {
                        output.close();
                    }
                    if (null != input) {
                        input.close();
                    }
                    if (null != client) {
                        client.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
