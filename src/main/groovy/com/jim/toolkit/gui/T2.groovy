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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import com.jim.toolkit.tools.ClientSupport;
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

@Bindable
class ClientTransaction 
{ 
    String id, client, reason
    Boolean flag    
    Boolean usage = false;

    String toString() { "Transaction[id=$id,flag=$flag,reason=$reason,client=$client,usage=$usage]" }

   /** 
    * Method to produce and write a client into the H2 table named 'clients'
    * 
    * @return void
    */     
    void say(boolean usage) 
    { 
	    ClientSupport obj = new ClientSupport();
	    int idn = id as Integer    
	    def ma = []
	    ma.push(idn)
	    ma.push(flag)
	    ma.push(client)
	    ma.push(reason)
	    println "... ClientTransaction.say(boolean ${usage}) list="+ma;
	    if (!usage) { obj.insert(ma); }
	    if (usage) { obj.update(ma); }
    } // end of say()


   /** 
    * Method to remove a client from the H2 table named 'clients'
    * 
    * @return void
    */     
    void delete(int idn) 
    { 
	    ClientSupport obj = new ClientSupport();
		obj.delete(idn); 
    } // end of method    

} // end of class
   

public class T2
{
   /** 
    * Variable name of current class.
    */  
    String classname = "T2";

   /** 
    * establish a default set of fields.
    */  
    def ClientTransaction = new ClientTransaction(id:0, client:"", reason:"", flag:false, usage:false)

    def swingBuilder = new SwingBuilder()

    public void load()
    {
        swingBuilder.edt 
        {   // edt method makes sure UI is build on Event Dispatch Thread.
            lookAndFeel 'nimbus' //'nimbus'  // Simple change in look and feel.
            frame(title: 'Client', size: [480, 280], show: true, locationRelativeTo: null,defaultCloseOperation: JFrame.EXIT_ON_CLOSE) 
            {
                borderLayout(vgap: 5)
         
                panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), 
                	titledBorder('Enter your details:')])) 
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
	    	                    	textField id: 'clientField', columns: 22  
                    	   } // end of td
                        } // end of tr
                        
                        
            	        tr{
                            td {
                                hbox{
                           	        label 'Flag :'  
                                } // end of hbox
                            }
	                    	td {
                        	   checkBox id: 'flagField', text:'Expense' 
                    	   } // end of td
                        } // end of tr
                        
            	        tr{
                            td {
                                hbox{
                           	        label 'Reason :'  
                                } // end of hbox
                            }
	                    	td {
	    	                    	textField id: 'reasonField', columns: 22  
                    	   } // end of td
                        } // end of tr
                        
                        
                    } // end of tableLayout
             
                } // end of panel
         
                panel(constraints: BorderLayout.SOUTH) 
                {
	                button id:"reset", text:"Reset", actionPerformed: {
                        println "... Reset"  
						swingBuilder.find.setEnabled(true)
						swingBuilder.idField.setEditable(true)
						swingBuilder.idField.setEnabled(true)
						swingBuilder.idField.requestFocus();
						swingBuilder.idField.grabFocus()

						swingBuilder.clientField.setEditable(false)
						swingBuilder.clientField.setEnabled(false)
						swingBuilder.reasonField.setEditable(false)
						swingBuilder.reasonField.setEnabled(false)
						swingBuilder.flagField.setEnabled(false)
						swingBuilder.saver.setEnabled(false)
						swingBuilder.delete.setEnabled(false)
						

						swingBuilder.idField.text = "";
						swingBuilder.clientField.text = "";
						swingBuilder.reasonField.text = "";
						swingBuilder.flagField.setSelected(false);
						swingBuilder.flagField.text = "Expense";

						swingBuilder.saver.text = "Add";
						swingBuilder.ft.text = ' ';            			          		         			          		

						ClientTransaction.id = "";
						ClientTransaction.client = "";
						ClientTransaction.reason = "";
						ClientTransaction.flag = false; 
                    } // end of actionPerformed


                    button id:'saver', text: 'Add', actionPerformed: {
                        println "... Saved"  
						swingBuilder.find.setEnabled(true)
						
						ClientTransaction.id = swingBuilder.idField.text;
						ClientTransaction.client = swingBuilder.clientField.text;
						ClientTransaction.reason = swingBuilder.reasonField.text;
						ClientTransaction.flag = swingBuilder.flagField.isSelected();
						println "... ClientTransaction (usage:${ClientTransaction.usage}) ="+ClientTransaction.toString();
                        ClientTransaction.say(ClientTransaction.usage);

						swingBuilder.idField.setEditable(true)
						swingBuilder.idField.setEnabled(true)
						swingBuilder.idField.requestFocus();
						swingBuilder.idField.grabFocus()

						swingBuilder.clientField.setEditable(false)
						swingBuilder.clientField.setEnabled(false)
						swingBuilder.reasonField.setEditable(false)
						swingBuilder.reasonField.setEnabled(false)
						swingBuilder.flagField.setEnabled(false)
						swingBuilder.saver.setEnabled(false)
						swingBuilder.delete.setEnabled(false)
						

						swingBuilder.idField.text = "";
						swingBuilder.clientField.text = "";
						swingBuilder.reasonField.text = "";
						swingBuilder.flagField.setSelected(false);
						swingBuilder.flagField.text = "Expense";

						swingBuilder.saver.text = "Add";
						swingBuilder.ft.text = ' ';            			          		         			          		

						ClientTransaction.id = "";
						ClientTransaction.client = "";
						ClientTransaction.reason = "";
						ClientTransaction.flag = false; 
                    } // end of actionPerformed
                    

	                button id:"delete", text:"Delete", enabled:false, actionPerformed: {
                        println "... Delete"  
						swingBuilder.find.setEnabled(true)
						swingBuilder.idField.setEditable(true)
						swingBuilder.idField.setEnabled(true)
						swingBuilder.idField.requestFocus();
						swingBuilder.idField.grabFocus();
						
						def j = swingBuilder.idField.text as Integer;
                        ClientTransaction.delete(j);

						swingBuilder.clientField.setEditable(false)
						swingBuilder.clientField.setEnabled(false)
						swingBuilder.reasonField.setEditable(false)
						swingBuilder.reasonField.setEnabled(false)
						swingBuilder.flagField.setEnabled(false)
						swingBuilder.saver.setEnabled(false)
						swingBuilder.delete.setEnabled(false)
						

						swingBuilder.idField.text = "";
						swingBuilder.clientField.text = "";
						swingBuilder.reasonField.text = "";
						swingBuilder.flagField.setSelected(false);
						swingBuilder.flagField.text = "Expense";

						swingBuilder.saver.text = "Add";
						swingBuilder.ft.text = ' ';            			          		         			          		

						ClientTransaction.id = "";
						ClientTransaction.client = "";
						ClientTransaction.reason = "";
						ClientTransaction.flag = false; 
                    } // end of actionPerformed


                    button text: 'Exit', actionPerformed: {
                        System.exit(0);
                    }
                } // end of panel
         
         
                // Binding of textfield's to Transaction object.
                bean ClientTransaction,
                    id: bind { idField.text() }
                    client: bind { clientField.text() }
                    reason: bind { reasonField.text() }
                    flag: bind { flagField.isSelected() }

            } // end of frame
    
			    find.addActionListener(new ActionListener()
			    {
        			public void actionPerformed(ActionEvent e) 
        			{
            			// Do something here...
            			println "gui.find hit to find "+swingBuilder.idField.text  

						def j = swingBuilder.idField.text as Integer
						swingBuilder.idField.setEditable(false)
						swingBuilder.idField.setEnabled(false)

						swingBuilder.clientField.setEditable(true)
						swingBuilder.clientField.setEnabled(true)
						swingBuilder.reasonField.setEditable(true)
						swingBuilder.reasonField.setEnabled(true)
						swingBuilder.flagField.setEnabled(true)
						swingBuilder.saver.setEnabled(true)


						ClientSupport cs = new ClientSupport();

						ClientTransaction.usage = cs.getH2(j);
						// row id found, so update or delete
						if (ClientTransaction.usage)
						{
							println "... find says to update"
	                		String client = cs.getClient(j);
							swingBuilder.clientField.text = client;            			          		
	                		String reason = cs.getReason(j);
							swingBuilder.reasonField.text = reason;            			          		
							swingBuilder.saver.text = "Update";   
							swingBuilder.delete.setEnabled(true);
							swingBuilder.ft.text = 'found';

							swingBuilder.flagField.setSelected( cs.getFlag(j) );  	
							swingBuilder.flagField.text = (swingBuilder.flagField.isSelected()) ? "Income" : "Expense";  			          		           			          		         			          		
						}

						else
						{
							println "... find says to add: "+ClientTransaction.usage;
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


        		flagField.addActionListener(new ActionListener()
            	{
		    		public void actionPerformed(ActionEvent actionEvent) 
      				{
        				println "... actionListener starting"  // for :"+id;
				        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
    	    			println "... isSelected :"+actionEvent.getSource().isSelected();

				        String key = actionEvent.getActionCommand().toString();
    	    			boolean selected = abstractButton.getModel().isSelected();
        				ClientTransaction.flag = selected;
        				if (selected) { swingBuilder.flagField.text = "Income" } else { swingBuilder.flagField.text = "Expense" }
        				println "\n... actionListener ended\n";        
      				} // end of action
            	}); // end of addActionListener         

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
