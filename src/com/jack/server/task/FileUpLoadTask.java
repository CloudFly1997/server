package com.jack.server.task;

import com.jack.server.util.PropertiesUtil;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Jinkang He
 * @version 1.0
 * @date 2020/5/5 22:13
 */

public class FileUpLoadTask implements Runnable {

    Socket accept;

    public FileUpLoadTask(Socket accept) {
        this.accept = accept;
    }

    @Override
    public void run() {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
