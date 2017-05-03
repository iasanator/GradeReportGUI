package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by iassona on 5/2/2017.
 */
public class GRFrameTeacher extends JFrame {

    private JPanel panelOnScreen;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem menuItem;
    PreparedStatement pstmt;
    ResultSet rs;


    public GRFrameTeacher() {
        super("GradeReport: Teacher Edition v0.000000001");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1024, 768);

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

        this.setJMenuBar(menuBar);
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
