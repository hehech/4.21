package com.start;


import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    @Test
    public void testGen(){
        String url = "jdbc:oracle:thin:@123.56.178.104:1521:orcl";
        String username = "worker";
        String password = "1z2y3x4w";

        try {
            // 加载 Oracle 驱动
            Class.forName("oracle.jdbc.OracleDriver");

            // 建立连接
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接成功！");
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC 驱动未找到！");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("数据库连接失败！错误信息：" + e.getMessage());
            e.printStackTrace();
        }

    }

}
