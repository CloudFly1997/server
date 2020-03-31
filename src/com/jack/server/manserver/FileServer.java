package com.jack.server.manserver;

import com.jack.server.common.ClientObj;
import com.jack.server.util.PropertiesUtil;
import com.jack.transfer.LoginRequest;

import java.io.*;
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
            Socket accept = serverSocket.accept();
            System.out.println("文件来了");

            InputStream in = accept.getInputStream();
            //创建图片字节流
            FileOutputStream fos = new FileOutputStream("server.jpg");
            byte[] buf = new byte[1024];
            int len = 0;
            //往字节流里写图片数据
            while ((len = in.read(buf)) != -1)
            {
                fos.write(buf,0,len);
            }
            fos.close();
            in.close();


            /*File file = new File("d:/test.png");
            if (!file.exists()) {
                file.createNewFile();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
            DataInputStream dis = new DataInputStream(accept.getInputStream());
            byte[] buf = new byte[1027 * 9];
            int len = 0;
            while ((len = dis.read(buf)) != -1) {
                dos.write(buf, 0, len);
            }
            dos.flush();
            accept.close();*/


//                new Thread(new ReceiveFileFromClient("1",accept)).start();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
