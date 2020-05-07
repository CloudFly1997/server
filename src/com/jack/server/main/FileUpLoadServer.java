package com.jack.server.main;

import com.jack.server.task.FileUpLoadTask;
import com.jack.server.util.PropertiesUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Jinkang He
 * @version 1.0
 * @date 2020/3/26 20:43
 */

public class FileUpLoadServer implements Runnable {
    @Override
    public void run() {

        try {
            ExecutorService executor = new ThreadPoolExecutor(10, 10,
                    60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue(10));
            ServerSocket serverSocket =
                    new ServerSocket(Integer.parseInt(PropertiesUtil.getValue("client.file.upload_port")));
            System.out.println("文件上传服务端已打开");
            while (true) {
                Socket accept = serverSocket.accept();
                executor.submit(new FileUpLoadTask(accept));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
