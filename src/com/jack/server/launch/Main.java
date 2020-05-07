package com.jack.server.launch;

import com.jack.server.main.FileDownLoadServer;
import com.jack.server.main.FileUpLoadServer;
import com.jack.server.main.LoginServer;

/**
 * @author Jinkang He
 * @version 1.0
 * @date 2020/3/26 0:25
 */

public class Main {
    public static void main(String[] args) {
        new Thread(new LoginServer()).start();
        new Thread(new FileUpLoadServer()).start();
        new Thread(new FileDownLoadServer()).start();
    }
}
