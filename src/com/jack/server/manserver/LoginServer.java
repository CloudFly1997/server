package com.jack.server.manserver;

import com.jack.server.common.ClientMap;
import com.jack.server.common.ClientObj;
import com.jack.server.util.PropertiesUtil;
import com.jack.transfer.LoginRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;

/**
 * @author Jinkang He
 * @version 1.0
 * @date 2020/3/26 0:24
 */

public class LoginServer implements Runnable {
    ServerSocket serverSocket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Map<String, ClientObj> clientMap = ClientMap.getClientMap();

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Integer.parseInt(PropertiesUtil.getValue("client.login.port")));
            System.out.println("服务端已打开");
            while (true) {
                Socket acceptsSocket = serverSocket.accept();
                ois = new ObjectInputStream(acceptsSocket.getInputStream());
                oos = new ObjectOutputStream(acceptsSocket.getOutputStream());
                LoginRequest loginRequest = (LoginRequest) ois.readObject();
                ClientObj client = new ClientObj(acceptsSocket,ois,oos);
                System.out.println(loginRequest.getUser() + "连接成功");
                clientMap.put(loginRequest.getUser(), client);
                System.out.println(clientMap.entrySet());
                new ReceiveMessageFromClient(acceptsSocket, ois, oos, loginRequest.getUser()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
