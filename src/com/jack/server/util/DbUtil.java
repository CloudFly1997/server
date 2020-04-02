package com.jack.server.util;

import java.sql.*;

/**
 * @author jack
 */
public class DbUtil {
	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName(PropertiesUtil.getValue("mysql.classname"));
			connection = DriverManager.getConnection(PropertiesUtil.getValue("mysql.url"),
					PropertiesUtil.getValue("mysql.username"),
					PropertiesUtil.getValue("mysql.password"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void close(Connection conn, ResultSet rs, PreparedStatement ps) {
		try {
			if(rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
