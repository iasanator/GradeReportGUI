package com.company;

import java.awt.FlowLayout;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

        final JFrame frame = new JFrame("Login");

        LoginDialog loginDlg = new LoginDialog(frame);
        loginDlg.setVisible(true);

        if(loginDlg.isSucceeded()){
            System.out.println("Hello " + loginDlg.getUsername() + "!");
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 780);
        frame.setLayout(new FlowLayout());
        frame.setVisible(true);

    }
}
