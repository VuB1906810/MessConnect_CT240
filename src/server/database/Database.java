package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import server.database.MySQLDB;
import server.database.Postgre;

public class Database {

    private String DBMN;
    private String DB_URL;
    private String USER_NAME;
    private String PASSWORD;

    public Database(String DBMN, String DB_URL, String USER_NAME, String PASSWORD) {
        this.DBMN = DBMN;
        this.DB_URL = DB_URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }

    public Connection getConnection() {
        Connection conn = null;
        if (DBMN.equals("MySQLDB")) {
            MySQLDB Db = null;
            Db = new MySQLDB("jdbc:mysql://103.130.216.100:3306/quananc1_messconnect?connectTimeout=0&socketTimeout=0&autoReconnect=true", "quananc1_refooddb", "Lieutuanvu");
            conn = Db.getConnection();
        } else if (DBMN.equals("Postgre")) {
            Postgre Db = null;
            Db = new Postgre("jdbc:mysql://103.130.216.100:3306/quananc1_messconnect?connectTimeout=0&socketTimeout=0&autoReconnect=true", "quananc1_refooddb", "Lieutuanvu");
            conn = Db.getConnection();
        }
        return conn;
    }
}
