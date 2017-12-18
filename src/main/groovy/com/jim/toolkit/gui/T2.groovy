package com.jim.toolkit.gui;

import groovy.swing.SwingBuilder 
import groovy.beans.Bindable 
import static javax.swing.JFrame.EXIT_ON_CLOSE 
import java.awt.*
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.Date;
import java.text.SimpleDateFormat
import java.awt.event.KeyEvent
import com.jim.toolkit.Cell;

public class T2
{
  
   /** 
    * Variable name of current class.
    */  
    String classname = "T2";

    def d = new Date();

    //@Bindable
    def model = new Cell()
   
    def swingBuilder = new SwingBuilder()

    public void load()
    {
        swingBuilder.edt 
        {   // edt method makes sure UI is build on Event Dispatch Thread.
            lookAndFeel 'nimbus' //'nimbus'  // Simple change in look and feel.
            frame(title: 'Cell', size: [320, 360], show: true, locationRelativeTo: null,defaultCloseOperation: JFrame.EXIT_ON_CLOSE) 
            {
                borderLayout(vgap: 5)
         
                panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Enter your details:')])) 
                {
                    tableLayout 
                    {
            	        tr{
                            td {
                                hbox{
                           	        label 'Row Id:'  // text property is default, so it is implicit.
                                } // end of hbox
                            }
	                    	td {
    	                		hbox{
	    	                    	textField id: 'idField', columns: 8  // , text: Transaction.id
	    	                    	label ' '
                            		button(id:"find", text:"Find", actionPerformed: { println "find.."});
	                    	    } // end of hbox
                    	   } // end of td
                        } // end of tr
                    } // end of tableLayout
             
                } // end of panel
         
                panel(constraints: BorderLayout.SOUTH) 
                {
                    button text: 'Save', actionPerformed: {
                        println "... Saved"  //Cell
                        //Transaction.say();
                    }
                    button text: 'Exit', actionPerformed: {
                        System.exit(0);
                    }
                } // end of panel
         
                // Binding of textfield's to Transaction object.
                bean Cell,
                    id: bind { idField.text() }
                    //println "... bean Transaction... ${flagField} ?"
            } // end of frame
    
            //boolean ok = (Transaction.flag.trim().toLowerCase().startsWith('t') ) ? true : false;
            //flagField.setSelected(ok);
        } // end of swingBuilder
    } // end of load()


   /** 
    * Method to print audit log.
    * 
    * @param the text to be said
    * @return void
    */     
    public void say(txt)
    {
        println txt;
    }  // end of method


   // ======================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */     
    public static void main(String[] args)
    {
        println "--- starting T2 ---"
        T2 obj = new T2();
        obj.load();
        println "--- the end of T2 ---"
    } // end of main

} // end of class
