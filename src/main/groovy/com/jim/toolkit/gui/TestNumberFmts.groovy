package com.jim.toolkit.gui;

import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.Box;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TestNumberFmts {
   /** 
    * Variable name of current class.
    */  
    String classname = "TestNumberFmts";


    public static void main(String[] args) throws Exception 
    {
        //System.setProperty("java.awt.headless", "true");
  
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        BigDecimal bd = new BigDecimal(-364565.14)
        System.out.println(df.format(bd));
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMMMM, yyyy")
          
        Box form = Box.createVerticalBox();
        form.add(new JLabel("Name:"));
        form.add(new JTextField("User Name"));

        form.add(new JLabel("Amount:"));
        form.add(new JTextField(df.format(bd)));

//-----------
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameTextField = new JTextField();

        form.add(usernameLabel);
        form.add(usernameTextField);


        // Register a KeyListener for the text field. Using the KeyAdapter class
        // allow us implement the only key listener event that we want to listen,
        // in this example we use the keyReleased event.
        usernameTextField.addKeyListener(new KeyAdapter() 
        {
            public void keyReleased(KeyEvent e) 
            {
                JTextField textField = (JTextField) e.getSource();
                String text = textField.getText();
                def ch = text.charAt(text.length()-1);
                if (ch in 'a'..'z' || '0'..'9' || '.')
                {
                    usernameTextField.setText(text.toUpperCase());
                }
                else
                {
                    usernameTextField.setText("")   //text.substring(0,text.length()-2))
                } // end of else
            } // end of keyReleased
        }); // end of addKeyListener

//-----------

        form.add(new JLabel("Birthday:"));
        JFormattedTextField birthdayField = new JFormattedTextField(new SimpleDateFormat("MM/dd/yy"));
        birthdayField.setValue(new Date());
        form.add(birthdayField);

        form.add(new JLabel("Age:"));
        form.add(new JFormattedTextField(new Integer(32)));

        form.add(new JLabel("Hairs on Body:"));
        JFormattedTextField hairsField = new JFormattedTextField(new DecimalFormat("###,###"));
        hairsField.setValue(new Integer(100000));
        form.add(hairsField);

        form.add(new JLabel("Phone Number:"));
        JFormattedTextField phoneField = new JFormattedTextField(new MaskFormatter("(###)###-####"));
        phoneField.setValue("(314)888-1234");
        form.add(phoneField);

        JFrame frame = new JFrame("User Information");
        frame.getContentPane().add(form);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setVisible(true);
    } // end of main
} // end of class