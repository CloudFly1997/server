package com.jack.server.launch;

import com.jack.server.manserver.FileServer;
import com.jack.server.manserver.LoginServer;

/**
 * @author Jinkang He
 * @version 1.0
 * @date 2020/3/26 0:25
 */

public class Main {
    public static void main(String[] args) {
        new Thread(new LoginServer()).start();
        new Thread(new FileServer()).start();
    }
}
