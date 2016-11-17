package cn.lb.java;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by LiuBo on 2016-11-11.
 */

public class JavaServer {

    public static void main(String[] args) throws Exception{

        ServerSocket serverSocket;
        SocketAddress address;
        address = new InetSocketAddress("192.168.0.103", 9090);

        serverSocket = new ServerSocket();

        serverSocket.bind(address);
        serverSocket.setReceiveBufferSize(1024*1024);
        serverSocket.setSoTimeout(3000);
        Socket socket = serverSocket.accept();

        socket.getOutputStream();
        socket.getInputStream();

    }

}
