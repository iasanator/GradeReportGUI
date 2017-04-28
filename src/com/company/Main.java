package com.company;

public class Main {

    public static void main(String[] args) {

        final GRFrame frame = new GRFrame("GradeReport");

        LoginDialog loginDlg = new LoginDialog(frame);
        loginDlg.setVisible(true);

        if(loginDlg.isSucceeded()){
            System.out.println("Hello " + loginDlg.getUsername() + "!");
            frame.setVisible(true);
        }

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1024, 780);
//        frame.setLayout(new FlowLayout());
//        frame.setVisible(true);

    }
}
