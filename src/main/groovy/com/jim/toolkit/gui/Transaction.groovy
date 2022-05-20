package com.jim.toolkit.gui;

import groovy.swing.SwingBuilder 
import groovy.beans.Bindable 
import static javax.swing.JFrame.EXIT_ON_CLOSE 
import java.awt.*
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.*;          
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import javax.swing.SwingUtilities;
  
@groovy.beans.Bindable
public class Transaction 
{ 
    String client, amount, reason;
    String toString() { "Transaction[client=$client,amount=$amount,reason=$reason]" }
    

    JFrame frame = new JFrame(title: 'Transaction', size: [350, 230], show: true, locationRelativeTo: null, 
    defaultCloseOperation: JFrame.EXIT_ON_CLOSE,UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"));

    // edt method makes sure UI is built on Event Dispatch Thread.
    def swing = new SwingBuilder();
    swing.edt {
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        borderLayout(vgap: 5)
        panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Enter your Transaction:')])) 
        {
                tableLayout {
                    tr {
                        td {
                            label 'client:'  // text property is default, so it is implicit.
                        }
                        td {
                            textField tr.client, id: 'clientField', columns: 20
                        }
                    } // end of tr
                    tr {
                        td {
                            label 'amount:'
                        }
                        td {
                            textField id: 'amountField', columns: 5, text: tr.amount
                        }
                    } // end of tr
                    tr {
                        td {
                            label 'reason:'
                        }
                        td {
                            textField id: 'reasonField', columns: 20, tr.reason
                        }
                    } // end of tr
                } // end of tableLayout         
            } // end of panel
         

            panel(constraints: BorderLayout.SOUTH) {
                button text: 'Keep', actionPerformed: {
                    println tr
                }
                button text: 'Exit', actionPerformed: {
                    System.exit(0);
                }
            } // end of panel
         
            // Binding of textfield's to Transaction object.
            bean Transaction,
            client: bind { clientField.text },
            amount: bind { amountField.text },
            reason: bind { reasonField.text }
        } // end of panel 

    } // end of edt method
  
} // end of class
