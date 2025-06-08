package com.inventory.Database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Class to retrieve connection for database and login verification.
 */
public class ConnectionFactory {

    static final String driver = "com.mysql.cj.jdbc.Driver";
    static final String url = "jdbc:mysql://localhost:3306/inventory";
    static String username;
    static String password;

    Properties prop;
    Connection conn = null;
    Statement statement = null;
    ResultSet resultSet = null;

    public ConnectionFactory() {
        try {
            prop = new Properties();
            prop.loadFromXML(new FileInputStream("lib/DBCredentials.xml"));
            username = prop.getProperty("username");
            password = prop.getProperty("password");

            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            statement = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Check login credentials.
     * For ADMINISTRATOR: use username/password check.
     * For EMPLOYEE: check if the user exists; OTP is verified separately in UI.
     */
    public boolean checkLogin(String username, String password, String userType) {
        try {
            if (userType.equalsIgnoreCase("ADMINISTRATOR")) {
                // Traditional login
                String query = "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "' AND usertype='ADMINISTRATOR' LIMIT 1";
                resultSet = statement.executeQuery(query);
                if (resultSet.next()) return true;
            } else if (userType.equalsIgnoreCase("EMPLOYEE")) {
                // EMPLOYEE: only check existence, OTP will be verified separately
                String query = "SELECT * FROM users WHERE username='" + username + "' AND usertype='EMPLOYEE' LIMIT 1";
                resultSet = statement.executeQuery(query);
                if (resultSet.next()) return true; // found valid employee
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
