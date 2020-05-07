package com.jack.server.main;

import com.jack.server.util.PropertiesUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Jinkang He
 * @version 1.0
 * @date 2020/3/26 20:43
 */

public class FileDownLoadServer implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket =
                    new ServerSocket(Integer.parseInt(PropertiesUtil.getValue("client.file.download_port")));
            System.out.println("文件下载服务端已打开");
            while (true) {
                Socket accept = serverSocket.accept();
                System.out.println(accept+"下载请求");
                DataInputStream dis = new DataInputStream(accept.getInputStream());
                String name = dis.readUTF();
                System.out.println("下载请求"+name);
                //根据文件名在服务器存储文件中找到文件
                File file = new File(PropertiesUtil.getValue("server.file.save.path") + name);
                //创建图片字节流
                DataInputStream fileInputStream = new DataInputStream(new FileInputStream(file));
                DataOutputStream dos = new DataOutputStream(accept.getOutputStream());
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = fileInputStream.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                accept.shutdownOutput();
                dos.close();
                dis.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
