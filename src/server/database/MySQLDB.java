package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLDB {

    private static String DB_URL = "jdbc:mysql://103.130.216.100:3306/quananc1_messconnect";
    private static String USER_NAME = "quananc1_refooddb";
    private static String PASSWORD = "Lieutuanvu";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);

            System.out.println("Connect to DB successfully!");
        } catch (Exception ex) {
            System.out.println("Connect to DB failure!");
            ex.printStackTrace();
        }
        return conn;
    }
}
