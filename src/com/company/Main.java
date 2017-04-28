package com.company;

public class Main {

    public static DatabaseConnector dbConnector;

    public static String user;
    public static int userID;
    public static boolean isStudent;

    public static void main(String[] args) {

        Main.dbConnector = new DatabaseConnector();

        final GRFrame frame = new GRFrame("GradeReport");

        LoginDialog loginDlg = new LoginDialog(frame);
        loginDlg.setVisible(true);

        if(loginDlg.isSucceeded()){
            System.out.println("Hello " + Main.user + "!");
            frame.setVisible(true);
            loginDlg.dispose();
        }

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1024, 780);
//        frame.setLayout(new FlowLayout());
//        frame.setVisible(true);

    }
}
