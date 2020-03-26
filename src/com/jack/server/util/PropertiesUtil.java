package com.jack.server.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Jinkang He
 * @version 1.0
 * @date 2020/3/26 20:45
 */

public class PropertiesUtil {


    private static Properties props;

    static {
        String fileName = "config.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        String value = "";
            if(props.containsKey(key)){
                value = props.getProperty(key);
            }

        return value;
    }
}
