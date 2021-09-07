package com.jim.toolkit.gui;

import groovy.swing.SwingBuilder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import groovy.transform.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
 
import groovy.util.logging.Slf4j;
import org.slf4j.*
/** 
 * RadioButtons class description
 *
 * This is a class to hold a list of Cell objects
 *
 */ 
 @Slf4j
 @Canonical 
 public class RadioButtons
 {
	/** 
	* Variable name of current class.
	*/  
	String classname = "RadioButtons";

	def swing = new SwingBuilder();

	public void load()
	{
    	def gui = swing.frame(size:[250,100],defaultCloseOperation: EXIT_ON_CLOSE) 
        {
        	panel(layout:new FlowLayout()) 
            {
                // This will work:
                myGroup = buttonGroup(id:"bg");
                hbox{
                    radioButton(id:"one", text:"One", buttonGroup:myGroup);
                    radioButton(id:"two", text:"Two", buttonGroup:myGroup, selected:true);
                    radioButton(id:"three", text:"Three", buttonGroup:myGroup);
                }
            } // end of panel

            one.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) 
                {
                    // Do something here...
                    log.info "gui.one hit"
                }
            }); // end of listener
    
            two.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) 
                {
                    // Do something here...
                    log.info "gui.two hit"
                }
            }); // end of listener
    
    
        three.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                // Do something here...
                log.info "gui.three hit"
            }
        }); // end of listener    
    } // end of gui

	gui.show()

    } // end of load


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return classname;
    }  // end of string

    
   // ======================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */     
    public static void main(String[] args)
    {
        println "--- starting RadioButtons ---"
        RadioButtons obj = new RadioButtons()
        obj.load();
        println "--- the end of RadioButtons ---"
    } // end of main

} // end of class

