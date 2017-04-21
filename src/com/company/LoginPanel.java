package com.company;

import javax.swing.*;

/**
 * Created by iassona on 4/21/2017.
 */
public class LoginPanel extends JPanel{

    public LoginPanel() {
        JTextField textFieldUsername = new JTextField();
        add(textFieldUsername);

        JTextField textFieldPassword = new JTextField();
        add(textFieldPassword);

        JButton buttonStudent = new JButton();
        buttonStudent.setText("Login as Student");
        add(buttonStudent);

        JButton buttonTeacher = new JButton();
        buttonTeacher.setText("Login as Teacher");
        add(buttonTeacher);

        setVisible(true);
    }

}
