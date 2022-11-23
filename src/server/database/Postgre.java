package server.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Postgre {

    private static String DB_URL = "jdbc:mysql://103.130.216.100:3306/quananc1_messconnect?connectTimeout=0&socketTimeout=0&autoReconnect=true";
    private static String USER_NAME = "quananc1_refooddb";
    private static String PASSWORD = "Lieutuanvu";

    public Postgre(String DB_URL, String USER_NAME, String PASSWORD) {
        this.DB_URL = DB_URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }

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
