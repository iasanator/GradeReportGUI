package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {

        final JFrame frame = new JFrame("Login");

        LoginDialog loginDlg = new LoginDialog(frame);
        loginDlg.setVisible(true);

        if(loginDlg.isSucceeded()){
            System.out.println("Hi " + loginDlg.getUsername() + "!");
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 780);
        frame.setLayout(new FlowLayout());
        frame.setVisible(true);

    }
}
