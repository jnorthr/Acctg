package com.jim.toolkit.gui;

import groovy.swing.SwingBuilder 
import groovy.beans.Bindable 
import static javax.swing.JFrame.EXIT_ON_CLOSE 
import java.awt.event.*
import javax.swing.event.*
import java.awt.*
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.Date;
import java.text.SimpleDateFormat
import java.awt.event.KeyEvent
//import com.jim.toolkit.database.H2TableSupport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

import groovy.transform.*;

import com.jim.toolkit.database.H2RowSupport;
import com.jim.toolkit.tools.ClientSupport;
import com.jim.toolkit.tools.DateSupport;
import com.jim.toolkit.tools.DateArithmetic;
import com.jim.toolkit.tools.DecodeDate;

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

@Bindable
class Transaction 
{ 
   /** 
    * Variable holds string version of date of current txn date.
    */  
    String date

    String type, reason, amount, dow, ccy, name, clientnumber
    Boolean flag, usage
    int client
    int id 
	int frequency = 1; // 1=+1days 2= -1days 3= +1month 4= -1 month    

    int seq = 2; // affects ordering of client display
    
   /** 
    * Method to produce and write an interval of times between repeat TXNs
    * 
    * @param x1 is an integer number of frequency interval: 1= + 1 day 2 = - 1 day 3 = + 1 month 4 = - 1 month
    * @return void
    */     
    void setFreq(int i) { frequency = i; }

    String toString() { "Transaction[date=$date,type=$type,amount=$amount,ccy=$ccy,client=$client,flag=$flag,reason=$reason,dow=$dow,usage=$usage,name=$name]" }

    String clientreason = "";

   /** 
    * Method to produce and write a TXN into th H2 table named 'core'
    * 
    * @param x1 is an integer number of times to repeat this TXN
    * @return void
    */     
    void say(int x1) 
    { 
	    H2RowSupport obj = new H2RowSupport();  
    	DateSupport ds = new DateSupport();
        DateArithmetic da = new DateArithmetic();
        if (clientnumber=="") { clientnumber = "0" }
        if (amount=="") { amount = "0" }

        int client = clientnumber as Integer
        def nbd = new BigDecimal(amount)
        Map ma = [:]
        def d = new Date()

        if (ds.isDate(date)) // 03/02/2018 =3feb2018
        {
            d = ds.getIsoDate(date);
        }

        // GUI say date =|22/12/2016| d=|Thu Dec 22 18:07:04 IST 2016|
        println "... GUI say date =|${date}| d=|${d}|"

        // if update, then disallow repeat txns
        if (usage)
        {
            ma = [id:id, date:d, type:type, amount:nbd, ccy:ccy, client:client, flag:flag,reason:reason, name:name]
            println "... want to update id ${id} map="+ma; //obj.update(ma);
            obj.update(ma)
        }

        else
        // add loop to produce one or more TXNs the same except for +/- date
        x1.times
        {
	        ma = [id:0, date:d, type:type, amount:nbd, ccy:ccy, client:client, flag:flag,reason:reason, name:name]
            // if update, pass ID to 
    	    obj.add(ma);

        	if (frequency==1) { d+=7; }
        	if (frequency==2) { d-=7; }
        	if (frequency==3) 
        	{ 
				d = da.bumpMonth(d, 1)
        	} // end of if
        	
        	if (frequency==4) 
        	{ 
				d = da.bumpMonth(d, -1)
        	} // end of if
        	        	
        } // end of times

    } // end of say()
} // end of class
   

/** 
 * TransactionGUI class description
 *
 * This is a tool to add entries to the Sql 'core' table with all bits needed 
 */ 
// @Canonical 
public class TransactionGUI
{
   /** 
    * Variable name of current class.
    */  
    String classname = "TransactionGUI";

   /** 
    * Variable flag is true if core row found for this Id.
    */  
    boolean usage = false;

    /** 
     * DateArithmetic is code with all bits needed to validate and increment date strings, usually as dd/mm/ccyy or yyyy-mm-dd mostly ISO date formatting
     *
     * Strange findings: Beware: Month is zero-relative so January is held in Date object as zero;
     * when adding or subtracting to dat[MONTH] MUST use += syntax
     *
     * Saturday is day 7 with Sunday as day 1 for day of week names.
     */ 
    DateArithmetic da = new DateArithmetic();

   /** 
    * Variable datex of current today's date.
    */  
    Date datex = new Date();
    
   /** 
    * Setup format of expected date variables.
    */  
    def sdf = new SimpleDateFormat("dd/MM/yyyy")

   /** 
    * establish day-of-week name field using DateArithmetic method that needs a Date object.
    */  
    String dow = da.getDayOfWeekName(datex);

   /** 
    * establish a default day-of-week name field using DateArithmetic method that needs a Date object.
    */  
    def Transaction = new Transaction(date: sdf.format(datex), type: 'C', amount:0.00, ccy:1, client:0, flag:false, reason:'', name:'', dow:dow, usage:false)
   

    def swingBuilder = new SwingBuilder()

   /** 
    * Handle to open another scrolling display of current client data 
    */  
    JFrame win = new ClientDisplay();


   /** 
    * Handle to open another scrolling display of current core transaction data 
    */  
    JFrame core = new CoreDisplay();


   /** 
    * Method to display log messages 
    */  
    public void say(String tx)
    {
        println tx;
    } // end of method


   /** 
    * Method to set display to original conditions 
    */  
    public void reset()
    {
        win.loader(Transaction.seq);    
        core.loader(10);  
        swingBuilder.idField.setEditable(true)
        swingBuilder.idField.setEnabled(true)                                    
        swingBuilder.remove.setEnabled(false)
        swingBuilder.find.setEnabled(true)
        swingBuilder.ft.text = '';
        swingBuilder.idField.text = '';
        swingBuilder.amountField.text = '';
        swingBuilder.purposeField.text = '';

        swingBuilder.flagField.selected = false
        swingBuilder.flagField.text = "N"

        swingBuilder.t3.selected = true
        swingBuilder.eur.selected = true

        swingBuilder.clientField.text = "";         
        swingBuilder.nameField.text = "";         
        swingBuilder.save.text = 'Save';

        swingBuilder.dateField.setEnabled(false)                       
        swingBuilder.clientField.setEnabled(false)                       
        swingBuilder.purposeField.setEnabled(false)                       
        swingBuilder.flagField.setEnabled(false)                       
        swingBuilder.amountField.setEnabled(false) 
        swingBuilder.plus.setEnabled(false)
        swingBuilder.minus.setEnabled(false)

        swingBuilder.t1.setEnabled(false)
        swingBuilder.t2.setEnabled(false)
        swingBuilder.t3.setEnabled(false)
        swingBuilder.eur.setEnabled(false)
        swingBuilder.gbp.setEnabled(false)
        swingBuilder.usd.setEnabled(false)
        swingBuilder.save.setEnabled(false)                       
        swingBuilder.remove.setEnabled(false)                       

        swingBuilder.idField.requestFocus();
        swingBuilder.idField.grabFocus()
    } // end of method

   /** 
    * Method to set display to accept user input 
    */  
    public void unset()
    {
        swingBuilder.dateField.setEnabled(true)                       
        swingBuilder.clientField.setEnabled(true)                       
        swingBuilder.purposeField.setEnabled(true)                       
        swingBuilder.flagField.setEnabled(true)                       
        swingBuilder.amountField.setEnabled(true) 
        swingBuilder.plus.setEnabled(true)
        swingBuilder.minus.setEnabled(true)

        swingBuilder.t1.setEnabled(true)
        swingBuilder.t2.setEnabled(true)
        swingBuilder.t3.setEnabled(true)
        swingBuilder.eur.setEnabled(true)
        swingBuilder.gbp.setEnabled(true)
        swingBuilder.usd.setEnabled(true)
        swingBuilder.save.setEnabled(true)                       
        swingBuilder.remove.setEnabled(true)                       

        swingBuilder.clientField.requestFocus();
        swingBuilder.clientField.grabFocus()
    } // end of method
    
   /** 
    * Method to do the GUI.
    * edt method makes sure UI is built on Event Dispatch Thread.
    * 
    * @return void
    */     
    public void load()
    {
        win.setVisible(true);
        win.loader(Transaction.seq);        
        core.setVisible(true);
        core.loader(10);

		swingBuilder.edt 
		{  
            //dow = da.getDayOfWeekName(datex);
    		lookAndFeel 'nimbus' //'nimbus'  // Simple change in look and feel.    
		    frame(title: 'Transaction', size: [570, 510], show: true, locationRelativeTo: null, defaultCloseOperation: JFrame.EXIT_ON_CLOSE) 
    		{
		        borderLayout(vgap: 2)

                panel(constraints: BorderLayout.NORTH, border: compoundBorder([emptyBorder(4), titledBorder('Search For Transaction Id:')]) ) 
                {
                    tableLayout 
                    {
                        tr{
                            td {
                                hbox{
                                    label 'Transaction Id:'  // text property is default, so it is implicit.
                                } // end of hbox
                            }
                            td {
                                hbox{
                                    textField id: 'idField', columns: 3 
                                    label ' '
                                    button(id:"find", text:"Find");
                                    label ' '
                                    button(id:"remove", text:"Delete", enabled:false);
                                    label ' '
                                    label id:'ft', text:' '
                               } // end of hbox
                           } // end of td
                        } // end of tr
                        
                    } // end of tableLayout
                } // end of panel




                panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Add/Update Your Transaction :')]) ) 
        		{
            		tableLayout 
            		{
                		tr {
                    		td {
                                hbox{
                           		 label 'Date of Action:'  // text property is default, so it is implicit.
                                } // end of hbox
                    		}
	                    	td {
    	                		hbox{
	    	                    	textField id: 'dateField', columns: 8, text: Transaction.date
	    	                    	label ' '
                    	            label id: 'dowField', text: Transaction.dow
	    	                    	label ' '
                            		button(id:"plus", text:"+ 1 day");
            						button(id:"minus", text:"- 1 day");
	                    	    } // end of hbox
                    	   } // end of td
                		} // end of tr


                		tr {
                    		td {
        							label ' '
                    		}
                		} // end of tr

	                	tr {
    	                	td {
        	                	label 'Client Number :'
            	        	}
                	    	td {
        						hbox{
               	    		        textField id: 'clientField',  horizontalAlignment:JTextField.LEFT, columns: 8, Transaction.clientnumber
	    	                	    label ' '
                    	        	label id: 'nameField', text: Transaction.name
	    	                    	label ' '
        						}
                    		} // end of td
                		} // end of tr


		                tr {
    		                td {
        		                label 'Purpose :'
            		        }
                		    td {
                    		    textField id: 'purposeField', columns: 16, Transaction.reason
                    		}
	                	} // end of tr

                		tr {
                    		td {
        							label ' '
                    		}
                		} // end of tr

                		tr {
                    		td {
                        			label 'Type :'
                    		}

                    		td {
       								tyGroup = buttonGroup(id:"bg");
        							hbox{
                            			radioButton(id:"t3", text:"Expense", buttonGroup:tyGroup, selected:true);
            							radioButton(id:"t2", text:"Income", buttonGroup:tyGroup);
                            			radioButton(id:"t1", text:"Balance", buttonGroup:tyGroup);
        							}
                    		} // end of td
                		} // end of tr


	                    tr {
    	                    td {
        	                   label 'Amount :'
            	            }
                            td {
                 	            ccyGroup = buttonGroup(id:"cg");
                                hbox{
	                 	       		textField id: 'amountField', horizontalAlignment:JTextField.RIGHT, columns: 12, Transaction.amount
                                    radioButton(id:"eur", text:"EURO  ", buttonGroup:ccyGroup, selected:true);
                                    radioButton(id:"gbp", text:"GBP  ", buttonGroup:ccyGroup);
                                    radioButton(id:"usd", text:"USD", buttonGroup:ccyGroup);
                                }

                    	   }
                	    } // end of tr

                        tr {
    	                    td {
        	                   label 'Flag :'
            	            }
                	        td {
                    	       //checkBox(id:'aCheckBox4', text:"Stuffed Crust");
                        	   checkBox id: 'flagField', text:'N' 
                    	    }
                	    } // end of tr

                        tr {
                    	   td {
        						label ' '
                   		   }
                        } // end of tr

                    } // end of tableLayout
                } // end of panel

         
		        panel(constraints: BorderLayout.SOUTH) 
        		{
                    vbox{
                        hbox(border: compoundBorder([emptyBorder(10), titledBorder('Repeat Transaction Choices')])){
                            // pane.setBorder(BorderFactory.createLineBorder(Color.black));
                            tableLayout{
                                tr {
                                    td {
                                            label 'Repeat This TXN:'
                                    }
                                    td {
                                        tGroup = buttonGroup(id:"tg");
                                        hbox{
                                            radioButton(id:"r1", text:"Once", buttonGroup:tg, selected:true);
                                            label ' '
                                            radioButton(id:"r2", text:"Twice", buttonGroup:tg);
                                            label ' '
                                            radioButton(id:"r3", text:"3 Times", buttonGroup:tg);
                                            label ' '
                                            radioButton(id:"r4", text:"4 Times", buttonGroup:tg);
                                        } // end of hbox
                                    } // end of td                      
                                } // end of tr

                                tr {
                                    td {
                                        label 'Frequency :'
                                    }

                                    td {
                                        bGroup = buttonGroup(id:"bg");
                                        hbox{
                                            radioButton(id:"plus7", text:"+7 days", buttonGroup:bg);
                                            label ' '
                                            radioButton(id:"minus7", text:"-7 days", buttonGroup:bg);
                                            label ' '
                                            radioButton(id:"plus1", text:"+1 Month", buttonGroup:bg);
                                            label ' '
                                            radioButton(id:"minus1", text:"-1 Month", buttonGroup:bg);
                                        } // end of hbox
                                    } // end of td
                                } // end of tr

                                tr {
                                    td {
                                        label ' '
                                    }
                                } // end of tr

                            } // end of tableLayout
                        } // end of hbox

                        hbox{
                            button text: 'Reset', actionPerformed: 
                            {
                                reset();
                            } // end of button


                    		button id:'save', text: 'Save', actionPerformed: 
                    		{
        		                int count = 1; // default to +7day intervals
            		            if ( swingBuilder.minus7.isSelected() ) { count = 2;}
                		        if ( swingBuilder.plus1.isSelected() ) { count = 3;}
                    		    if ( swingBuilder.minus1.isSelected() ) { count = 4;}
                        		Transaction.setFreq(count);

    		                    count = 1;
        		                if ( swingBuilder.r2.isSelected() ) { count = 2;}
            		            if ( swingBuilder.r3.isSelected() ) { count = 3;}
                		        if ( swingBuilder.r4.isSelected() ) { count = 4;}

                                // if blank id, make it zero
                                if (swingBuilder.amountField.text.size() < 1 ) 
                                { 
                                    JOptionPane.showMessageDialog(null, "Can not save without an amount", "Missing Amount", JOptionPane.ERROR_MESSAGE);
                                    swingBuilder.amountField.requestFocus();
                                    swingBuilder.amountField.grabFocus()
                                    return
                                } // end of if

    	                	    Transaction.say(count);
                                reset();
                    		} // end of button

        		            button text: 'Exit', actionPerformed: 
                            {
                		        System.exit(0);
            	           	} // end of button

                        } // end of hbox
                    } // end of vbox
        		} // end of panel
       
				// ==========================================================================================
				// start of Listeners
				
		    	remove.addActionListener(new ActionListener()
		    	{
        			public void actionPerformed(ActionEvent e) 
        			{
            			println "... will remove this - "
	    				H2RowSupport h2rs = new H2RowSupport();  
						// if blank id, make it zero
                        def j = 0;
                        if (swingBuilder.idField.text.isInteger())
                        {
                            j = swingBuilder.idField.text as Integer
            			} // end of if
            				    				
						// check if this ID (int j) exists in 'clients' table
						Transaction.usage = h2rs.remove(j);
						if (Transaction.usage)
						{
                            reset();
                            Transaction.id = 0;
						} // end of if
						else
						{
							swingBuilder.ft.text = "Id ${j} could not be removed";
                            Transaction.id = j;
						}
        			}
    			}); // end of addActionListener


				// logic run when 'Find' button is clicked
				find.addActionListener(new ActionListener()
				{
        			public void actionPerformed(ActionEvent e) 
        			{
            			// Do something here...
            			println "gui.find hit to find "+swingBuilder.idField.text  

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

                        if (swingBuilder.idField.text.isInteger())
                        {
                            j = swingBuilder.idField.text as Integer
                        }
            			
                        unset();
						swingBuilder.idField.setEditable(false)
						swingBuilder.idField.setEnabled(false)

	    				H2RowSupport h2rs = new H2RowSupport();  
	    				
						// check if this ID (int j) exists in 'clients' table
						Transaction.usage = h2rs.hasId(j);
						
						// row id found, so update or delete possible
						if (Transaction.usage)
						{
							println "... found so can update"
                            Transaction.id = j;

							swingBuilder.save.text = "Update";   
							swingBuilder.remove.setEnabled(true);
							swingBuilder.ft.text = 'found';
                            def payload = h2rs.getIdRow(j) 
							println "... found payload |${payload}|"

                            DecodeDate dd = new DecodeDate();
                            swingBuilder.dateField.text = dd.reformat(payload.DATE.toString());
                            Transaction.date = swingBuilder.dateField.text

                            swingBuilder.clientField.text = payload.CLIENT.toString();
                            Transaction.clientnumber = swingBuilder.clientField.text
                            Transaction.client = payload.CLIENT

                            swingBuilder.nameField.text = "";
                            if (!payload.NAME==null)
                            {
                                swingBuilder.nameField.text = payload.NAME.toString();
                            }
                            Transaction.name = swingBuilder.nameField.text;

                            swingBuilder.purposeField.text = payload.REASON.toString()
                            Transaction.reason = payload.REASON.toString()

                            swingBuilder.amountField.text = payload.AMOUNT.toString()
                            Transaction.amount = payload.AMOUNT.toString();

                            if (payload.TYPE=="A")
                            {
                                swingBuilder.t1.setSelected(true); 
                                Transaction.type = 'A';
                            }

                            if (payload.TYPE=="B")
                            {
                                swingBuilder.t2.setSelected(true); 
                                Transaction.type = 'B';
                            }

                            if (payload.TYPE=="C")
                            {
                                swingBuilder.t3.setSelected(true); 
                                Transaction.type = 'C';
                            }
                            if (payload.CCY.toString()=="1")
                            {
                                swingBuilder.eur.setSelected(true); 
                                swingBuilder.gbp.setSelected(false); 
                                swingBuilder.usd.setSelected(false); 
                                Transaction.ccy = '1';
                            }

                            if (payload.CCY.toString()=="2")
                            {
                                swingBuilder.eur.setSelected(false); 
                                swingBuilder.gbp.setSelected(true); 
                                swingBuilder.usd.setSelected(false); 
                                Transaction.ccy = '2';
                            }

                            if (payload.CCY.toString() == "3")
                            {
                                swingBuilder.eur.setSelected(false); 
                                swingBuilder.gbp.setSelected(false); 
                                swingBuilder.usd.setSelected(true); 
                                Transaction.ccy = '3';
                            }
						} // end of if

						else // add this ID as it's new
						{
							println "... find says to add: "+Transaction.usage;
							swingBuilder.ft.text = 'not found';            			          		         			          		
							swingBuilder.save.text = "Add";            			          		
							swingBuilder.remove.setEnabled(false)		          		
						} // end of else
						
						swingBuilder.clientField.requestFocus();
						swingBuilder.clientField.grabFocus()
						swingBuilder.find.setEnabled(false)
        			} // end of addActionPeerformed
    			}); // end of addActionListener



        		flagField.addActionListener(new ActionListener()
            	{
		    		public void actionPerformed(ActionEvent actionEvent) 
      				{
				        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();

				        String key = actionEvent.getActionCommand().toString();
    	    			boolean selected = abstractButton.getModel().isSelected();
        				Transaction.flag = selected;
        				if (selected) { swingBuilder.flagField.text = "Y" } else { swingBuilder.flagField.text = "N" }
      				} // end of action
            	}); // end of addActionListener
	       
         
	        	// Binding of textfield's to Transaction object.
        		bean Transaction,
            		date: bind { dateField.text },
            		//type: bind { typeField.text },
	            	amount: bind{amountField.text}, 
    	        	clientnumber: bind { clientField.text },
        	    	flag: bind { flagField.isSelected() },
                    name: bind {nameField.text},
            		reason: bind {purposeField.text}
    			} // end of frame


			    plus.addActionListener(new ActionListener()
			    {
        			public void actionPerformed(ActionEvent e) 
        			{
						DateSupport ds = new DateSupport();
    	    			if (ds.isDate(swingBuilder.dateField.text))
        				{
            				datex = ds.getDate();
        				}

			            datex += 1;  
		                Transaction.date = sdf.format(datex); 
	        	        swingBuilder.dateField.text = Transaction.date;
	                
		                // update day-of-week
		                String dow = da.getDayOfWeekName(datex);
	    	            Transaction.dow = dow;
	        	        swingBuilder.dowField.text = dow; 
        			} // end of addActionPerformed
    			}); // end of addActionListener
    

			    minus.addActionListener(new ActionListener()
			    {
        			public void actionPerformed(ActionEvent e) 
        			{
						DateSupport ds = new DateSupport();
    	    			if (ds.isDate(swingBuilder.dateField.text))
        				{
            				datex = ds.getDate();
        				}

			            datex -= 1;  
		                Transaction.date = sdf.format(datex); 
	        	        swingBuilder.dateField.text = Transaction.date;

	            	    // update day-of-week
	                	String dow = da.getDayOfWeekName(datex);
	                	Transaction.dow = dow;
	                	swingBuilder.dowField.text = dow; 
        			}
    			});  // end of addActionListener
    
    
				// set new Type values Listeners
		    	t1.addActionListener(new ActionListener()
		    	{
        			public void actionPerformed(ActionEvent e) 
        			{
            			Transaction.type = 'A';
        			}
    			}); // end of addActionListener

    
			    t2.addActionListener(new ActionListener()
			    {
        			public void actionPerformed(ActionEvent e) 
        			{
            			Transaction.type = 'B';
        			}
    			}); // end of addActionListener
    
			    t3.addActionListener(new ActionListener()
			    {
        			public void actionPerformed(ActionEvent e) 
        			{
            			Transaction.type = 'C';
        			}
    			}); // end of addActionListener


				// -----------------------------------------------------------
				// logic run when choice is made to use GBP as currency			
	    	    gbp.addActionListener(new ActionListener()
    	    	{
        			public void actionPerformed(ActionEvent e) 
            		{
	                	// Do something here...
    	                Transaction.ccy = "2";
            	    }
            	}); // end of listener
    
				// -----------------------------------------------------------
				// logic run when choice is made to use EUR as currency			
	    	    eur.addActionListener(new ActionListener()
    	    	{
        			public void actionPerformed(ActionEvent e) 
            		{
                		// Do something here...
                    	Transaction.ccy = "1";
                	}
            	}); // end of listener
    
				// -----------------------------------------------------------
				// logic run when choice is made to use USD as currency			
	    	    usd.addActionListener(new ActionListener()
    	    	{
        			public void actionPerformed(ActionEvent e) 
            		{
                		// Do something here...
                    	Transaction.ccy = "3";
                	}
            	}); // end of listener

    
		    	clientField.addFocusListener(
            	[
             		focusGained: { e -> }, //println "Focus gained on clientField: $e.cause"},
             		focusLost: {e -> 
	                	// update reasons and client name
                        def j = 0;
                        if (swingBuilder.clientField.text.isInteger())
                        {
						    j = swingBuilder.clientField.text as Integer
                        }

						ClientSupport cs = new ClientSupport();

	                	String client = cs.getClient(j);
	                	if ( client.size() < 4)
	                	{
	                		client = "";
		            	}
		            
		            	Transaction.clientreason = client;
		            	swingBuilder.nameField.text = client;

	                	String rea = cs.getReason(j);  
	                	if ( rea.size() > 1 ) { swingBuilder.purposeField.text = rea; }

                        rea = cs.getClient(j);  
                        if ( rea.size() > 1 ) { swingBuilder.nameField.text = rea; }
                        
                        int cur = cs.getCurrency(j);
                        Transaction.ccy = cur.toString();                        
                        if ( cur==1 )
                        {
                            swingBuilder.eur.setSelected(true); 
                            swingBuilder.gbp.setSelected(false); 
                            swingBuilder.usd.setSelected(false); 
                        }
                        if ( cur==2 )
                        {
                            swingBuilder.eur.setSelected(false); 
                            swingBuilder.gbp.setSelected(true); 
                            swingBuilder.usd.setSelected(false); 
                        }
                        if ( cur==3 )
                        {
                            swingBuilder.usd.setSelected(true); 
                            swingBuilder.eur.setSelected(false); 
                            swingBuilder.gbp.setSelected(false); 
                        }

                        boolean tf = cs.getFlag(j); // true for Income, false for Expense
                        //Transaction.flag = tf;
                        if ( tf )
                        {
                            swingBuilder.t2.setSelected(true); 
                            Transaction.type = 'B';
                        }
                        else
                        {
                            swingBuilder.t3.setSelected(true);
                            Transaction.type = 'C';
                        }

/*
	                	// when this client is in our map, then we can use ir
	                	if ( cs.inMap(j) ) 
	                	{ 
							if ( cs.isIncome(j) )
							{
		                		swingBuilder.two.setSelected(true); 
            					Transaction.type = 'B';
		            		}
		            		else
		            		{
		                		swingBuilder.three.setSelected(true); 
		            			Transaction.type = 'C';
		            		}    
	                	} // end of if
*/

	             	} // end of focusLost
            	] as FocusListener) // end of FocusListener


	        	dateField.addFocusListener(
    	        [
        	     	focusGained: { e -> }, 
            	 	focusLost: {e -> 
		                // update day-of-week
						DateSupport ds1 = new DateSupport();
        				if (ds1.isDate(swingBuilder.dateField.text))
        				{
            				datex = ds1.getDate();
        				}
    					DateArithmetic da1 = new DateArithmetic();
	                	String dow = da1.getDayOfWeekName(datex);
	                	Transaction.dow = dow;
	                	swingBuilder.dowField.text = dow; 
             		} // end of focusLost
             	] as FocusListener)    

		} // end of swingBuilder

        reset();
    }  // end of load() method


   // ======================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */     
    public static void main(String[] args)
    {
        println "--- starting TransactionGUI ---"
        TransactionGUI obj = new TransactionGUI();
        obj.load();
        println "--- the end of TransactionGUI ---"
    } // end of main

} // end of class

/*
                tr {
                    td {
                        label 'Type :'
                    }
                    td {
                        textField id: 'typeField', columns: 2,  horizontalAlignment:JTextField.RIGHT, text: Transaction.type
                    }
                }
*/                
