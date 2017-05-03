package com.company;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Created by iassona on 4/21/2017.
 */
public class GRFrame extends JFrame{

    private JPanel panelOnScreen;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem menuItem;
    PreparedStatement pstmt;
    ResultSet rs;
    
    int userID;

    public GRFrame(String title) {
        super(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1024, 768);
        
        menuBar = new JMenuBar();
        
        menu = new JMenu("Menu");
        menuBar.add(menu);
        
        menuItem = new JMenuItem("Show student grades");
        menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub.
				System.out.println("menu pressed User: " + userID);
				getContentPane().removeAll();
				
				try {
				Connection con = DatabaseConnector.getConnection();
				String SQL = "EXEC studentAssignments " + userID;
				
				PreparedStatement pstmt = con.prepareStatement(SQL);
				ResultSet rs = pstmt.executeQuery();
				
				makeTable(rs);
				
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
				String SQL = "EXEC studentGradeRoster " + userID;
				
				PreparedStatement pstmt = con.prepareStatement(SQL);
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
        
        menuItem = new JMenuItem("Create class");
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
				"WHERE NOT ClassID IN (SELECT ClassID FROM Enrolled WHERE StudentID = " + userID + ")";
				
				PreparedStatement pstmt = con.prepareStatement(SQL);
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
						"(SELECT ClassID FROM Class WHERE Name = '" + array[1] + 
						"' AND SectionNumber = " + array[2] + "), " + userID + ")";
						
						PreparedStatement pstmt;
						try {
							pstmt = con.prepareStatement(SQL);
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
				JFrame pop = new JFrame("Add class");

				JPanel panel = new JPanel(new GridBagLayout());
				GridBagConstraints cs = new GridBagConstraints();
				
				cs.fill = GridBagConstraints.HORIZONTAL;

				JComboBox comboBox = new JComboBox();
				JComboBox comboBox2 = new JComboBox();
				
				try {
				Connection con = DatabaseConnector.getConnection();
				String SQL = "SELECT Name, SectionNumber, CourseListing FROM Class " +
				"WHERE ClassID IN (SELECT ClassID FROM Enrolled WHERE StudentID = " + userID + ")";
				
				PreparedStatement pstmt = con.prepareStatement(SQL);
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
						"WHERE ClassID IN (SELECT ClassID FROM Class WHERE Name = '" + array[1] + 
						"' AND SectionNumber = " + array[2] + ")";
						
						PreparedStatement pstmt = con.prepareStatement(SQL);
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
				
				cs.gridx = 0;
				cs.gridy = 3;
				cs.gridwidth = 1;
				panel.add(name, cs);
				
				JTextField points = new JTextField("Total Points");
				
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
						"(SELECT CategoryID FROM Category WHERE Name = '" + category +
						"' AND ClassID IN (SELECT ClassID FROM Class WHERE Name = '" + array[1] + 
						"' AND SectionNumber = " + array[2] + ")), '" + assignmentName +
						"', " + assignmentPoints + ")";
						
						PreparedStatement pstmt;
						try {
							pstmt = con.prepareStatement(SQL);
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
        //setVisible(true);
        //DatabaseConnector.disconnect();
    }


    public void setUser(int userID) {
    	this.userID = userID;
    }
    
    public void makeTable(ResultSet rs) {
    	JTable table = new JTable();
		DefaultTableModel model = new DefaultTableModel();
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//table.setFillsViewportHeight(true);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		try {
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();
		
		String[] columnNames = new String[columnCount];
		
		for (int i = 0; i < columnCount; i++) {
			columnNames[i] = meta.getColumnName(i + 1);
		}
		
		model.setColumnIdentifiers(columnNames);
		
		while (rs.next()) {
			Object[] data = new Object[columnCount];
			for (int i = 0; i < columnCount; i++) {
				data[i] = rs.getString(columnNames[i]);
			}
			model.addRow(data);
		}
		add(scroll);
		setVisible(true);
		} catch (SQLException exception) {
			// TODO Auto-generated catch-block stub.
			exception.printStackTrace();
		}
    }
}
