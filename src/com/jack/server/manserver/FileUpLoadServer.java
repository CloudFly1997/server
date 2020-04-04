package com.jack.server.manserver;

import com.jack.server.util.PropertiesUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Jinkang He
 * @version 1.0
 * @date 2020/3/26 20:43
 */

public class FileUpLoadServer implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket =
                    new ServerSocket(Integer.parseInt(PropertiesUtil.getValue("client.file.upload_port")));
            System.out.println("文件上传服务端已打开");
            while (true) {
                Socket accept = serverSocket.accept();
                DataInputStream dis = new DataInputStream(accept.getInputStream());
                String name = dis.readUTF();
                System.out.println("上传请求"+name);
                File file = new File(PropertiesUtil.getValue("server.file.save.path") + name);
                if (!file.exists()) {
                    file.createNewFile();
                }
                //创建图片字节流
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len = 0;
                //往字节流里写图片数据
                while ((len = dis.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    fos.flush();
                }
                fos.close();
                dis.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
