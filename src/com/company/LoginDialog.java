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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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


        //Begin to create the layout of the login pane

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
                loginAction(true);
            }
        });

        btnLoginTeacher = new JButton("Login As Teacher");

        btnLoginTeacher.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loginAction(false);
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

    /**
     *
     * Gets the username input to the login dialog
     *
     * @return
     */
    public String getUsername() {
        return tfUsername.getText().trim();
    }

    /**
     *
     * Gets the password input to the login dialog
     *
     * @return
     */
    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    /**
     *
     * Gets whether or not the login attemp was successful
     *
     * @return
     */
    public boolean isSucceeded() {
        return succeeded;
    }

    /**
     *
     * Handles the authentication and registration of a user in the database.
     *
     * @param isStudent
     */
    private void loginAction(boolean isStudent){
        if (Main.dbConnector.authenticateLogin(getUsername(), getPassword(), isStudent, LoginDialog.this)) {
            JOptionPane.showMessageDialog(LoginDialog.this,
                    "Hi " + getUsername() + "! You have successfully logged in.",
                    "Login",
                    JOptionPane.INFORMATION_MESSAGE);
            succeeded = true;

            Main.isStudent = isStudent;
            Main.user = getUsername();

            dispose();
        } else {
            int response = JOptionPane.showConfirmDialog(LoginDialog.this,
                    "Invalid username or password. Create a new account?",
                    "Login",
                    JOptionPane.ERROR_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {

                if (usernameInDatabase(getUsername(), isStudent)){
                    JOptionPane.showMessageDialog(LoginDialog.this, "Username is already taken", "Error", JOptionPane.ERROR_MESSAGE);

                    // reset username and password
                    tfUsername.setText("");
                    pfPassword.setText("");
                    succeeded = false;

                    return;

                }

                JPasswordField jpf = new JPasswordField(24);
                JLabel jl = new JLabel("Please re-enter your password");
                Box box = Box.createVerticalBox();
                box.add(jl);
                box.add(jpf);
                JOptionPane.showMessageDialog(LoginDialog.this, box, "Password", JOptionPane.QUESTION_MESSAGE);

                String password = jpf.getText();

                if (!password.equals(getPassword())) {
                    JOptionPane.showMessageDialog(LoginDialog.this, "Passwords don't match", "Error", JOptionPane.OK_OPTION);

                    // reset username and password
                    tfUsername.setText("");
                    pfPassword.setText("");
                    succeeded = false;

                    return;

                }

                String name = JOptionPane.showInputDialog(LoginDialog.this, "Please enter your name");

                while (name.length() > Main.MAX_STRING_SIZE) {
                    JOptionPane.showMessageDialog(LoginDialog.this, "Name is too long", "Error", JOptionPane.ERROR_MESSAGE);
                    name = JOptionPane.showInputDialog("Please enter your name");
                }

                try {
                    Connection con = DatabaseConnector.getConnection();
                    String SQL;
                    if (isStudent) {
                        SQL = "INSERT INTO Student VALUES (?, ?, ?, ?)";
                    } else {
                        SQL = "INSERT INTO Teacher VALUES (?, ?, ?)";
                    }

                    PreparedStatement pstmt = con.prepareStatement(SQL);
                    pstmt.setString(1, String.valueOf(getUsername()));
                    pstmt.setString(2, DatabaseConnector.hash(getPassword()));
                    pstmt.setString(3, name);

                    if (isStudent){
                        pstmt.setString(4, "4.0");
                    }

                    pstmt.execute();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginDialog.this, "Your username already exists", "Login", JOptionPane.ERROR_MESSAGE);

                    // reset username and password
                    tfUsername.setText("");
                    pfPassword.setText("");
                    succeeded = false;

                    return;

                }

                succeeded = true;

                Main.isStudent = isStudent;
                Main.user = getUsername();

                dispose();
                return;

            } else {

                // reset username and password
                tfUsername.setText("");
                pfPassword.setText("");
                succeeded = false;
            }
        }
    }


    /**
     *
     * This checks to see if a given username is already contained in either the
     * student of the teacher tables of the database.
     *
     * @param username
     * @param isStudent
     * @return
     */
    private boolean usernameInDatabase(String username, boolean isStudent) {

        String table = "Student";

        if (!isStudent) {
            table = "Teacher";
        }

        try {
            Connection con = DatabaseConnector.getConnection();
            String SQL = "SELECT Username FROM " + table +
                    " WHERE Username = ?";

            System.out.println(SQL);

            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException exception) {
            // TODO Auto-generated catch-block stub.
            exception.printStackTrace();
        }

        return false;
    }

}