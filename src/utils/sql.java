package utils;

import javafx.scene.control.Alert;

import java.io.*;
import java.sql.*;
import java.util.Properties;


public class sql {

    /**
     * Constructor reads the properties file for the database settings so that it can later create the connection.
     * @throws IOException
     */
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection conn;  // Connection Interface

    public static Connection getConn() {
        return conn;
    }

    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            conn = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }


}
