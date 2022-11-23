package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import server.database.MySQLDB;

public class FileModel {

    public Connection conn;
    private Statement statement;
    private String fileID;
    private String fileName;

    public FileModel(Connection conn) {
        this.conn = conn;
        try {
            statement = conn.createStatement();
            statement.setQueryTimeout(0);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public FileModel(String fileID, String fileName) {
        this.fileID = fileID;
        this.fileName = fileName;
    }
//	Get on user from username

    public String getFileID() {
        return fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public ArrayList<String> getAllFile(String userSend, String userReceive) {
        ResultSet rs;
        try {
            rs = statement.executeQuery("select * from files where "
                    + "(user_send='" + userSend + "' and user_receive='" + userReceive + "') or"
                    + "(user_send='" + userReceive + "' and user_receive='" + userSend + "')"
                    + "order by date desc");
            return parseToString(userSend, userReceive, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> parseToString(String userSend, String userReceive, ResultSet rs) {
        ArrayList<String> list = new ArrayList<String>();
        String file;
        try {
            while (rs.next()) {
                if (rs.getString("user_send").equals(userSend)) {
                    file = "I:@" + userSend + ":";
                } else {
                    file = "I:@" + userReceive + ":";
                }
//                file = "I:@" + userSend + ":";
                file += "FileId: " + rs.getString("file_id") + ", ";
                file += "FileName: " + rs.getString("file_name");
                list.add(new String(file));

            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertFile(String userSend, String userReceive, String fileid, String fileName) {
        try {
            String sql = "INSERT INTO `files` (`user_send`, `user_receive`, `file_id`, `file_name`, `date`) "
                    + "VALUES ('" + userSend + "', '" + userReceive + "', '" + fileid + "', '" + fileName + "', current_timestamp())";
            int t = statement.executeUpdate(sql);
            return t == 1;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }

    public ArrayList<String> getAllFile(String currentUser) {
        ResultSet rs;
        try {
            rs = statement.executeQuery("select * from files where user_receive='all' or user_send='all' order by date desc");
            return parseToString(currentUser, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> parseToString(String userSend, ResultSet rs) {
        ArrayList<String> list = new ArrayList<String>();
        String file;
        try {
            while (rs.next()) {
//                if (rs.getString("user_send").equals(userSend)) {
//                    file = "E:";
//                } else {
//                    file = "I:@" + rs.getString("user_send") + ":";
//                }
                file = "I:@" + rs.getString("user_send") + ":";
                file += "FileId: " + rs.getString("file_id") + ", ";
                file += "FileName: " + rs.getString("file_name");
                list.add(new String(file));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
