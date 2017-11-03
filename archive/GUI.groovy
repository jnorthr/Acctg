import groovy.swing.SwingBuilder;
import java.awt.FlowLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import java.awt.ItemSelectable;
import javax.swing.*;

import java.awt.event.ItemEvent; // for comboBox
import java.awt.event.ItemListener; // for comboBox

import groovy.beans.Bindable // to setup Bean annotation
        
    r = new Reply(); // if you do this any refs to dropdownChoice,etc must be qualified by 'r.dropdownChoice'
        
    String getId(ActionEvent evt)
    {
        String txt = evt.toString();
        int j = txt.indexOf(" on ");
        def id = txt.substring(j+3).trim();
        return id;
    } // end of method


    ActionListener actionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        String id = getId(actionEvent);

        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
        String key = actionEvent.getActionCommand().toString();
        
        boolean selected = abstractButton.getModel().isSelected();
        r.checkbox[id]=selected; 
      }
    };
    
    
    ItemListener itemListener = new ItemListener() {
      public void itemStateChanged(ItemEvent itemEvent) {
        boolean flag = (itemEvent.getStateChange() == ItemEvent.SELECTED) ? true : false; 
        
        String src = itemEvent.getItem();
        int j = src.indexOf('[');
        src = src.substring(j+1);
        j = src.indexOf(',');
        def button = src.substring(0, j);        
        r.radiobutton[button] = flag; 
      }
    };
    
def names = ["Tom":false, "Dick":false, "Harry":false, "Bill":false]
    
swing = new SwingBuilder();
gui = swing.frame(id:'fr',title:'Test 2', size:[500,300]) {
    panel(layout:new FlowLayout()) {
        panel(layout:new FlowLayout()) {
            checkBox(id:'aCheckBox4', text:"Stuffed Crust");
        } // end of panel
        
        panel(layout:new FlowLayout()) {
            ["Tom", "Dick", "Harry", "Bill", "Fred"].each{ name ->
                checkBox(text:name,id:"${name}" );
            }
        }
        panel(layout:new FlowLayout()) {
            comboBox(id:'dropdown',items:["Red", "Green", "Blue", "Orange","White"],
                     selectedIndex:2);
        }
        
        panel(layout:new FlowLayout()) {        
            myGroup = buttonGroup();
            radioButton(text:"One",id:"One", buttonGroup:myGroup);
            radioButton(text:"Two",id:"Two", buttonGroup:myGroup, selected:true);
            radioButton(text:"Three",id:"Three", buttonGroup:myGroup);
        } // end of panel
        
        panel() {   
            tableLayout {
                tr {
                    td {
                        label 'Street:'  // text property is default, so it is implicit.
                    }
                    td {
                        textField(id:'tf', columns:9, editable:true)
                    } // end of td
                } // end of tr
                tr{
                    td{
                        button(id:'okButton',text:"OK", actionPerformed:{ println r.toString(); } );
                    }
                } // end of tr                
               } // end of layout
               
        } // end of panel

    } // end of panel

    tf.setInputVerifier(new MyInputVerifier());

    // check boxes
    aCheckBox4.addActionListener(actionListener);
    Tom.addActionListener(actionListener); 
    Dick.addActionListener(actionListener); 
    Harry.addActionListener(actionListener); 
    Bill.addActionListener(actionListener); 
    Fred.addActionListener(actionListener); 

    // radio buttons
    One.addItemListener(itemListener); 
    Two.addItemListener(itemListener); 
    Three.addItemListener(itemListener); 

    // Drop Down List
    // dropdown.addItemListener(itemListener);
    dropdown.addActionListener(new ActionListener() 
            {   public void actionPerformed(ActionEvent evt) 
                {
                    JComboBox dd = (JComboBox) evt.getSource();
                    r.dropdownChoice = dd.getSelectedItem();
                }
            }
    ); // end of addActionListener
     
    // textField.addItemListener(itemListener); only hit on Enter key press not validate()
    tf.addActionListener(new ActionListener() 
            {   public void actionPerformed(ActionEvent evt) 
                {
                    println "... hit textField listener"
                }
            }
    ); // end of addActionListener

    r.radiobutton["Two"]=true; // manual add of selected:true radioButton default
    fr.getRootPane().setDefaultButton(okButton)
} // end of gui frame 

gui.show();