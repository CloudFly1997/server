package com.jack.server.manserver;

import com.jack.server.common.ClientObj;
import com.jack.server.util.PropertiesUtil;
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
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(PropertiesUtil.getValue("client.file.port")));
            System.out.println("文件服务端已打开");
            while (true) {
                Socket accept = serverSocket.accept();
                new Thread(new ReceiveFileFromClient("1",accept)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
