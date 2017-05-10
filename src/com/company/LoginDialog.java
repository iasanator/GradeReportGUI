package com.company;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LoginDialog extends JDialog {

    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JButton btnLoginStudent;
    private JButton btnLoginTeacher;
    private JButton btnCancel;
    private boolean succeeded;
    
    int userID;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        tfUsername.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (tfUsername.getText().length() >= Main.MAX_STRING_SIZE ) {
                    e.consume();
                }
            }
        });
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));

        btnLoginStudent = new JButton("Login As Student");

        btnLoginStudent.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Main.dbConnector.authenticateLogin(getUsername(), getPassword(), true, LoginDialog.this)) {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Hi " + getUsername() + "! You have successfully logged in.",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                    succeeded = true;

                    Main.isStudent = true;
                    Main.user = getUsername();

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Invalid username or password",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                    // reset username and password
                    tfUsername.setText("");
                    pfPassword.setText("");
                    succeeded = false;
                }
            }
        });

        btnLoginTeacher = new JButton("Login As Teacher");

        btnLoginTeacher.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Main.dbConnector.authenticateLogin(getUsername(), getPassword(), false,LoginDialog.this)) {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Hi " + getUsername() + "! You have successfully logged in.",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                    succeeded = true;

                    Main.isStudent = false;
                    Main.user = getUsername();

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Invalid username or password",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                    // reset username and password
                    tfUsername.setText("");
                    pfPassword.setText("");
                    succeeded = false;

                }
            }
        });

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(btnLoginStudent);
        bp.add(btnLoginTeacher);
        bp.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        this.getRootPane().setDefaultButton(btnLoginStudent);
    }

    public String getUsername() {
        return tfUsername.getText().trim();
    }

    public String getPassword() {
        return new String(pfPassword.getPassword());
    }
    
    public int getuserID() {
    	return userID;
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}