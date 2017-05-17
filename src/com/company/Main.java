package com.company;

import javax.swing.*;

/*

    This is the main class from which the program starts.
    It is responsible for creating the login dialog and spawning the corresponding GRFrame

 */

public class Main {

    public static DatabaseConnector dbConnector;

    public static String user;
    public static int userID;
    public static boolean isStudent;

    public static GRFrame frame;

    public static final int MAX_STRING_SIZE = 32;

    public static void main(String[] args) {


        //Create the database connector
        Main.dbConnector = new DatabaseConnector();

        //Create the login dialog and wait until it responds
        LoginDialog loginDlg = new LoginDialog(new JFrame());
        loginDlg.setVisible(true);

        if(loginDlg.isSucceeded()){

            loginDlg.dispose();

            //Create the appropriate GRFrame
            if (Main.isStudent) {
                frame = new GRFrameStudent();
            } else {
                frame = new GRFrameTeacher();
            }

            frame.setVisible(true);
        } else {
            System.out.println("Login failed. Exiting...");
            loginDlg.dispose();
            System.exit(0);
        }

    }
}
