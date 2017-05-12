package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

/**
 * Created by iassona on 5/2/2017.
 */
public class GRFrameTeacher extends GRFrame {

    public GRFrameTeacher() {
        super("GradeReport: Teacher Edition v0.000000001");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1024, 768);

        this.initMenuBar();
    }

    private void initMenuBar() {

        menuBar = new JMenuBar();

        menu = new JMenu("Menu");
        menuBar.add(menu);

        menuItem = new JMenuItem("My classes");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub.
                System.out.println("menu pressed User: " + Main.userID);
                getContentPane().removeAll();

                try {
                    Connection con = DatabaseConnector.getConnection();
                    String SQL = "EXEC getTeachersClasses " + Main.userID;

                    PreparedStatement pstmt = con.prepareStatement(SQL);
                    ResultSet rs = pstmt.executeQuery();

                    makeTable(rs);

                } catch (SQLException exception) {
                    // TODO Auto-generated catch-block stub.
                    exception.printStackTrace();
                }
            }});

        menu.add(menuItem);

        menuItem = new JMenuItem("View Roster");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame pop = new JFrame("Select Class");

                JComboBox comboBox = new JComboBox();

                try {
                    Connection con = DatabaseConnector.getConnection();
                    String SQL = "SELECT Name, SectionNumber, CourseListing FROM Class " +
                            "WHERE ClassID IN (SELECT ClassID FROM Class WHERE TeacherID = ?)";

                    PreparedStatement pstmt = con.prepareStatement(SQL);
                    pstmt.setString(1, String.valueOf(Main.userID));
                    ResultSet rs = pstmt.executeQuery();

                    DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
                    comboBox.setModel(comboBoxModel);

                    while(rs.next()) {
                        comboBoxModel.addElement(rs.getString("CourseListing") + ": " + rs.getString("Name") + ": " + rs.getString("SectionNumber"));
                    }

                    pop.getContentPane().add(comboBox, BorderLayout.CENTER);

                    JButton select = new JButton("Select");
                    select.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            // TODO Auto-generated method stub.
                            String item = comboBox.getSelectedItem().toString();

                            String[] array = item.split(": ");
                            System.out.println(array[2]);

                            Connection con = DatabaseConnector.getConnection();
                            String SQL = "DECLARE @input INT; " +
                                            "SELECT @input = classID FROM Class WHERE Name = ?"+
                                            " AND SectionNumber = ?;" +
                                            "EXEC classGradeRoster @ClassID = @input";

                            PreparedStatement pstmt;
                            try {
                                pstmt = con.prepareStatement(SQL);
                                pstmt.setString(1, array[1]);
                                pstmt.setString(2, array[2]);
                                ResultSet rs = pstmt.executeQuery();
                                makeTable(rs);
                                pstmt.close();
                                pop.dispose();
                            } catch (SQLException exception) {
                                // TODO Auto-generated catch-block stub.
                                exception.printStackTrace();
                            }
                        }

                    });


                    pop.getContentPane().add(select, BorderLayout.PAGE_END);
                    pop.pack();
                    pop.setVisible(true);
                } catch (SQLException exception) {
                    // TODO Auto-generated catch-block stub.
                    exception.printStackTrace();
                }
            }});

        menu.add(menuItem);

        menu = new JMenu("Edit");
        menuBar.add(menu);

        menuItem = new JMenuItem("Create class");menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame pop = new JFrame("Create class");

                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints cs = new GridBagConstraints();

                cs.fill = GridBagConstraints.HORIZONTAL;

                JComboBox comboBox = new JComboBox();

                try {
                    Connection con = DatabaseConnector.getConnection();
                    String SQL = "SELECT Name, TeacherID FROM Teacher";

                    PreparedStatement pstmt = con.prepareStatement(SQL);
                    ResultSet rs = pstmt.executeQuery();

                    DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
                    comboBox.setModel(comboBoxModel);
                    while(rs.next()) {
                        comboBoxModel.addElement(rs.getString("Name") + ":" + rs.getString("TeacherID"));
                    }

                    comboBoxModel.addElement("No Teacher");

                    cs.gridx = 0;
                    cs.gridy = 1;
                    cs.gridwidth = 1;
                    panel.add(comboBox, cs);

                    // Create fields for assignment
                    JTextField name = new JTextField("Name");
                    name.addKeyListener(new KeyAdapter() {
                        public void keyTyped(KeyEvent e) {
                            if (name.getText().length() >= Main.MAX_STRING_SIZE ) {
                                e.consume();
                            }
                        }
                    });
                    cs.gridx = 0;
                    cs.gridy = 2;
                    cs.gridwidth = 1;
                    panel.add(name, cs);

                    JTextField section = new JTextField("Section Number");
                    section.addKeyListener(new KeyAdapter() {
                        public void keyTyped(KeyEvent e) {
                            if (section.getText().length() >= Main.MAX_STRING_SIZE ) {
                                e.consume();
                            }
                        }
                    });
                    cs.gridx = 0;
                    cs.gridy = 3;
                    cs.gridwidth = 1;
                    panel.add(section, cs);


                    JTextField listing = new JTextField("Listing");
                    listing.addKeyListener(new KeyAdapter() {
                        public void keyTyped(KeyEvent e) {
                            if (listing.getText().length() >= Main.MAX_STRING_SIZE ) {
                                e.consume();
                            }
                        }
                    });
                    cs.gridx = 0;
                    cs.gridy = 4;
                    cs.gridwidth = 1;
                    panel.add(listing, cs);

                    JButton select = new JButton("Select");
                    select.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            // TODO Auto-generated method stub.
                            String teacherID;
                            String verified;
                            if (comboBox.getSelectedItem().toString() != "No Teacher") {
                                teacherID = comboBox.getSelectedItem().toString().split(":")[1];
                                verified = "1";
                            } else {
                                teacherID = "NULL";
                                verified = "0";
                            }
                            String className = name.getText();
                            String classSection = section.getText();
                            String classListing = listing.getText();

                            try {
                                Integer.parseInt(classSection);
                            } catch (Exception e){
                                section.setBackground(Color.RED);
                                return;
                            }


                            Connection con = DatabaseConnector.getConnection();

                            String SQL;

                            if (!teacherID.equals("NULL")) {
                                SQL = "INSERT INTO Class VALUES (?, ?, ?, ?, ?)";
                            } else {
                                SQL = "INSERT INTO Class VALUES (NULL, ?, ?, ?, ?)";
                            }

                            PreparedStatement pstmt;
                            try {
                                pstmt = con.prepareStatement(SQL);
                                if (!teacherID.equals("NULL")) {
                                    pstmt.setString(1, teacherID);
                                    pstmt.setString(2, verified);
                                    pstmt.setString(3, className);
                                    pstmt.setString(4, classSection);
                                    pstmt.setString(5, classListing);
                                } else {
                                    pstmt.setString(1, verified);
                                    pstmt.setString(2, className);
                                    pstmt.setString(3, classSection);
                                    pstmt.setString(4, classListing);
                                }
                                pstmt.execute();
                                pop.dispose();
                            } catch (SQLException exception) {
                                // TODO Auto-generated catch-block stub.
                                exception.printStackTrace();
                            }
                        }

                    });

                    cs.gridx = 0;
                    cs.gridy = 5;
                    cs.gridwidth = 1;
                    panel.add(select, cs);
                    pop.add(panel);
                    pop.pack();
                    pop.setVisible(true);
                } catch (SQLException exception) {
                    // TODO Auto-generated catch-block stub.
                    exception.printStackTrace();
                }
            }});
        menu.add(menuItem);

        menuItem = new JMenuItem("Add class");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame pop = new JFrame("Add class");

                JComboBox comboBox = new JComboBox();

                try {
                    Connection con = DatabaseConnector.getConnection();
                    String SQL = "SELECT Name, SectionNumber, CourseListing FROM Class " +
                            "WHERE NOT ClassID IN (SELECT ClassID FROM Enrolled WHERE StudentID = ?";

                    PreparedStatement pstmt = con.prepareStatement(SQL);
                    pstmt.setString(1, String.valueOf(Main.userID));
                    ResultSet rs = pstmt.executeQuery();

                    DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
                    comboBox.setModel(comboBoxModel);

                    while(rs.next()) {
                        comboBoxModel.addElement(rs.getString("CourseListing") + ": " + rs.getString("Name") + ": " + rs.getString("SectionNumber"));
                    }

                    pop.getContentPane().add(comboBox, BorderLayout.CENTER);

                    JButton select = new JButton("Select");
                    select.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            // TODO Auto-generated method stub.
                            String item = comboBox.getSelectedItem().toString();

                            String[] array = item.split(": ");
                            System.out.println(array[1]);

                            Connection con = DatabaseConnector.getConnection();
                            String SQL = "INSERT INTO Enrolled(ClassID, StudentID) VALUES (" +
                                    "(SELECT ClassID FROM Class WHERE Name = ?" +
                                    " AND SectionNumber = ?), ?)";

                            PreparedStatement pstmt;
                            try {
                                pstmt = con.prepareStatement(SQL);
                                pstmt.setString(1, array[1]);
                                pstmt.setString(2, array[2]);
                                pstmt.setString(3, String.valueOf(Main.userID));
                                pstmt.execute();
                                pop.dispose();
                            } catch (SQLException exception) {
                                // TODO Auto-generated catch-block stub.
                                exception.printStackTrace();
                            }
                        }

                    });


                    pop.getContentPane().add(select, BorderLayout.PAGE_END);
                    pop.pack();
                    pop.setVisible(true);
                } catch (SQLException exception) {
                    // TODO Auto-generated catch-block stub.
                    exception.printStackTrace();
                }
            }});

        menu.add(menuItem);

        menuItem = new JMenuItem("Create category");
        menu.add(menuItem);

        menuItem = new JMenuItem("Create assignment");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame pop = new JFrame("Create Assignment");

                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints cs = new GridBagConstraints();

                cs.fill = GridBagConstraints.HORIZONTAL;

                JComboBox comboBox = new JComboBox();
                JComboBox comboBox2 = new JComboBox();

                try {
                    Connection con = DatabaseConnector.getConnection();
                    String SQL = "SELECT Name, SectionNumber, CourseListing FROM Class " +
                            "WHERE ClassID IN (SELECT ClassID FROM Enrolled WHERE StudentID = ?)";

                    PreparedStatement pstmt = con.prepareStatement(SQL);
                    pstmt.setString(1, String.valueOf(Main.userID));
                    ResultSet rs = pstmt.executeQuery();

                    DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
                    comboBox.setModel(comboBoxModel);
                    while(rs.next()) {
                        comboBoxModel.addElement(rs.getString("CourseListing") + ": " + rs.getString("Name") + ": " + rs.getString("SectionNumber"));
                    }

                    cs.gridx = 0;
                    cs.gridy = 0;
                    cs.gridwidth = 1;
                    panel.add(comboBox, cs);

                    comboBox.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            String item = comboBox.getSelectedItem().toString();

                            String[] array = item.split(": ");
                            //System.out.println(array[1]);

                            try {
                                Connection con = DatabaseConnector.getConnection();
                                String SQL = "SELECT Name FROM Category " +
                                        "WHERE ClassID IN (SELECT ClassID FROM Class WHERE Name = ?" +
                                        " AND SectionNumber = ?)";

                                PreparedStatement pstmt = con.prepareStatement(SQL);
                                pstmt.setString(1, array[1]);
                                pstmt.setString(2, array[2]);
                                ResultSet rs = pstmt.executeQuery();

                                DefaultComboBoxModel aModel = new DefaultComboBoxModel();
                                comboBox2.setModel(aModel);

                                while(rs.next()) {
                                    aModel.addElement(rs.getString("Name"));
                                }

                            } catch (SQLException exception) {
                                // TODO Auto-generated catch-block stub.
                                exception.printStackTrace();
                            }

                        }

                    });

                    cs.gridx = 0;
                    cs.gridy = 1;
                    cs.gridwidth = 1;
                    panel.add(comboBox2, cs);

                    // Create fields for assignment
                    JTextField name = new JTextField("Name");
                    name.addKeyListener(new KeyAdapter() {
                        public void keyTyped(KeyEvent e) {
                            if (name.getText().length() >= Main.MAX_STRING_SIZE ) {
                                e.consume();
                            }
                        }
                    });

                    cs.gridx = 0;
                    cs.gridy = 3;
                    cs.gridwidth = 1;
                    panel.add(name, cs);

                    JTextField points = new JTextField("Total Points");
                    points.addKeyListener(new KeyAdapter() {
                        public void keyTyped(KeyEvent e) {
                            if (points.getText().length() >= Main.MAX_STRING_SIZE ) {
                                e.consume();
                            }
                        }
                    });

                    cs.gridx = 0;
                    cs.gridy = 4;
                    cs.gridwidth = 1;
                    panel.add(points, cs);

                    JButton select = new JButton("Select");
                    select.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            // TODO Auto-generated method stub.
                            String category = comboBox2.getSelectedItem().toString();
                            String assignmentName = name.getText();
                            String assignmentPoints = points.getText();
                            String item = comboBox.getSelectedItem().toString();

                            String[] array = item.split(": ");

                            Connection con = DatabaseConnector.getConnection();
                            String SQL = "INSERT INTO Assignment VALUES (" +
                                    "(SELECT CategoryID FROM Category WHERE Name = ?" +
                                    " AND ClassID IN (SELECT ClassID FROM Class WHERE Name = ?" +
                                    " AND SectionNumber = ?)), ?" +
                                    ", ?)";

                            PreparedStatement pstmt;
                            try {
                                pstmt = con.prepareStatement(SQL);
                                pstmt.setString(1, category);
                                pstmt.setString(2, array[1]);
                                pstmt.setString(3, array[2]);
                                pstmt.setString(4, assignmentName);
                                pstmt.setString(5, assignmentPoints);
                                pstmt.execute();
                                pop.dispose();
                            } catch (SQLException exception) {
                                // TODO Auto-generated catch-block stub.
                                exception.printStackTrace();
                            }
                        }

                    });

                    cs.gridx = 0;
                    cs.gridy = 5;
                    cs.gridwidth = 1;
                    panel.add(select, cs);
                    pop.add(panel);
                    pop.pack();
                    pop.setVisible(true);
                } catch (SQLException exception) {
                    // TODO Auto-generated catch-block stub.
                    exception.printStackTrace();
                }
            }});

        menu.add(menuItem);

        menuItem = new JMenuItem("Create grade");
        menu.add(menuItem);

        this.setJMenuBar(menuBar);
    }
}
