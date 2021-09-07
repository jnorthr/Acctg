package com.jim.toolkit.gui;

// MrHaki' isimple sample for GUI for Address object
import groovy.transform.*;

import groovy.swing.SwingBuilder 
import groovy.beans.Bindable 
import static javax.swing.JFrame.EXIT_ON_CLOSE 
import java.awt.*
import javax.swing.JFrame;

import groovy.util.logging.Slf4j;
import org.slf4j.*
/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// notice every obj here is a String
@Bindable
class Address { 
    	String street, number, city
    	String toString() { "address[street=$street,number=$number,city=$city]" }
} // end of class


/** 
 * MrHakiGUI class description
 *
 * This is a sample MrHakiGUI with all bits needed to do a gradle project
 *
 */ 
 @Canonical 
 public class MrHakiGUI
 {  
	// make one   
	def address = new Address(street: 'Evergreen Terrace', number: '742', city: 'Springfield')
   
	/** 
	* Variable name of current class.
	*/  
	String classname = "MrHakiGUI";

	// build GUI   
	def swingBuilder = new SwingBuilder()

   /** 
    * Method to do the GUI.
    * 
    * @return void
    */     
    public void load()
    {
		// edt method makes sure UI is build on Event Dispatch Thread.
		swingBuilder.edt 
		{  
			lookAndFeel 'nimbus'  // Simple change in look and feel.
    		frame(title: 'Address', size: [350, 230], show: true, locationRelativeTo: null, defaultCloseOperation: JFrame.EXIT_ON_CLOSE) 
    		{
		        borderLayout(vgap: 5)
         
				panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Enter your address:')])) 
        		{
            		tableLayout 
            		{
                		tr {
                    		td {
                        		label 'Street:'  // text property is default, so it is implicit.
                    		}
                    	td {
                        	textField address.street, id: 'streetField', columns: 20
                    	}
                	}
    	            tr {
	                    td {
                    	    label 'Number:'
                	    }
            	        td {
        	                textField id: 'numberField', columns: 5, text: address.number
    	                }
	                }
    	            tr {
	                    td {
                        	label 'City:'
                    	}
                	    td {
            	            textField id: 'cityField', columns: 20, address.city
        	            }
    	            }
	            } // end of table
        	} // end of panel        
         
	        panel(constraints: BorderLayout.SOUTH) 
    	    {
        	    button text: 'Save', actionPerformed: {
            	    //log.info address
            	}
            	button text: 'Exit', actionPerformed: {
                	System.exit(0);
            	}
        	} // end of panel
        
         
		        // Binding of textfield's to address object.
        		bean address,
            		street: bind { streetField.text },
            		number: bind { numberField.text },
            		city: bind { cityField.text }
    		}  // end of frame

		} // end of swingbuilder
    }  // end of method


        
   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    @Override
    public String toString()
    {
        return """classname=Loader
"""
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
        println "--- starting MrHakiGUI ---"
		MrHakiGUI obj = new MrHakiGUI();
		obj.load();
        println "--- the end of MrHakiGUI ---"
    } // end of main

} // end of class
