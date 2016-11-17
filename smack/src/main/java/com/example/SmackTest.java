package com.example;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

public class SmackTest {

    private static String host = "cn.lb.test";

    private static AbstractXMPPConnection connection1;

    public static void main(String[] args) throws Exception {

        System.out.println("start talk:");

// Create a connection to the jabber.org server.
        /*AbstractXMPPConnection conn1 = new XMPPTCPConnection("lb1", "123456", "cn.ln.test");
        conn1.connect();*/

// Create a connection to the jabber.org server on a specific port.
        /*XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("lb1", "123456")
                .setServiceName("cn.lb.test")
                .setHost("cn.lb.test")
                .setPort(5222)
                .build();

        AbstractXMPPConnection conn2 = new XMPPTCPConnection(config);
        conn2.connect();*/

        login1();

        // 发送消息
        //Assume we've created an XMPPConnection name "conn"
        ChatManager chatmanager = ChatManager.getInstanceFor(connection1);
        Chat newChat = chatmanager.createChat("lb2@cn.lb.test");
        for (int i = 0; i < 10; i++) {
            newChat.sendMessage("counter is " + i);
            Thread.sleep(2000);
        }
        System.out.println("send message!");
        while (true);
    }


    public static void login1() throws Exception {
        //demo
        AbstractXMPPConnection connection = buildConnection(host, 5222);
        connection.connect();
        connection.login("lb1", "123456");
        connection1 = connection;
    }

    private static AbstractXMPPConnection buildConnection(String host, int port) {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        XMPPTCPConnectionConfiguration config = builder
                .setServiceName(host)
                .setHost(host).setPort(port)
                .build();
        return new XMPPTCPConnection(config);
    }
}
