package com.company;

import javax.swing.*;

public class Main {

    public static DatabaseConnector dbConnector;

    public static String user;
    public static int userID;
    public static boolean isStudent;

    public static GRFrame frame;

    public static void main(String[] args) {

        Main.dbConnector = new DatabaseConnector();

        LoginDialog loginDlg = new LoginDialog(new JFrame());
        loginDlg.setVisible(true);

        if(loginDlg.isSucceeded()){

            loginDlg.dispose();

            System.out.println(Main.user + ":" + Main.userID + ":" + isStudent);

            if (Main.isStudent) {
                //Main.frame = new GRFrameStudent("GradeReport: Student Edition v0.000000001");
            } else {
                //Main.frame = new GRFrameTeacher("GradeReport: Teacher Edition v0.000000001");
            }

            //frame.setUser(loginDlg.getuserID());
            //frame.setVisible(true);
        }

        //



//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1024, 780);
//        frame.setLayout(new FlowLayout());
//        frame.setVisible(true);

    }
}
