package com.jack.server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author jack
 */
public class DbUtil {
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(PropertiesUtil.getValue("mysql.classname"));
            connection = DriverManager.getConnection(PropertiesUtil.getValue("mysql.url")
                    , PropertiesUtil.getValue("mysql.username")
                    , PropertiesUtil.getValue("mysql.password"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
