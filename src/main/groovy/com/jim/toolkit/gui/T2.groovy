package com.jim.toolkit.gui;

import groovy.swing.SwingBuilder 
import groovy.beans.Bindable 
import static javax.swing.JFrame.EXIT_ON_CLOSE 
import java.awt.*
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.Date;
import java.text.SimpleDateFormat
import java.awt.event.KeyEvent
import com.jim.toolkit.Cell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import com.jim.toolkit.tools.ClientSupport;
import com.jim.toolkit.tools.ClientLoader;
import com.jim.toolkit.database.H2TableMethods;

import javax.swing.JOptionPane;

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

/** 
 * ClientTransaction class description
 *
 * This is code with all fields needed to set up a Bean for our GUI
 *
 */ 
@Slf4j
@Bindable
class ClientTransaction 
{ 
    String id, client="", reason=""
    Boolean flag    
    Boolean usage = false;
    int seq = 2;
    int ccy = 1; // euros

    String toString() { "Transaction[id=$id,ccy=$ccy, flag=$flag, reason=$reason, client=$client, usage=$usage,seq=$seq]" }

   /** 
    * Method to produce and write a client into the H2 table named 'clients'
    * 
    * @return void
    */     
    String say(boolean usage) 
    { 
	    ClientSupport obj = new ClientSupport();
	    if (id.size() < 1) { id = "0"; }
	    int idn = id as Integer    
	    def ma = []
	    ma.push(idn) // Id number
	    ma.push(ccy) // Internal Currency code like 1 for EUR, or 3 for USD
	    ma.push(flag) // true if income or false if expenses
	    ma.push(client)
	    ma.push(reason)
	    String msg = "Id ";
	    if (!usage) { msg += obj.insert(ma); msg+=" added" }
	    if (usage) {  msg += obj.update(ma); msg+= " updated";}
	    return msg
    } // end of say()


   /** 
    * Method to remove a client from the H2 table named 'clients'
    * 
    * @return void
    */     
    String delete(int idn) 
    { 
	    ClientSupport obj = new ClientSupport();
	    String msg = "";
		msg += obj.delete(idn); 
		return msg;
    } // end of method    

} // end of class

   
/** 
 * T2 class description
 *
 * This is code with all fields needed to operate a GUI for our Bean for our GUI
 */ 
@Slf4j
public class T2
{
   /** 
    * Variable name of current class.
    */  
    String classname = "T2";

   /** 
    * establish a default set of fields.
    */  
    def ClientTransaction = new ClientTransaction(id:0, ccy:1, client:"", reason:"", flag:false, usage:false, seq:2)

   /** 
    * Variable set to true if logging printouts are needed or false if not
    */  
    Boolean logFlag = false;

   /** 
    * Handle to open another scrolling display of current client data 
    */  
    JFrame win = new ClientDisplay();

   /** 
    * Handle to container for this GUI 
    */  
    def swingBuilder = new SwingBuilder()

   /** 
    * Method to make GUI ready for next event for this GUI 
    */  
    public reset()
    {
		// prepare TXN fields back to original settings
		ClientTransaction.id = "";
        ClientTransaction.ccy = 1;
		ClientTransaction.client = "";
		ClientTransaction.reason = "";
		ClientTransaction.flag = false; 

		// update the Client Display with latest Ordering choice by user
		win.loader(ClientTransaction.seq);

		// disable text fields until 'Find' button opens them again
		swingBuilder.clientField.setEditable(false)
		swingBuilder.clientField.setEnabled(false)

		swingBuilder.reasonField.setEditable(false)
		swingBuilder.reasonField.setEnabled(false)
		
		swingBuilder.flagField.setEnabled(false)
		swingBuilder.saver.setEnabled(false)
		swingBuilder.delete.setEnabled(false)						

		// reset GUI text fields and radio buttons back to blank
		swingBuilder.idField.text = "";
		swingBuilder.clientField.text = "";
		swingBuilder.reasonField.text = "";
		swingBuilder.flagField.setSelected(false);
		swingBuilder.flagField.text = "Expense";
		swingBuilder.saver.text = "Add";
		swingBuilder.ft.text = ' ';            			          		         			          		

		// re-allow find field and it's button
		swingBuilder.find.setEnabled(true)
		swingBuilder.idField.setEditable(true)
		swingBuilder.idField.setEnabled(true)
		swingBuilder.idField.requestFocus();
		swingBuilder.idField.grabFocus()
	} // end of reset

	
   /** 
    * Method to construct GUI  
    */  
    public void load()
    {
        win.setVisible(true);
	    win.loader(ClientTransaction.seq);        

        swingBuilder.edt 
        {   // edt method makes sure UI is build on Event Dispatch Thread.
            lookAndFeel 'nimbus' //'nimbus'  // Simple change in look and feel.
            frame(title: 'Client', size: [400, 380], show: true, locationRelativeTo: null, 
            	  defaultCloseOperation: JFrame.EXIT_ON_CLOSE) 
            {
                borderLayout(vgap: 5)
         
                panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Enter your details:')])) 
                {
                    tableLayout 
                    {
            	        tr{
                            td {
                                hbox{
                           	        label 'Client Id:'  // text property is default, so it is implicit.
                                } // end of hbox
                            }
	                    	td {
    	                		hbox{
	    	                    	textField id: 'idField', columns: 3 
	    	                    	label ' '
	                            	button(id:"find", text:"Find");
	    	                    	label ' '
	    	                    	label id:'ft', text:' '
	                    	    } // end of hbox
                    	   } // end of td
                        } // end of tr
                        
            	        tr{
                            td {
                                hbox{
                           	        label 'Client:'  // text property is default, so it is implicit.
                                } // end of hbox
                            }
	                    	td {
	    	                    	textField id: 'clientField', columns: 22, enabled:false  
                    	   } // end of td
                        } // end of tr
                        
                        
            	        tr{
                            td {
                                hbox{
                           	        label 'Usage :'  
                                } // end of hbox
                            }
	                    	td {
                        	   checkBox id: 'flagField', text:'Expense', enabled:false 
                    	   } // end of td
                        } // end of tr
                        
            	        tr{
                            td {
                                hbox{
                           	        label 'Reason :'  
                                } // end of hbox
                            }
	                    	td {
	    	                    	textField id: 'reasonField', columns: 22, enabled:false  
                    	   } // end of td
                        } // end of tr


                        tr{
                            td {
                                hbox{
                                    label ' '  
                                } // end of hbox
                            }
                        } // end of tr


                        tr{
                            td {
                                hbox{
                                    label 'Currency :'  
                                } // end of hbox
                            }
                            td {
                                // This will work:
                                ccyGroup = buttonGroup(id:"cg");
                                hbox{
                                    radioButton(id:"gbp", text:"GBP  ", buttonGroup:ccyGroup);
                                    radioButton(id:"eur", text:"Eur  ", buttonGroup:ccyGroup, selected:true);
                                    radioButton(id:"usd", text:"USD", buttonGroup:ccyGroup);
                                }
                            } // end of td
                        } // end of tr
                                                
                    } // end of tableLayout
             
                } // end of panel
         
         
         		// put these components in the SOUTH gui area
                panel(constraints: BorderLayout.SOUTH, border: compoundBorder([emptyBorder(10), titledBorder('Enter your choice of actions :') ] ) ) 
                {
                  vbox{                  	
                	hbox{

						// logic to restart GUI logic
		                button text: 'Reset', actionPerformed: 
	    	            {
    	                    swingBuilder.message.text = "";
    	    	            reset();
        	    	    } // end of button
        	        
						// logic run when the 'Add or Update' button is  clicked
    	                button id:'saver', enabled:false, text: 'Add', actionPerformed: {
							swingBuilder.find.setEnabled(true)
							
							ClientTransaction.id = swingBuilder.idField.text;
							ClientTransaction.client = swingBuilder.clientField.text;
							ClientTransaction.reason = swingBuilder.reasonField.text;
							ClientTransaction.flag = swingBuilder.flagField.isSelected();""
        	                ClientTransaction.ccy = 1;  // assume Euro CCY
            	            if (swingBuilder.usd.isSelected()) { ClientTransaction.ccy = 3; }
                	        if (swingBuilder.gbp.isSelected()) { ClientTransaction.ccy = 2;}

	                        String xxx = ClientTransaction.say(ClientTransaction.usage);
    	                    swingBuilder.message.text = xxx;
							reset();
            	        } // end of actionPerformed
                    
                    
					// logic run when the 'Delete' button is  clicked
		                button id:"delete", text:"Delete", enabled:false, actionPerformed: 
		                {
        	                say "... Delete"  
							swingBuilder.find.setEnabled(true)
							swingBuilder.idField.setEditable(true)
							swingBuilder.idField.setEnabled(true)
							swingBuilder.idField.requestFocus();
							swingBuilder.idField.grabFocus();
						
							def j = swingBuilder.idField.text as Integer;
    	                    String xxx = ClientTransaction.delete(j);
							reset();
            	            swingBuilder.message.text = xxx;
                	    } // end of actionPerformed

						// logic to end this job when the 'Exit' button is  clicked
		                button text: 'Exit', actionPerformed: 
	    	            {
    	    	            System.exit(0);
        	    	    } // end of Exit
        	        
                	} // end of hbox 


                    hbox{
                        // logic to populate H2 clients table from external file
                        button text: 'Load', actionPerformed: 
                        {
                            ClientLoader obj = new ClientLoader()
                            def ct = obj.loadClients();

                            swingBuilder.message.text = ct+" clients loaded";
                            reset();
                        } // end of button

                        // logic to copy H2 clients table to an external file
                        button text: 'Save', actionPerformed: 
                        {
                            ClientLoader obj = new ClientLoader()
                            def ct = obj.saveClients();

                            swingBuilder.message.text = ct+" clients saved";
                            reset();
                        } // end of button

                    } // end of hbox
                    

	                hbox{
		    	    	label id:'message', text:' '
        	        } // end of hbox

            		} // end of vbox 
        		} // end of panel
         
         
                // Binding of textfield's to Transaction object.
                bean ClientTransaction,
                    id: bind { idField.text() }
                    client: bind { clientField.text() }
                    reason: bind { reasonField.text() }
                    flag: bind { flagField.isSelected() }

            } // end of frame
    
			// ----------------------------------------------------------------------------------
			// end of layout
			// ----------------------------------------------------------------------------------
			
			// logic run when 'Find' button is clicked
			find.addActionListener(new ActionListener()
			{
        			public void actionPerformed(ActionEvent e) 
        			{
            			// Do something here...
            			say "gui.find hit to find "+swingBuilder.idField.text  

						// if blank id, make it zero
            			if (swingBuilder.idField.text.size() < 1 ) 
            			{ 
            				swingBuilder.idField.text = "0"
            				//JOptionPane.showMessageDialog(null, "Can not use Find without a number", "Missing Number", JOptionPane.ERROR_MESSAGE);
							//swingBuilder.idField.requestFocus();
							//swingBuilder.idField.grabFocus()
            				//return
            			} // end of if
            			

						def j = 0;
						try
						{
							j = swingBuilder.idField.text as Integer
						}
						catch (Exception x)
						{
							println "... bad news:"+x;
							j=0;
						}
						swingBuilder.idField.setEditable(false)
						swingBuilder.idField.setEnabled(false)
                        swingBuilder.message.text = "";

						swingBuilder.clientField.setEditable(true)
						swingBuilder.clientField.setEnabled(true)
						swingBuilder.reasonField.setEditable(true)
						swingBuilder.reasonField.setEnabled(true)
						swingBuilder.flagField.setEnabled(true)
						swingBuilder.saver.setEnabled(true)


						ClientSupport cs = new ClientSupport();

						// check if this ID (int j) exists in 'clients' table
						ClientTransaction.usage = cs.getH2(j);
						
						// row id found, so update or delete
						if (ClientTransaction.usage)
						{
							say "... find says to update"
	                		String client = cs.getClient(j);
							swingBuilder.clientField.text = client;            			          		
	                		String reason = cs.getReason(j);
							swingBuilder.reasonField.text = reason;            			          		
							swingBuilder.saver.text = "Update";   
							swingBuilder.delete.setEnabled(true);
							swingBuilder.ft.text = 'found';

							swingBuilder.flagField.setSelected( cs.getFlag(j) );  	
							swingBuilder.flagField.text = (swingBuilder.flagField.isSelected()) ? "Income" : "Expense";  			          		           			          		         			          		
							
                            H2TableMethods h2tm = new H2TableMethods();
							// figure out currency here
							def cc = cs.getCurrency(j) // 1,2 or 3 for 978, 826 or 840
							//def x = h2tm.getCode(cc)   // EUR, GBP, USD
							
							//say "... client FIND of $j found cc=${cc} and x=${x}"
							
							boolean bb = (cc == 1)?true:false;
							swingBuilder.eur.setSelected( bb );  	

							bb = (cc==2)?true:false;
							swingBuilder.gbp.setSelected( bb );
							  	
							bb = (cc==3)?true:false;
							swingBuilder.usd.setSelected( bb ); 
							//if (x=='GBP') 	
						} // end of if

						else // add this ID as it's new
						{
							say "... find says to add: "+ClientTransaction.usage;
							swingBuilder.ft.text = 'not found - add ?';            			          		         			          		
							swingBuilder.saver.text = "Add";            			          		
							swingBuilder.clientField.text = "";            			          		
							swingBuilder.reasonField.text = "";          
							swingBuilder.flagField.text = "Income"  			          		
							swingBuilder.flagField.setSelected(true);  	
							swingBuilder.delete.setEnabled(false)		          		
						} // end of else
						
						swingBuilder.clientField.requestFocus();
						swingBuilder.clientField.grabFocus()
						swingBuilder.find.setEnabled(false)
        			} // end of addActionPerformed
    		}); // end of addActionListener


			// -----------------------------------------------------------
			// logic run when 'Flag' choice is made			
        	flagField.addActionListener(new ActionListener()
            {
		    		public void actionPerformed(ActionEvent actionEvent) 
      				{
        				say "... actionListener starting"  // for :"+id;
				        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
    	    			say "... isSelected :"+actionEvent.getSource().isSelected();

				        String key = actionEvent.getActionCommand().toString();
    	    			boolean selected = abstractButton.getModel().isSelected();
        				ClientTransaction.flag = selected;
        				if (selected) 
        				{ 
        					swingBuilder.flagField.text = "Income" 
        				} 
        				else 
        				{ 
        					swingBuilder.flagField.text = "Expense" 
        				} // end of else
        				
        				say "\n... actionListener ended\n";        
      				} // end of action
            	}); // end of addActionListener        

			// -----------------------------------------------------------
			// logic run when choice is made to use GBP as currency			
	        gbp.addActionListener(new ActionListener()
    	    {
        		public void actionPerformed(ActionEvent e) 
            	{
                	// Do something here...
                    ClientTransaction.ccy = 2;
                    say "gui.gbp hit"
                }
            }); // end of listener
    

			// -----------------------------------------------------------
			// logic run when choice is made to use GBP as currency			
	        eur.addActionListener(new ActionListener()
    	    {
        		public void actionPerformed(ActionEvent e) 
            	{
                	// Do something here...
                    ClientTransaction.ccy = 1;
                    say "gui.eur hit"
                }
            }); // end of listener
    

			// -----------------------------------------------------------
			// logic run when choice is made to use GBP as currency			
	        usd.addActionListener(new ActionListener()
    	    {
        		public void actionPerformed(ActionEvent e) 
            	{
                	// Do something here...
                    ClientTransaction.ccy = 3;
                    say "gui.usd hit"
                }
            }); // end of listener
    
    		find.setMnemonic(KeyEvent.VK_F);
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
    	if (logFlag) { log.info txt; }
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

//say "... saver:ClientTransaction.flag="+ClientTransaction.flag+" isSelected()="+swingBuilder.flagField.isSelected() 
//say "... ClientTransaction (usage:${ClientTransaction.usage}) ="+ClientTransaction.toString();

