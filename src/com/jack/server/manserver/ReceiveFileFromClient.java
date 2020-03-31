package com.jack.server.manserver;

import java.io.*;
import java.net.Socket;

/**
 * @author Jinkang He
 * @version 1.0
 * @date 2020/3/26 19:38
 */

public class ReceiveFileFromClient implements Runnable {
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String id;
    private Socket socket;

    public ReceiveFileFromClient(String id, Socket socket) throws IOException {
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.id = id;
        this.socket = socket;
    }

    public ReceiveFileFromClient() {
    }

    @Override
    public void run() {
        try {
            File file = new File("d:/test.png");
            if (!file.exists()) {
                file.createNewFile();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
            byte[] buf = new byte[1027 * 9];
            int len = 0;
            while ((len = dataInputStream.read(buf)) != -1) {
                dos.write(buf, 0, len);
            }
            dos.flush();
            socket.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
