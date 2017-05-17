package com.company;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
<<<<<<< HEAD
 * This is the parent class for both GRFrameStudent and GRFrameTeacher
 *
 * It contains the common methods that the frame uses to display information and inherits
 * the JFrame object.
 *
 * It also makes it so that the menu and menuBar are accessible from the Main class.
 *
=======
 * This is a super class for student and teacher frame. It implements
 * methods needed in both.
>>>>>>> 5d3c3b0a69e9a6a889b4e542495c159f2ea3dcda
 */
public class GRFrame extends JFrame{

	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem;

    public GRFrame(String title) {
        super(title);
    }
<<<<<<< HEAD


	/**
	 *
	 * This method takes a result set from a query and generates
	 * a spreadsheet of the data that fills the frame
	 *
	 * @param rs
	 */
	public void makeTable(ResultSet rs) {
=======
    
    /**
     * Makes table on GRframe with given data.
     * 
     * @param rs
     */
    public void makeTable(ResultSet rs) {
>>>>>>> 5d3c3b0a69e9a6a889b4e542495c159f2ea3dcda
    	JTable table = new JTable();
		DefaultTableModel model = new DefaultTableModel() {
			
			@Override
			public boolean isCellEditable(int i, int i1) {
				return false;
			}
			
		};
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
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
			exception.printStackTrace();
		}
    }
}
