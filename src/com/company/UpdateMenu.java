package com.company;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UpdateMenu {
	JMenu menu;
	JMenuItem menuItem;

	public UpdateMenu(boolean isStudent) {

		menu = new JMenu("Update");

		menuItem = new JMenuItem("Update class");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFrame pop = new JFrame("Update class");
				final JPanel panel = new JPanel(new GridBagLayout());
				
				final JComboBox comboBox = new JComboBox();
				final GridBagConstraints layout = new GridBagConstraints();
				layout.fill = GridBagConstraints.HORIZONTAL;

				try {
					Connection con = DatabaseConnector.getConnection();
					String SQL = "SELECT Name, SectionNumber, CourseListing FROM Class "
							+ "WHERE ClassID IN (SELECT ClassID FROM Enrolled WHERE StudentID = ?)";

					PreparedStatement pstmt = con.prepareStatement(SQL);
					pstmt.setString(1, String.valueOf(Main.userID));
					ResultSet rs = pstmt.executeQuery();

					DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
					comboBox.setModel(comboBoxModel);

					while (rs.next()) {
						comboBoxModel.addElement(rs.getString("CourseListing") + ": " + rs.getString("Name") + ": "
								+ rs.getString("SectionNumber"));
					}
					comboBox.setSelectedItem(null);
					layout.gridx = 0;
					layout.gridy = 0;
					layout.gridwidth = 2;
					panel.add(comboBox, layout);

					final String[] array = new String[3];
					final JTextField courseListing = new JTextField(array[0]);
					courseListing.addKeyListener(new LengthControler(courseListing));
					final JTextField name = new JTextField(array[1]);
					name.addKeyListener(new LengthControler(name));
					final JTextField sectionNum = new JTextField(array[2]);
					sectionNum.addKeyListener(new LengthControler(sectionNum));
					layout.gridx = 0;
					layout.gridy = 2;
					layout.gridwidth = 2;
					panel.add(courseListing, layout);
					layout.gridy = 3;
					panel.add(name, layout);
					layout.gridy = 4;
					panel.add(sectionNum, layout);
					comboBox.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							String item = comboBox.getSelectedItem().toString();

							String[] temp = item.split(": ");
							array[0] = temp[0];
							array[1] = temp[1];
							array[2] = temp[2];
							//array[0] = CourseListing
							//array[1] = Name
							//array[2] = Section#
							courseListing.setText(array[0]);
							name.setText(array[1]);
							sectionNum.setText(array[2]);
						}

					});
					JButton update = new JButton("Update");
					update.addActionListener(new ActionListener() {
						
						@SuppressWarnings("resource")
						@Override
						public void actionPerformed(ActionEvent e) {
							Connection con = DatabaseConnector.getConnection();
							//TODO: finish sql string with ? method
							String SQL = "UPDATE Class SET CourseListing = ?, Name = ?, SectionNumber = ? " + " WHERE CourseListing = ? and Name = ?"+
									" and SectionNumber = ?;";
							
							PreparedStatement pstmt;
							try {
								pstmt = con.prepareStatement(SQL);
								pstmt.setString(1, courseListing.getText());
								pstmt.setString(2, name.getText());
								pstmt.setString(3, sectionNum.getText());
								pstmt.setString(4, array[0]);
								pstmt.setString(5, array[1]);
								pstmt.setString(6, array[2]);
								
								pstmt.execute();
							} catch (SQLException exception) {
								// TODO Auto-generated catch-block stub.
								exception.printStackTrace();
							}
						}
							
					});
					
					JButton cancel = new JButton("Cancel");
					cancel.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub.
							courseListing.setText(array[0]);
							name.setText(array[1]);
							sectionNum.setText(array[2]);
						}
					});
					layout.gridy = 5;
					layout.gridwidth = 1;
					panel.add(update, layout);
					layout.gridx = 1;
					panel.add(cancel, layout);
					pop.getContentPane().add(panel, BorderLayout.CENTER);
					pop.pack();
					pop.setVisible(true);
				} catch (SQLException exception) {
					// TODO Auto-generated catch-block stub.
					exception.printStackTrace();
				}
			}
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Update category");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFrame pop = new JFrame("Select Class");
				JPanel panel = new JPanel(new GridBagLayout());

				final JComboBox comboBox = new JComboBox();
				final JComboBox comboBox2 = new JComboBox();
				GridBagConstraints layout = new GridBagConstraints();

				layout.fill = GridBagConstraints.HORIZONTAL;
				

				try {
					final String[] array = new String[3];
					final String[] array2 = new String[3];
					final Connection con = DatabaseConnector.getConnection();
					String SQL = "SELECT Name, SectionNumber, CourseListing FROM Class "
							+ "WHERE ClassID IN (SELECT ClassID FROM Enrolled WHERE StudentID = ?)";

					PreparedStatement pstmt = con.prepareStatement(SQL);
					pstmt.setString(1, String.valueOf(Main.userID));
					ResultSet rs = pstmt.executeQuery();

					DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
					comboBox.setModel(comboBoxModel);
					comboBox.setSelectedItem(null);
					final JTextField nameText = new JTextField(array2[1]);
					nameText.addKeyListener(new LengthControler(nameText));
					layout.gridwidth = 2;
					layout.gridy = 2;
					panel.add(nameText, layout);
					final JTextField weightText = new JTextField(array2[2]);
					weightText.addKeyListener(new LengthControler(weightText));
					layout.gridy = 3;
					panel.add(weightText, layout);

					while (rs.next()) {
						comboBoxModel.addElement(rs.getString("CourseListing") + ": " + rs.getString("Name") + ": "
								+ rs.getString("SectionNumber"));
					}
					comboBox.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							String item = comboBox.getSelectedItem().toString();

							final String[] temp = item.split(": ");
							array[0] = temp[0];
							array[1] = temp[1];
							array[2] = temp[2];
							
							//array[0] = CourseListing
							//array[1] = Name
							//array[2] = Section#
							String SQL = "SELECT CategoryID, Name, Weight FROM Category "
							+ "WHERE ClassID = (SELECT ClassID FROM Class WHERE CourseListing = ? and SectionNumber = ?)";
							PreparedStatement pstmt;
							
							DefaultComboBoxModel comboBoxModel2 = new DefaultComboBoxModel();
							comboBox2.setModel(comboBoxModel2);
							comboBox2.setSelectedItem(null);
							
							try {
								pstmt = con.prepareStatement(SQL);
								pstmt.setString(1, array[0]);
								pstmt.setString(2, array[2]);
								ResultSet rs = pstmt.executeQuery();
								while (rs.next()) {
									comboBoxModel2.addElement(rs.getString("CategoryID") + ": " + rs.getString("Name") + ": "
											+ rs.getString("Weight"));
								}
							} catch (SQLException exception1) {
								// TODO Auto-generated catch-block stub.
								exception1.printStackTrace();
							}

							comboBox2.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub.
									if(comboBox2.getSelectedItem()!= null){
									String item = comboBox2.getSelectedItem().toString();

									String[] temp = item.split(": ");
									array2[0] = temp[0];
									array2[1] = temp[1];
									array2[2] = temp[2];
									
									//array2[0] = CategoryID
									//array2[1] = Name
									//array2[2] = Weight
									nameText.setText(array2[1]);
									weightText.setText(array2[2]);
								}}
							});
							
						}
					});
					
					layout.gridx = 0;
					layout.gridy = 0;
					
					panel.add(comboBox, layout);
					layout.gridy = 1;
					panel.add(comboBox2, layout);
					
					
					final JButton sendButton = new JButton("Update");

					sendButton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub.
							String catName;
							float catWeight;
							String item = comboBox.getSelectedItem().toString();

							String[] array = item.split(": ");
							// System.out.println(array[1]);

							Connection con1 = DatabaseConnector.getConnection();
							String clsID = "UPDATE Category SET Name = ?, Weight = ? WHERE CategoryID = ?";

							catName = nameText.getText();
							catWeight = (float) Double.parseDouble(weightText.getText());
							

							PreparedStatement pstmt;
							try {
								pstmt = con1.prepareStatement(clsID);
								pstmt.setString(1, catName);
								pstmt.setLong(2, (long) catWeight);
								pstmt.setString(3, array2[0]);
								pstmt.execute();

							} catch (SQLException exception) {
								// TODO Auto-generated catch-block stub.
								exception.printStackTrace();

							}

						}
					});
					layout.gridy = 4;
					layout.gridwidth = 1;
					panel.add(sendButton, layout);
					
					final JButton cancel = new JButton("Cancel");
					cancel.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							nameText.setText(array2[1]);
							weightText.setText(array2[2]);
						}
					});
					
					layout.gridx = 1;
					layout.gridwidth = 1;
					panel.add(cancel, layout);
					
					pop.add(panel);
					pop.pack();
					pop.setVisible(true);
				} catch (SQLException exception) {
					// TODO Auto-generated catch-block stub.
					exception.printStackTrace();
				}
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Update assignment");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFrame pop = new JFrame("Update assignment");

				JPanel panel = new JPanel(new GridBagLayout());
				GridBagConstraints cs = new GridBagConstraints();

				cs.fill = GridBagConstraints.HORIZONTAL;

				final JComboBox comboBox = new JComboBox();
				final JComboBox comboBox2 = new JComboBox();
				final JComboBox comboBox3 = new JComboBox();
				final String[] array = new String[4];
				

				try {
					Connection con = DatabaseConnector.getConnection();
					String SQL = "SELECT Name, SectionNumber, CourseListing FROM Class "
							+ "WHERE ClassID IN (SELECT ClassID FROM Enrolled WHERE StudentID = ?)";

					PreparedStatement pstmt = con.prepareStatement(SQL);
					pstmt.setString(1, String.valueOf(Main.userID));
					ResultSet rs = pstmt.executeQuery();

					DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
					comboBox.setModel(comboBoxModel);

					while (rs.next()) {
						comboBoxModel.addElement(rs.getString("CourseListing") + ": " + rs.getString("Name") + ": "
								+ rs.getString("SectionNumber"));
					}
					comboBox.setSelectedItem(null);
					cs.gridx = 0;
					cs.gridy = 0;
					cs.gridwidth = 2;
					panel.add(comboBox, cs);

					comboBox.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							String item = comboBox.getSelectedItem().toString();

							String[] temp = item.split(": ");
							// System.out.println(array[1]);

							try {
								Connection con = DatabaseConnector.getConnection();
								String SQL = "SELECT Name, CategoryID FROM Category "
										+ "WHERE ClassID IN (SELECT ClassID FROM Class WHERE Name = ? "
										+ "AND SectionNumber = ?)";

								PreparedStatement pstmt = con.prepareStatement(SQL);
								pstmt.setString(1, temp[1]);
								pstmt.setString(2, temp[2]);
								ResultSet rs = pstmt.executeQuery();

								DefaultComboBoxModel aModel = new DefaultComboBoxModel();
								comboBox2.setModel(aModel);

								while (rs.next()) {
									aModel.addElement(rs.getString("Name")+ ": " + rs.getInt("CategoryID"));
								}

							} catch (SQLException exception) {
								// TODO Auto-generated catch-block stub.
								exception.printStackTrace();
							}

						}

					});

					cs.gridx = 0;
					cs.gridy = 1;
					cs.gridwidth = 2;
					panel.add(comboBox2, cs);
					
					comboBox2.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							if(comboBox2.getSelectedItem()!=null){
							String item = comboBox2.getSelectedItem().toString();

							String[] temp = item.split(": ");
							//array[0] = catID
							array[0] = temp[1];
							try {
								Connection con = DatabaseConnector.getConnection();
								String SQL = "SELECT Name, TotalPoints, AssignmentID FROM Assignment "
										+ "WHERE CategoryID = ?";

								PreparedStatement pstmt = con.prepareStatement(SQL);
								pstmt.setString(1, temp[1]);
								ResultSet rs = pstmt.executeQuery();

								DefaultComboBoxModel aModel = new DefaultComboBoxModel();
								comboBox3.setModel(aModel);

								while (rs.next()) {
									aModel.addElement(rs.getString("Name")+ ": " + rs.getInt("TotalPoints")+": "+rs.getInt("AssignmentID"));
								}

							} catch (SQLException exception) {
								// TODO Auto-generated catch-block stub.
								exception.printStackTrace();
							}
						}}
					});
					cs.gridx = 0;
					cs.gridy = 2;
					cs.gridwidth = 2;
					panel.add(comboBox3, cs);
					final JTextField name = new JTextField(array[1]);
					name.addKeyListener(new LengthControler(name));

					cs.gridx = 0;
					cs.gridy = 4;
					cs.gridwidth = 2;
					panel.add(name, cs);

					final JTextField points = new JTextField(array[2]);
					points.addKeyListener(new LengthControler(points));

					cs.gridx = 0;
					cs.gridy = 5;
					cs.gridwidth = 2;
					panel.add(points, cs);
					comboBox3.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							if(comboBox3.getSelectedItem()!=null){
							String item = comboBox3.getSelectedItem().toString();

							String[] temp = item.split(": ");
							//array[1] = Name
							//array[2] = TotalPoints
							//array[3] = AssignmentID
							array[1]=temp[0];
							array[2]=temp[1];
							array[3]=temp[2];
							name.setText(array[1]);
							points.setText(array[2]);
						}}
					});
					

					JButton update = new JButton("Update");
					update.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub.
							String assignmentName = name.getText();
							String assignmentPoints = points.getText();


							Connection con = DatabaseConnector.getConnection();
							String SQL = "UPDATE Assignment SET Name = ?, TotalPoints = ? WHERE AssignmentID = ?";

							PreparedStatement pstmt;
							try {
								pstmt = con.prepareStatement(SQL);
								pstmt.setString(1, assignmentName);
								pstmt.setString(2, assignmentPoints);
								pstmt.setString(3, array[3]);
								
								pstmt.execute();
							} catch (SQLException exception) {
								// TODO Auto-generated catch-block stub.
								exception.printStackTrace();
							}
						}

					});

					cs.gridx = 0;
					cs.gridy = 6;
					cs.gridwidth = 1;
					panel.add(update, cs);
					JButton cancel = new JButton("Cancel");
					cancel.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							name.setText(array[1]);
							points.setText(array[2]);
						}
					});
					cs.gridx = 1;
					cs.gridy = 6;
					cs.gridwidth = 1;
					panel.add(cancel, cs);
					
					pop.add(panel);
					pop.pack();
					pop.setVisible(true);
				} catch (SQLException exception) {
					// TODO Auto-generated catch-block stub.
					exception.printStackTrace();
				}
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Update grade");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFrame pop = new JFrame("Update grade");

				JPanel panel = new JPanel(new GridBagLayout());
				GridBagConstraints cs = new GridBagConstraints();

				cs.fill = GridBagConstraints.HORIZONTAL;

				final JComboBox comboBox = new JComboBox();
				final JComboBox comboBox2 = new JComboBox();
				final JComboBox comboBox3 = new JComboBox();
				final JComboBox comboBox4 = new JComboBox();
				final String[] array = new String[4];
				

				try {
					Connection con = DatabaseConnector.getConnection();
					String SQL = "SELECT Name, SectionNumber, CourseListing FROM Class "
							+ "WHERE ClassID IN (SELECT ClassID FROM Enrolled WHERE StudentID = ?)";

					PreparedStatement pstmt = con.prepareStatement(SQL);
					pstmt.setString(1, String.valueOf(Main.userID));
					ResultSet rs = pstmt.executeQuery();

					DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
					comboBox.setModel(comboBoxModel);

					while (rs.next()) {
						comboBoxModel.addElement(rs.getString("CourseListing") + ": " + rs.getString("Name") + ": "
								+ rs.getString("SectionNumber"));
					}
					comboBox.setSelectedItem(null);
					cs.gridx = 0;
					cs.gridy = 0;
					cs.gridwidth = 2;
					panel.add(comboBox, cs);

					comboBox.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							String item = comboBox.getSelectedItem().toString();

							String[] temp = item.split(": ");
							// System.out.println(array[1]);

							try {
								Connection con = DatabaseConnector.getConnection();
								String SQL = "SELECT Name, CategoryID FROM Category "
										+ "WHERE ClassID IN (SELECT ClassID FROM Class WHERE Name = ? "
										+ "AND SectionNumber = ?)";

								PreparedStatement pstmt = con.prepareStatement(SQL);
								pstmt.setString(1, temp[1]);
								pstmt.setString(2, temp[2]);
								ResultSet rs = pstmt.executeQuery();

								DefaultComboBoxModel aModel = new DefaultComboBoxModel();
								comboBox2.setModel(aModel);

								while (rs.next()) {
									aModel.addElement(rs.getString("Name")+ ": " + rs.getInt("CategoryID"));
								}

							} catch (SQLException exception) {
								// TODO Auto-generated catch-block stub.
								exception.printStackTrace();
							}

						}

					});

					cs.gridx = 0;
					cs.gridy = 1;
					cs.gridwidth = 2;
					panel.add(comboBox2, cs);
					
					comboBox2.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							if(comboBox2.getSelectedItem()!=null){
							String item = comboBox2.getSelectedItem().toString();

							String[] temp = item.split(": ");
							//array[0] = catID
							array[0] = temp[1];
							try {
								Connection con = DatabaseConnector.getConnection();
								String SQL = "SELECT Name, TotalPoints, AssignmentID FROM Assignment "
										+ "WHERE CategoryID = ?";

								PreparedStatement pstmt = con.prepareStatement(SQL);
								pstmt.setString(1, temp[1]);
								ResultSet rs = pstmt.executeQuery();

								DefaultComboBoxModel aModel = new DefaultComboBoxModel();
								comboBox3.setModel(aModel);

								while (rs.next()) {
									aModel.addElement(rs.getString("Name")+ ": " + rs.getInt("TotalPoints")+": "+rs.getInt("AssignmentID"));
								}

							} catch (SQLException exception) {
								exception.printStackTrace();
							}
						}}
					});
					cs.gridx = 0;
					cs.gridy = 2;
					cs.gridwidth = 2;
					panel.add(comboBox3, cs);
					comboBox3.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							if(comboBox3.getSelectedItem()!=null){
							String item = comboBox3.getSelectedItem().toString();

							String[] temp = item.split(": ");
							//array[1] = Name
							//array[2] = TotalPoints
							//array[3] = AssignmentID
							array[1]=temp[0];
							array[2]=temp[1];
							array[3]=temp[2];
							try {
								Connection con = DatabaseConnector.getConnection();
								String SQL = "SELECT GradeID, Points FROM Grade "
										+ "WHERE AssignmentID = ? and StudentID = ?";

								PreparedStatement pstmt = con.prepareStatement(SQL);
								pstmt.setString(1, array[3]);
								pstmt.setLong(2, Main.userID);
								ResultSet rs = pstmt.executeQuery();

								DefaultComboBoxModel aModel = new DefaultComboBoxModel();
								comboBox4.setModel(aModel);

								while (rs.next()) {
									aModel.addElement(rs.getString("GradeID")+ ": " + array[1] + ": " + rs.getInt("Points")+": "+ array[2]);
								}

							} catch (SQLException exception) {
								exception.printStackTrace();
							}
						}}
					});
					
					cs.gridx = 0;
					cs.gridy = 3;
					cs.gridwidth = 2;
					panel.add(comboBox4, cs);
					
					final JTextField points = new JTextField(array[0]);
					points.addKeyListener(new LengthControler(points));

					cs.gridx = 0;
					cs.gridy = 4;
					cs.gridwidth = 2;
					panel.add(points, cs);
					 
					comboBox4.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							if(comboBox2.getSelectedItem()!=null){
							String item = comboBox4.getSelectedItem().toString();

							String[] temp = item.split(": ");
							//array[0] = Points
							//array[1] = GradeID
							array[0]=temp[2];
							array[1]=temp[0];
							points.setText(array[0]);
						}}
					});
					

					JButton update = new JButton("Update");
					update.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub.
							String grade = points.getText();


							Connection con = DatabaseConnector.getConnection();
							String SQL = "UPDATE Grade SET  Points = ? WHERE GradeID = ?";

							PreparedStatement pstmt;
							try {
								pstmt = con.prepareStatement(SQL);
								pstmt.setString(1, grade);
								pstmt.setString(2, array[1]);
								
								pstmt.execute();
							} catch (SQLException exception) {
								// TODO Auto-generated catch-block stub.
								exception.printStackTrace();
							}
						}

					});

					cs.gridx = 0;
					cs.gridy = 5;
					cs.gridwidth = 1;
					panel.add(update, cs);
					JButton cancel = new JButton("Cancel");
					cancel.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							points.setText(array[0]);
						}
					});
					cs.gridx = 1;
					cs.gridy = 5;
					cs.gridwidth = 1;
					panel.add(cancel, cs);
					
					pop.add(panel);
					pop.pack();
					pop.setVisible(true);
				} catch (SQLException exception) {
					// TODO Auto-generated catch-block stub.
					exception.printStackTrace();
				}
			}
		});
		menu.add(menuItem);

	}

	public JMenu getMenu() {
		return this.menu;
	}

}
