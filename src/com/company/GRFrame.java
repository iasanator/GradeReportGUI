package com.company;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
				System.out.println("menu pressed");
				
				
				String[] columnNames = {"Type", "Assignment Name", "Earned", "Total Points", "Grade"};
				
				JTable table = new JTable();
				DefaultTableModel model = new DefaultTableModel();
				model.setColumnIdentifiers(columnNames);
				table.setModel(model);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				table.setFillsViewportHeight(true);
				JScrollPane scroll = new JScrollPane(table);
				scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				
				String connectionUrl = "jdbc:sqlserver://golem.csse.rose-hulman.edu:1433;" + 
		    			"databaseName=GradeReport_Data;user=GRuser;password=abc123;";
				try {
				Connection con = DriverManager.getConnection(connectionUrl);
				String SQL = "EXEC studentAssignments 1, 2";
				
				String type;
				String assignment;
				String earned;
				String points;
				String grade;
	
				
				PreparedStatement pstmt = con.prepareStatement(SQL);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					type = rs.getString("Type");
					assignment = rs.getString("Assignment Name");
					earned = rs.getString("Earned");
					points = rs.getString("Total Points");
					grade = rs.getString("Grade");
					model.addRow(new Object[]{type, assignment, earned, points, grade});
				}
				add(scroll);
				setVisible(true);
				
				} catch (SQLException exception) {
					// TODO Auto-generated catch-block stub.
					exception.printStackTrace();
				}
				
				
				
		}});
        
        menu.add(menuItem);
        this.setJMenuBar(menuBar);
        //setVisible(true);
    }




}
