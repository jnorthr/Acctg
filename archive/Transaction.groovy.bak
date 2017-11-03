import groovy.swing.SwingBuilder 
import groovy.beans.Bindable 
import static javax.swing.JFrame.EXIT_ON_CLOSE 
import java.awt.*
import javax.swing.JFrame;
  
@Bindable
public class Transaction 
{ 
    String client, amount, reason
    String toString() { "Transaction[client=$client,amount=$amount,reason=$reason]" }
   
    //def Trans tr = new Trans(client: 'US Embassy', amount: '742', reason: 'Pension JBN')
   
    def swingBuilder = new SwingBuilder()
    swingBuilder.edt 
    {  // edt method makes sure UI is build on Event Dispatch Thread.
        lookAndFeel 'nimbus'  // Simple change in look and feel.
        frame(title: 'Transaction', size: [350, 230], show: true, locationRelativeTo: null, defaultCloseOperation: JFrame.EXIT_ON_CLOSE) 
        {
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
    } // end of frame
} // end of class