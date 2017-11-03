import groovy.swing.SwingBuilder 
import groovy.beans.Bindable 
import static javax.swing.JFrame.EXIT_ON_CLOSE 
import java.awt.*
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.Date;
import java.text.SimpleDateFormat
import java.awt.event.KeyEvent
import com.jim.toolkit.tools.H2Support;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;

@Bindable
class Transaction { 
    String date, type, client, purpose, amount
    Boolean flag
    String toString() { "Transaction[date=$date,type=$type,client=$client,purpose=$purpose,amount=$amount,flag=$flag]" }
    void say() 
    { 
    	println "... say() Transaction.flag=|"+flag+"|";
    	//H2Support obj = new H2Support();    
    	def date = new Date()
		//def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
		//obj.add("Transaction here.   ",date);    
        //println "H2Support = [${obj.toString()}]"
        //obj.select(); 
    } // end of say()
}
   

   /** 
    * Variable datex of current today's date.
    */  
    Date datex = new Date();
    def sdf = new SimpleDateFormat("dd/MM/yyyy")

    def Transaction = new Transaction(date: sdf.format(datex), type: 'A', client: '12', purpose:'rent', amount:'123.45', flag:false)
   
    // need this for flag checkbox selection
    ActionListener actionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        println "... actionListener starting"  // for :"+id;

        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
        println "... isSelected :"+actionEvent.getSource().isSelected();

        String key = actionEvent.getActionCommand().toString();
        boolean selected = abstractButton.getModel().isSelected();
        Transaction.flag = selected;
        println "\n... actionListener ended\n";        
      }
    }; // end of ActionListener

def swingBuilder = new SwingBuilder()
swingBuilder.edt {  // edt method makes sure UI is build on Event Dispatch Thread.
    lookAndFeel 'nimbus' //'nimbus'  // Simple change in look and feel.
    frame(title: 'Transaction', size: [320, 360],
            show: true, locationRelativeTo: null,
            defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
        borderLayout(vgap: 5)
         
        panel(constraints: BorderLayout.CENTER,
                border: compoundBorder([emptyBorder(10), titledBorder('Enter your details:')])) {
            tableLayout {
                tr {
                    td {
                        label 'Date of Action:'  // text property is default, so it is implicit.
                    }
                    td {
                        textField id: 'dateField', columns: 8, text: Transaction.date
                    }
                }
                tr {
                    td {
                        label 'Type :'
                    }
                    td {
                        textField id: 'typeField', columns: 2,  horizontalAlignment:JTextField.RIGHT, text: Transaction.type
                    }
                }
                tr {
                    td {
                        label 'Client Number :'
                    }
                    td {
                        textField id: 'clientField',  horizontalAlignment:JTextField.RIGHT, columns: 2, Transaction.client
                    }
                } // end of tr
                tr {
                    td {
                        label 'Purpose :'
                    }
                    td {
                        textField id: 'purposeField', columns: 16, Transaction.purpose
                    }
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
                        label 'YN Flag :'
                    }
                    td {
                        //checkBox(id:'aCheckBox4', text:"Stuffed Crust");
                        checkBox id: 'flagField' 
                    }
                } // end of tr

            } // end of tableLayout
             
        }
         
        panel(constraints: BorderLayout.SOUTH) {
            button text: 'Save', actionPerformed: {
                println Transaction
                Transaction.say();
            }
            button text: 'Exit', actionPerformed: {
                System.exit(0);
            }
        }

        flagField.addActionListener(actionListener);

         
        // Binding of textfield's to Transaction object.
        bean Transaction,
            date: bind { dateField.text },
            type: bind { typeField.text },
            flag: bind { flagField.isSelected() },
            purpose: bind {purposeField.text},
            amount: bind{amountField.text}, 
            client: bind { clientField.text }
            println "... bean Transaction... ${flagField} ?"
    } // end of frame
    
    //boolean ok = (Transaction.flag.trim().toLowerCase().startsWith('t') ) ? true : false;
    //flagField.setSelected(ok);
} // end of swingBuilder