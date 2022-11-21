package models;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import server.database.MySQLDB;

public class UserModel {

    public Connection conn;
    private Statement statement;

    public UserModel(Connection conn) {
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

    public ResultSet getOne(String username) {
        ResultSet rs;
        try {
            rs = statement.executeQuery("select * from users where username='" + username + "'");

            if (rs.next()) {
                return rs;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//	Verify Login
    public boolean verifyLogin(String username, String password) {
        try {
            ResultSet rs = getOne(username);
            if (rs == null) {
                return false;
            }
            String MD5pass = md5Hash(password);
            return MD5pass.equals(rs.getString("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean handleSignup(String username, String password) {
        try {
            String MD5pass = md5Hash(password);
            int t = statement.executeUpdate("insert into users "
                    + "values ('" + username + "', '" + MD5pass + "')");
            System.out.println("So dong bi tac dong: " + t);
            return t == 1;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }

    public boolean checkExistUsername(String username) {
        return getOne(username) != null;
    }

//	Create md5 hash from a string 
    public String md5Hash(String message) {
        String md5 = "";
        if (null == message) {
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(message.getBytes(), 0, message.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }
}
