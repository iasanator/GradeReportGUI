package com.company;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
				
				JTable table = new JTable();
				DefaultTableModel model = new DefaultTableModel();
				table.setModel(model);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				//table.setFillsViewportHeight(true);
				JScrollPane scroll = new JScrollPane(table);
				scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				
				try {
				Connection con = DatabaseConnector.getConnection();
				String SQL = "EXEC studentAssignments " + userID;
				
				PreparedStatement pstmt = con.prepareStatement(SQL);
				ResultSet rs = pstmt.executeQuery();
				
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
		}});
        
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Show student grade roster");
        menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub.
				System.out.println("menu pressed User: " + userID);
				getContentPane().removeAll();
				
				JTable table = new JTable();
				DefaultTableModel model = new DefaultTableModel();
				table.setModel(model);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				//table.setFillsViewportHeight(true);
				JScrollPane scroll = new JScrollPane(table);
				scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				
				try {
				Connection con = DatabaseConnector.getConnection();
				String SQL = "EXEC studentGradeRoster " + userID;
				
				PreparedStatement pstmt = con.prepareStatement(SQL);
				ResultSet rs = pstmt.executeQuery();
				
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
		}});
        
        
        menu.add(menuItem);
        this.setJMenuBar(menuBar);
        //setVisible(true);
        //DatabaseConnector.disconnect();
    }


    public void setUser(int userID) {
    	this.userID = userID;
    }

}
