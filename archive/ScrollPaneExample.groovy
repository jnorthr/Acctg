package com.jim.toolkit.gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import groovy.util.logging.Slf4j;
import org.slf4j.*
@Slf4j
public class ScrollPaneExample
{
    String[] data = ["001","25-12-2017","Expense","123.45","5","No","Cooking","123.45"]
    String[] col = ["Id","Date","Type","Amount","Client","Y/N","Reason","Balance"];
    JFrame frame = null;
    JPanel panel = new JPanel();
    JScrollPane pane;    
	JTextArea textArea;

    //JTable table = new JTable(data,col);
    //JTableHeader header = null;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int height = screenSize.height * ( 3 / 4 );
    int width = screenSize.width * ( 3 / 4 ); 

    public ScrollPaneExample()
    {       
        frame = new JFrame("Creating a Scrollable JTable!");
        textArea = new JTextArea(5, 20);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);  //JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);  //AUTO_RESIZE_OFF);
		textArea.setEditable(false);
		textArea.setText("Hi kids")
        //header = table.getTableHeader();
        //header.setBackground(Color.yellow);
        //table.setSize(790,590);
        pane = new JScrollPane(textArea);
        pane.setPreferredSize( new Dimension( 480, 480 ) );
        panel.add(pane);
        frame.add(panel);

        //frame.setPreferredSize(new Dimension(width, height));
        frame.setSize(500,500);
        //frame.setUndecorated(true);
        //frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    } // constructor

    public static void main(String[] args) 
    {
        new ScrollPaneExample();
    } // end of main
} // end of class
        