/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.teamae.dbfiles;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnect {

    private static Connection conn;

    public static Connection getConn() {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String uid = prop.getProperty("username");
        String pwd = prop.getProperty("password");
        String host = prop.getProperty("host");
        String port = prop.getProperty("port");
        String DB_URL = "jdbc:mysql://" + host + ":" + port + "/delicacydb?user=" + uid + "&password=" + pwd;

//        String uid = "root";
//        String pwd = "";
//        String DB_URL = "jdbc:mysql://localhost:3306/delicacydb?user="+uid+"&password="+pwd;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connection established");
        } catch (ClassNotFoundException | SQLException e) {
            e.getStackTrace();
        }
        return conn;
    }
}
