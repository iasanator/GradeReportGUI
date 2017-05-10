package com.company;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by iassona on 4/21/2017.
 */
public class DatabaseConnector {

    private static Connection con;
    private static final String connectionUrl = "jdbc:sqlserver://golem.csse.rose-hulman.edu:1433;" +
            "databaseName=GradeReport_Data;user=GRuser;password=abc123;";

    public static MessageDigest md;

    public DatabaseConnector() {

        try{
            DatabaseConnector.md = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

    }

    private static void connect() {
        try {
            con = DriverManager.getConnection(connectionUrl);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean authenticateLogin(String username, String password, boolean isStudent, LoginDialog logindlg) {

        boolean loggedIn = false;

        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }

        String modifier;
        if (isStudent) {
            modifier = "_Student";
        } else {
            modifier = "_Teacher";
        }

        connect();

        try {
            String SQL = "DECLARE @validated INT;" +
                    "EXEC LoginCheck" + modifier + " " +
                    "@Username = ?, " +
                    "@HashPass = ?, " +
                    "@result = @validated OUTPUT;" +
                    "SELECT Validated = @validated;";

            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, username);
            pstmt.setString(2, hash(password));
            ResultSet rs = pstmt.executeQuery();
            rs.next();

            int output = rs.getInt("Validated");

            if (output == 0) {
                loggedIn = false;
            } else {
                loggedIn = true;
                logindlg.userID = output;
                Main.userID = output;
            }

            rs.close();
            pstmt.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        disconnect();

        return loggedIn;
    }


    public static String hash(String input){
        try{
            md.update(input.getBytes("UTF-8"));
            return(String.format("%064x", new java.math.BigInteger(1, md.digest())));
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        return null;
    }

    public static Connection getConnection(){
        connect();
        return con;
    }
}
