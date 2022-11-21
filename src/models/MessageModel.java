package models;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import server.database.MySQLDB;

public class MessageModel {

    public Connection conn;
    private Statement statement;

    public MessageModel(Connection conn) {
        this.conn = conn;
        try {
            statement = conn.createStatement();
            statement.setQueryTimeout(0);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
//	Get on user from username

    public ArrayList<String> getAllMessage(String userSend, String userReceive) {
        ResultSet rs;
        try {
            rs = statement.executeQuery("select * from messages where "
                    + "(user_send='" + userSend + "' and user_receive='" + userReceive + "') or"
                    + "(user_send='" + userReceive + "' and user_receive='" + userSend + "') order by 5 asc");
            return parseToString(userSend, userReceive, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> parseToString(String userSend, String userReceive, ResultSet rs) {
        ArrayList<String> list = new ArrayList<String>();
        String message;
        try {
            while (rs.next()) {
                if (rs.getString("user_send").equals(userSend)) {
                    message = "A:";
                } else {
                    message = "M:@" + userReceive + ":";
                }
                message += rs.getString("body");
                list.add(new String(message));

            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertMessage(String userSend, String userReceive, String message) {
        try {
            String sql = "INSERT INTO `messages` (`id`, `user_send`, `user_receive`, `body`, `date`) "
                    + "VALUES (NULL, '" + userSend + "', '" + userReceive + "', '" + message + "', current_timestamp())";
            int t = statement.executeUpdate(sql);
            return t == 1;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }

    public ArrayList<String> getAllMessage(String currentUser) {
        ResultSet rs;
        try {
            rs = statement.executeQuery("select * from messages where user_receive='all' or user_send='all' order by 5 asc");
            return parseToString(currentUser, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> parseToString(String userSend, ResultSet rs) {
        ArrayList<String> list = new ArrayList<String>();
        String message;
        try {
            while (rs.next()) {
                if (rs.getString("user_send").equals(userSend)) {
                    message = "A:";
                } else {
                    message = "M:@" + rs.getString("user_send") + ":";
                }
                message += rs.getString("body");
                list.add(new String(message));

            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
