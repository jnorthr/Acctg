import groovy.swing.SwingBuilder 
import groovy.beans.Bindable 
import static javax.swing.JFrame.EXIT_ON_CLOSE 
import java.awt.*
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.util.Date;
import java.text.SimpleDateFormat
import java.awt.event.KeyEvent


def d = new Date();

//@Bindable
model = new Cell()
   
def swingBuilder = new SwingBuilder()
swingBuilder.edt {  // edt method makes sure UI is build on Event Dispatch Thread.
    lookAndFeel 'nimbus' //'nimbus'  // Simple change in look and feel.
    frame(title: 'Cell', size: [320, 360], show: true, locationRelativeTo: null,defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
        borderLayout(vgap: 5)
         
        panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Enter your details:')])) 
        {
            tableLayout {
                tr {
                    td {
                        label 'YN Flag :'
                    }
                    td {
                        checkBox id: 'flagField',selected: bind(source: model.flag, sourceProperty: 'selected') 
                    }
                } // end of tr

            } // end of tableLayout
             
        }
         
        panel(constraints: BorderLayout.SOUTH) {
            button text: 'Save', actionPerformed: {
                println Cell
                Transaction.say();
            }
            button text: 'Exit', actionPerformed: {
                System.exit(0);
            }
        }
         
        // Binding of textfield's to Transaction object.
        bean Cell,
            flag: bind { flagField.isSelected() }
            println "... bean Transaction... ${flagField} ?"
    } // end of frame
    
    //boolean ok = (Transaction.flag.trim().toLowerCase().startsWith('t') ) ? true : false;
    //flagField.setSelected(ok);
} // end of swingBuilder