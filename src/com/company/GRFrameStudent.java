package com.company;

import java.awt.BorderLayout;
import java.awt.Color;
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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Created by iassona on 5/2/2017.
 */
public class GRFrameStudent extends GRFrame {

    public GRFrameStudent() {
        super("GradeReport: Student Edition v0.0");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 768);

        this.initMenuBar();

    }

    private void initMenuBar() {

        super.menuBar = new JMenuBar();

        menu = new JMenu("Menu");
        menuBar.add(menu);

        menuItem = new JMenuItem("Show student grades");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
            	JFrame pop = new JFrame("Add class");

                JComboBox comboBox = new JComboBox();

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
                    
                    comboBox.setSelectedItem(null);
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
                            String SQL = "DECLARE @classID int;" +
                                    "SELECT @classID = ClassID FROM Class WHERE Name = ?" +
                                    " AND SectionNumber = ?;"
                                    + "EXEC studentAssignments ?, @classID";

                            PreparedStatement pstmt;
                            try {
                                pstmt = con.prepareStatement(SQL);
                                pstmt.setString(1, array[1]);
                                pstmt.setString(2, array[2]);
                                pstmt.setString(3, String.valueOf(Main.userID));
                                ResultSet rs = pstmt.executeQuery();
                                getContentPane().removeAll();
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

        menuItem = new JMenuItem("Show student grade roster");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub.
                //System.out.println("menu pressed User: " + userID);
                getContentPane().removeAll();

                try {
                    Connection con = DatabaseConnector.getConnection();
                    String SQL = "EXEC studentGradeRoster ?";

                    PreparedStatement pstmt = con.prepareStatement(SQL);
                    pstmt.setString(1, String.valueOf(Main.userID));
                    ResultSet rs = pstmt.executeQuery();

                    makeTable(rs);
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
                            "WHERE NOT ClassID IN (SELECT ClassID FROM Enrolled WHERE StudentID = ?)";

                    PreparedStatement pstmt = con.prepareStatement(SQL);
                    pstmt.setString(1, String.valueOf(Main.userID));
                    ResultSet rs = pstmt.executeQuery();

                    DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
                    comboBox.setModel(comboBoxModel);

                    while(rs.next()) {
                        comboBoxModel.addElement(rs.getString("CourseListing") + ": " + rs.getString("Name") + ": " + rs.getString("SectionNumber"));
                    }
                    comboBox.setSelectedItem(null);
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
        menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFrame pop = new JFrame("Create category");
				JPanel panel = new JPanel(new GridBagLayout());
				
				final JComboBox comboBox = new JComboBox();
				GridBagConstraints layout = new GridBagConstraints();

                layout.fill = GridBagConstraints.HORIZONTAL; 
				
				try {
				Connection con = DatabaseConnector.getConnection();
				String SQL = "SELECT Name, SectionNumber, CourseListing FROM Class " +
				"WHERE ClassID IN (SELECT ClassID FROM Enrolled WHERE StudentID = ?)";
				
				PreparedStatement pstmt = con.prepareStatement(SQL);
				pstmt.setString(1, String.valueOf(Main.userID));
				ResultSet rs = pstmt.executeQuery();
				
				DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
				comboBox.setModel(comboBoxModel);
				comboBox.setSelectedItem(null);
				
				while(rs.next()) {
					comboBoxModel.addElement(rs.getString("CourseListing") + ": " + rs.getString("Name") + ": " + rs.getString("SectionNumber"));
				}
				layout.gridx = 0;
				layout.gridy = 0;
				layout.gridwidth = 1;
				panel.add(comboBox, layout);

				final JTextField nameText = new JTextField("Name");
                    nameText.addKeyListener(new KeyAdapter() {
                        public void keyTyped(KeyEvent e) {
                            if (nameText.getText().length() >= Main.MAX_STRING_SIZE ) {
                                e.consume();
                            }
                        }
                    });
				layout.gridy = 1;
				panel.add(nameText, layout);
				final JTextField weightText = new JTextField("Weight as %");
                    weightText.addKeyListener(new KeyAdapter() {
                        public void keyTyped(KeyEvent e) {
                            if (weightText.getText().length() >= Main.MAX_STRING_SIZE ) {
                                e.consume();
                            }
                        }
                    });
				layout.gridy = 2;
				panel.add(weightText, layout);
				final JButton sendButton = new JButton("Submit");
				
				sendButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub.
						String catName;
						float catWeight;
						int classID;
						String item = comboBox.getSelectedItem().toString();

                        try {
                            Integer.parseInt(weightText.getText());
                        } catch (Exception e){
                            weightText.setBackground(Color.RED);
                            return;
                        }
						
						String[] array = item.split(": ");
						//System.out.println(array[1]);
						
						Connection con1 = DatabaseConnector.getConnection();
						String clsID = "(SELECT ClassID FROM Class WHERE Name = ?" +
						" AND SectionNumber = ?)";
						
						catName = nameText.getText();
						catWeight = Integer.parseInt(weightText.getText());
						catWeight/=100;
						
						PreparedStatement pstmt;
						try {
							ResultSet temp;
							
							pstmt = con1.prepareStatement(clsID);
							pstmt.setString(1, array[1]);
							pstmt.setString(2, array[2]);
							temp = pstmt.executeQuery();
							temp.next();
							classID = temp.getInt(1);
							String insert = "INSERT INTO Category (ClassID, Name, Weight) VALUES (?, ?, ?);";
							pstmt = con1.prepareStatement(insert);
							pstmt.setString(1, String.valueOf(classID));
							pstmt.setString(2, catName);
							pstmt.setString(3, String.valueOf(catWeight));
							pstmt.executeQuery();
							nameText.setText("Next Name");
							weightText.setText("Next Weight");
							
									
						} catch (SQLException exception) {
							// TODO Auto-generated catch-block stub.
							exception.printStackTrace();
							
						}

					}});
				layout.gridy = 3;
				panel.add(sendButton, layout);	
				
				pop.add(panel);
				pop.pack();
				pop.setVisible(true);
				} catch (SQLException exception) {
					// TODO Auto-generated catch-block stub.
					exception.printStackTrace();
				}				
		}});
        menu.add(menuItem);

        menuItem = new JMenuItem("Create assignment");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame pop = new JFrame("Create assignment");

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
                    comboBox.setSelectedItem(null);
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
                                        "WHERE ClassID IN (SELECT ClassID FROM Class WHERE Name = ? " +
                                        "AND SectionNumber = ?)";

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


                            try {
                                Integer.parseInt(points.getText());
                            } catch (Exception e){
                                points.setBackground(Color.RED);
                                return;
                            }

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
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame pop = new JFrame("Create grade");

                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints cs = new GridBagConstraints();

                cs.fill = GridBagConstraints.HORIZONTAL;

                JComboBox comboBox = new JComboBox();
                JComboBox comboBox2 = new JComboBox();
                JComboBox comboBox3	= new JComboBox();

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
                    comboBox.setSelectedItem(null);
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
                    
                    comboBox2.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            String item = comboBox.getSelectedItem().toString();
                            String item2 =comboBox2.getSelectedItem().toString();

                            String[] array = item.split(": ");
                            //System.out.println(array[1]);

                            try {
                                Connection con = DatabaseConnector.getConnection();
                                String SQL = "SELECT Name FROM Assignment " +
                                        "WHERE CategoryID IN (SELECT CategoryID FROM Category WHERE Name = '"
                                        + item2 + "' AND ClassID IN (SELECT ClassID FROM Class WHERE Name = ?" +
                                        " AND SectionNumber = ?)"
                                        + ") AND AssignmentID NOT IN (SELECT AssignmentID FROM Grade "
                                        + "WHERE StudentID = ?)";

                                PreparedStatement pstmt = con.prepareStatement(SQL);
                                pstmt.setString(1, array[1]);
                                pstmt.setString(2, array[2]);
                                pstmt.setString(3, String.valueOf(Main.userID));
                                ResultSet rs = pstmt.executeQuery();

                                DefaultComboBoxModel aModel = new DefaultComboBoxModel();
                                comboBox3.setModel(aModel);

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
                    cs.gridy = 3;
                    cs.gridwidth = 1;
                    panel.add(comboBox3, cs);

                    // Create fields for assignment
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
                            String assignmentName = comboBox3.getSelectedItem().toString();
                            String assignmentPoints = points.getText();
                            String item = comboBox.getSelectedItem().toString();


                            try {
                                Integer.parseInt(points.getText());
                            } catch (Exception e){
                                points.setBackground(Color.RED);
                                return;
                            }

                            String[] array = item.split(": ");

                            Connection con = DatabaseConnector.getConnection();
                            String SQL = "INSERT INTO Grade VALUES (?"
                                    + ", (SELECT AssignmentID FROM Assignment WHERE " 
                            		+ "Name = ? AND CategoryID IN "
                            		+ "(SELECT CategoryID FROM Category WHERE"
                            		+ " Name = "
                                            + "? AND ClassID IN (SELECT ClassID FROM Class "
                                            +" WHERE Name = ?" +
                                        " AND SectionNumber = ?))), ?)";

                            PreparedStatement pstmt;
                            try {
                                pstmt = con.prepareStatement(SQL);
                                pstmt.setString(1, String.valueOf(Main.userID));
                                pstmt.setString(2, assignmentName);
                                pstmt.setString(3, category);
                                pstmt.setString(4, array[1]);
                                pstmt.setString(5, array[2]);
                                pstmt.setString(6, assignmentPoints);
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

        this.setJMenuBar(menuBar);

    }

}
