package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by iassona on 4/21/2017.
 */
public class DatabaseConnector {

    public static void main(String[] args) {
    	String connectionUrl = "jdbc:sqlserver://localhost:1433;" + 
    			"databaseName=GradeReport;user=MyUserName;password=*****;";
    	try {
			Connection con = DriverManager.getConnection(connectionUrl);
		} catch (SQLException exception) {
			// TODO Auto-generated catch-block stub.
			exception.printStackTrace();
		}
    }

    public boolean authenticateLogin(String username, String password) {
         return true;
    }

}
