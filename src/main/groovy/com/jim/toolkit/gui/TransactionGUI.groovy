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

import groovy.transform.*;

import com.jim.toolkit.database.H2RowSupport;
import com.jim.toolkit.tools.ClientSupport;
import com.jim.toolkit.tools.DateSupport;
import com.jim.toolkit.tools.DateArithmetic;
   
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
    String date, type, number, reason, amount, dow
    Boolean flag

	int frequency = 1; // 1=+7days 2= -7days 3= +1month 4= -1 month    
    

   /** 
    * Method to produce and write an interval of times between repeat TXNs
    * 
    * @param x1 is an integer number of frequency interval: 1=+7days 2=-7days 3=+1month 4=-1month
    * @return void
    */     
    void setFreq(int i) { frequency = i; }

    String toString() { "Transaction[date=$date,type=$type,amount=$amount,number=$number,flag=$flag,reason=$reason,dow=$dow]" }

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

        int value = number as Integer
        def nbd = new BigDecimal(amount)
        Map ma = [:]
        def d = new Date()

        if (ds.isDate(date))
        {
            d = ds.getDate();
        }

        // loop to produce one or more TXNs the same except for +/- date
        x1.times
        {
	        ma = [id:0, date:d, type:type, amount:nbd, number:value,flag:flag,reason:reason]
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
 *
 */ 
// @Canonical 
public class TransactionGUI
{
   /** 
    * Variable name of current class.
    */  
    String classname = "TransactionGUI";

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
    def Transaction = new Transaction(date: sdf.format(datex), type: 'C', amount:0.00, number: 0, flag:false, reason:'', dow:dow)
   
    def swingBuilder = new SwingBuilder()


   /** 
    * Method to do the GUI.
    * edt method makes sure UI is built on Event Dispatch Thread.
    * 
    * @return void
    */     
    public void load()
    {
		swingBuilder.edt 
		{  
            //dow = da.getDayOfWeekName(datex);
    		lookAndFeel 'nimbus' //'nimbus'  // Simple change in look and feel.    
		    frame(title: 'Transaction', size: [490, 370], show: true, locationRelativeTo: null, defaultCloseOperation: JFrame.EXIT_ON_CLOSE) 
    		{
		        borderLayout(vgap: 5)
                panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Enter your details:')]) ) 
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
                            		button(id:"plus", text:"+7 days");
            						button(id:"minus", text:"-7 days");
	                    	    } // end of hbox
                    	   } // end of td
                		} // end of tr


                		tr {
                    		td {
        							label ' '
                    		}
                		} // end of tr


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
	                	tr {
    	                	td {
        	                	label 'Client Number :'
            	        	}
                	    	td {
        						hbox{
                    	    		textField id: 'clientField',  horizontalAlignment:JTextField.LEFT, columns: 8, Transaction.number
	    	                	    label ' '
                    	        	label id: 'crField', text: Transaction.clientreason
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
                            			radioButton(id:"three", text:"Expense", buttonGroup:tyGroup, selected:true);
            							radioButton(id:"two", text:"Income", buttonGroup:tyGroup);
                            			radioButton(id:"one", text:"Balance", buttonGroup:tyGroup);
        							}
                    		} // end of td
                		} // end of tr


	                    tr {
    	                    td {
        	                   label 'Amount :'
            	            }
                            td {
                    	       textField id: 'amountField', horizontalAlignment:JTextField.RIGHT,  columns: 12, Transaction.amount
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
                            		radioButton(id:"plus7", text:"+7days", buttonGroup:bg);
        							label ' '
            						radioButton(id:"minus7", text:"-7days", buttonGroup:bg);
        							label ' '
                            		radioButton(id:"plus1", text:"+1Month", buttonGroup:bg);
        							label ' '
                            		radioButton(id:"minus1", text:"-1Month", buttonGroup:bg);
        					    } // end of hbox
                    	    } // end of td
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
            		button text: 'Save', actionPerformed: 
            		{
                		//println Transaction

		                int count = 1; // default to +7day intervals
    		            if ( swingBuilder.minus7.isSelected() ) { count = 2;}
        		        if ( swingBuilder.plus1.isSelected() ) { count = 3;}
            		    if ( swingBuilder.minus1.isSelected() ) { count = 4;}
                		Transaction.setFreq(count);

		                count = 1;
    		            if ( swingBuilder.r2.isSelected() ) { count = 2;}
        		        if ( swingBuilder.r3.isSelected() ) { count = 3;}
            		    if ( swingBuilder.r4.isSelected() ) { count = 4;}

	                	Transaction.say(count);
            		} // end of button

		            button text: 'Exit', actionPerformed: {
        		        System.exit(0);
            		}
        		} // end of panel
        

        		flagField.addActionListener(new ActionListener()
            	{
		    		public void actionPerformed(ActionEvent actionEvent) 
      				{
        				//println "... actionListener starting"  // for :"+id;

				        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
    	    			//println "... isSelected :"+actionEvent.getSource().isSelected();

				        String key = actionEvent.getActionCommand().toString();
    	    			boolean selected = abstractButton.getModel().isSelected();
        				Transaction.flag = selected;
        				if (selected) { swingBuilder.flagField.text = "Y" } else { swingBuilder.flagField.text = "N" }
        				//println "\n... actionListener ended\n";        
      				} // end of action
            	}); // end of addActionListener
	       
         
	        	// Binding of textfield's to Transaction object.
        		bean Transaction,
            		date: bind { dateField.text },
            		//type: bind { typeField.text },
	            	amount: bind{amountField.text}, 
    	        	number: bind { clientField.text },
        	    	flag: bind { flagField.isSelected() },
            		reason: bind {purposeField.text}
            		//println "... bean Transaction... ${flagField} ?"
    			} // end of frame

			    plus.addActionListener(new ActionListener()
			    {
        			public void actionPerformed(ActionEvent e) 
        			{
            			// Do something here...
            			//println "gui.plus hit"
            		
						DateSupport ds = new DateSupport();
    	    			if (ds.isDate(swingBuilder.dateField.text))
        				{
            				datex = ds.getDate();
        				}

			            datex += 7;  
		                Transaction.date = sdf.format(datex); 
	    	            //println "... + [${Transaction.date}]";
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
            			// Do something here...
            			//println "gui.minus hit"
            		
						DateSupport ds = new DateSupport();
    	    			if (ds.isDate(swingBuilder.dateField.text))
        				{
            				datex = ds.getDate();
        				}

			            datex -= 7;  
		                Transaction.date = sdf.format(datex); 
	    	            //println "... + [${Transaction.date}]";
	        	        swingBuilder.dateField.text = Transaction.date;

	            	    // update day-of-week
	                	String dow = da.getDayOfWeekName(datex);
	                	Transaction.dow = dow;
	                	swingBuilder.dowField.text = dow; 
        			}
    			});  // end of addActionListener
    

		    	one.addActionListener(new ActionListener()
		    	{
        			public void actionPerformed(ActionEvent e) 
        			{
            			// Do something here...
            			//println "gui.one hit"
            			Transaction.type = 'A';
        			}
    			}); // end of addActionListener

    
			    two.addActionListener(new ActionListener()
			    {
        			public void actionPerformed(ActionEvent e) 
        			{
            			// Do something here...
            			//println "gui.two hit"
            			Transaction.type = 'B';
        			}
    			}); // end of addActionListener
    
			    three.addActionListener(new ActionListener()
			    {
        			public void actionPerformed(ActionEvent e) 
        			{
            			// Do something here...
            			//println "gui.three hit"
            			Transaction.type = 'C';
        			}
    			}); // end of addActionListener
    
		    	clientField.addFocusListener(
            	[
             		focusGained: { e -> }, //println "Focus gained on clientField: $e.cause"},
             		focusLost: {e -> 
             			//println "Focus lost on clientField: $e.cause"

	                	// update reasons and client name
						def j = swingBuilder.clientField.text as Integer
						ClientSupport cs = new ClientSupport();

	                	String client = cs.getClient(j);
	                	if ( client.size() < 4)
	                	{
	                		client = "";
		            	}
		            
		            	Transaction.clientreason = client;
		            	swingBuilder.crField.text = client;

	                	String rea = cs.getReason(j);  
	                	if ( rea.size() > 1 ) { swingBuilder.purposeField.text = rea; }

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

	             	} // end of focusLost
            	] as FocusListener) // end of FocusListener


	        	dateField.addFocusListener(
    	        [
        	     	focusGained: { e -> }, //println "Focus gained: $e.cause"},
            	 	focusLost: {e -> 
             			//println "Focus lost: $e.cause"

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
        println "--- starting TransactionGUI ---"
        TransactionGUI obj = new TransactionGUI();
        obj.load();
        println "--- the end of TransactionGUI ---"
    } // end of main

} // end of class
