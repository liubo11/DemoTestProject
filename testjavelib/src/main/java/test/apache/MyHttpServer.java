package test.apache;


import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by LiuBo on 2016-11-21.
 */

public class MyHttpServer {
    public static void start() throws IOException {
        //启动服务，监听来自客户端的请求
        Context.load();

        HttpServerProvider provider = HttpServerProvider.provider();
        HttpServer httpserver = provider.createHttpServer(new InetSocketAddress(9090), 10);//监听端口9090,能同时接 受10个请求
        httpserver.createContext(Context.contextPath, new MyHttpHandler());
        httpserver.setExecutor(null);
        httpserver.start();
        System.out.println("server started");
    }


    public static void main(String[] args) throws IOException {
        start();
    }


}
