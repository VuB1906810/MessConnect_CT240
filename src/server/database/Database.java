package server.database;

import java.sql.Connection;
import java.util.ArrayList;

public interface Database {
    public Connection getConnection();
    public ArrayList<String> getAllFile(String userSend, String userReceive);
    public ArrayList<String> getAllMessage(String userSend, String userReceive);
}
