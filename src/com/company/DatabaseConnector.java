package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.security.MessageDigest;

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

    public static boolean authenticateLogin(String username, String password) {

        boolean loggedIn = false;

        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }

        connect();

        try {
            String SQL = "DECLARE @validated BIT;" +
                    "EXEC LoginCheck " +
                    "@Username = " + username + ", " +
                    "@HashPass = " + password + ", " +
                    "@result = @validated OUTPUT;" +
                    "SELECT Validated = @validated;";

            PreparedStatement pstmt = con.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();
            rs.next();

            String output = rs.getString("Validated");
            //System.out.println(output);

            if (output.contains("1")) {
                loggedIn = true;
            } else {
                loggedIn = false;
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
    }

    public static Connection getConnection(){
        connect();
        return con;
    }

    public static void closeConnection(){
        disconnect();
    }
}
