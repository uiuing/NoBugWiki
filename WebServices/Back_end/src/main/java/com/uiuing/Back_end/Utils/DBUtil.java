package com.uiuing.Back_end.Utils;

import java.sql.*;

public class DBUtil {
    /**
     * 用于链接数据库，得到的结果是数据库的连接对象，链接对象具备了操作数据库的很多功能。=
     * 
     * @return 链接对象
     */
    public static Connection getConn() {
        // 1. 加载数据库驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 2. 获取数据库链接
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://43.129.74.180:5743/nobug?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false", "NoBug",
                    "XT2XF5naJmWPM56e");
                    
            // 3. 将链接返回给工具的使用者
            return conn;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * 断开数据库资源的链接，用于释放资源
     * 
     * @param conn      要释放链接
     * @param statement 要释放的执行环境
     * @param resultSet 要释放的结果集
     */
    public static void close(Connection conn, Statement statement, ResultSet resultSet) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}
