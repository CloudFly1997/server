package com.jack.server.manserver;

import com.jack.server.common.ClientObj;
import com.jack.transfer.LoginRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Jinkang He
 * @version 1.0
 * @date 2020/3/26 20:43
 */

public class FileServer implements Runnable {
    @Override
    public void run() {
        /*try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
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
        }*/
    }
}
