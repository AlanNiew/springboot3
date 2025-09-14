package com.example.proxy.mybaits.factory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @Author: Niu
 * @Date: 2025/5/22 16:17
 * @Description:
 */
public class DataBaseConnectionFactory {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    // 数据库连接
    private static volatile Connection connection;

    // 获取数据库连接
    public static Connection getConnection() {
        if (connection == null) {
            synchronized (MySqlQueryFactory.class) {
                if (connection == null) {
                    try {
                        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return connection;
    }

    // 关闭数据库连接
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
