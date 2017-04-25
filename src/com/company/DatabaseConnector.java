package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by iassona on 4/21/2017.
 */
public class DatabaseConnector {
	static Connection con;

    public static void main(String[] args) {
    	String connectionUrl = "jdbc:sqlserver://golem.csse.rose-hulman.edu:1433;" + 
    			"databaseName=GradeReport_Data;user=UserName;password=*****;";
    	try {
			Connection con = DriverManager.getConnection(connectionUrl);
	    	String SQL = "SELECT * FROM Student";
	    	PreparedStatement pstmt = con.prepareStatement(SQL);
	    	ResultSet rs = pstmt.executeQuery();
	    	
	    	while (rs.next()) {
	    		System.out.println(rs.getString("Name"));
	    	}
	    	rs.close();
	    	pstmt.close();
		} catch (SQLException exception) {
			// TODO Auto-generated catch-block stub.
			exception.printStackTrace();
		}
    }

    public boolean authenticateLogin(String username, String password) {
         return true;
    }

}
