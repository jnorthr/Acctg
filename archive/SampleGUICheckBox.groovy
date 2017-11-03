import groovy.swing.SwingBuilder;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import java.awt.ItemSelectable;
import javax.swing.*;

import java.awt.event.ItemEvent; // for comboBox
import java.awt.event.ItemListener; // for comboBox

import groovy.beans.Bindable // to setup Bean annotation
    
    class Reply{
        String dropdownChoice = "Blue"; // @Bindable 
        Map checkbox = [:];    // @Bindable 
        Map radiobutton = [:]; // @Bindable 
        
        public String toString() {
            def sb = """dropdownChoice=${dropdownChoice}\n""";
            checkbox.each{k,v-> sb += "checkbox[${k}]=${v} "; }
            if (checkbox.size() > 0 ) sb+= "\n";
            radiobutton.each{k,v-> sb+= "radiobutton[${k}]=${v} "; }
            if (radiobutton.size() > 0 ) sb += "\n"
            return sb.toString();
        } // end of method
                
    } // end of class
    
    r = new Reply(); // if you do this any refs to dropdownChoice,etc must be qualified by 'r.dropdownChoice'
    
    public void show()
    {
        println "... show() now"
        println r;
    } // end of show
    
    Object oldItem = null;
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
        println "... actionListener starting for :"+id;

        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
        //println "... Src :"+actionEvent.getSource().toString();
        //println "... Txt :"+actionEvent.getSource().text.toString();
        //println "... isSelected :"+actionEvent.getSource().isSelected();
        //println "... params    :"+actionEvent.getSource().paramString().toString();

        String key = actionEvent.getActionCommand().toString();
        boolean selected = abstractButton.getModel().isSelected();
        //println "    actionEvent id=${id} cmd:"+key+"="+selected;

        //def tx = (selected) ? "sux" : "ok" ;
        //abstractButton.setText(tx); // actually chgs button text on screen
        // actionEvent.getSource().text.toString()
        r.checkbox[id]=selected; 
        print "... id=${id} = "
        r.checkbox.each{k,v-> print " r.checkbox[${k}]=${v}"; }
        println "\n... actionListener ended\n";        
      }
    };
    
    ItemListener itemListener = new ItemListener() {
      public void itemStateChanged(ItemEvent itemEvent) {
        println "... addItemListener start"
        boolean flag = (itemEvent.getStateChange() == ItemEvent.SELECTED) ?true:false; 
        
        String src = itemEvent.getItem();
        int j = src.indexOf('[');
        src = src.substring(j+1);
        j = src.indexOf(',');
        def button = src.substring(0, j);
        
        r.radiobutton[button] = flag; 
        
        System.out.println("... r.radiobutton[${button}]=" + r.radiobutton[button] + "\n... ");
        r.radiobutton.each{k,v-> print "${k}=${v}, " }
        println "\n... addItemListener end\n"
      }
    };
    
def names = ["Tom":false, "Dick":false, "Harry":false, "Bill":false]
    
swing = new SwingBuilder();
gui = swing.frame(title:'Test 2', size:[400,200]) {
    panel(layout:new FlowLayout()) {
        panel(layout:new FlowLayout()) {
            checkBox(id:'aCheckBox4', text:"Stuffed Crust");
        } // end of panel
        
        panel(layout:new FlowLayout()) {

            //for (name in names) {
            names.each{ name,v->
                checkBox(text:name,id:"${name}" );
            }
        }
        panel(layout:new FlowLayout()) {
            comboBox(id:'dropdown',items:["Red", "Green", "Blue", "Orange"],
                     selectedIndex:2);
        }
        
        panel(layout:new FlowLayout()) {        
            myGroup = buttonGroup();
            radioButton(text:"One",id:"One", buttonGroup:myGroup);
            radioButton(text:"Two",id:"Two", buttonGroup:myGroup, selected:true);
            radioButton(text:"Three",id:"Three", buttonGroup:myGroup);
        } // end of panel
        
        panel(layout:new FlowLayout()) {        
            button(id:'okButton',text:"OK");
            //textField(id:'input',columns:10, actionPerformed: { dropdownChoice = input.text }  ); // press ENTER to put txt into dropdownChoice
            // fails if you try Reply.dropdownChoice; bind annotation must change scope of var.
        } // end of panel

    } // end of panel
    
    aCheckBox4.addActionListener(actionListener);
    Tom.addActionListener(actionListener); 
    Dick.addActionListener(actionListener); 
    Harry.addActionListener(actionListener); 
    Bill.addActionListener(actionListener); 

    One.addItemListener(itemListener); 
    Two.addItemListener(itemListener); 
    Three.addItemListener(itemListener); 
    okButton.addActionListener(new ActionListener() 
    {
         public void actionPerformed(ActionEvent e) {
            show();
         }          
    });
    
    // Drop Down List
    // dropdown.addItemListener(itemListener);
    dropdown.addActionListener(new ActionListener() 
            {   public void actionPerformed(ActionEvent evt) 
                {
                    println "\n... dropdown.addActionListener start"; 
                    //println "... cmd :"+evt.getActionCommand()
                    JComboBox dd = (JComboBox) evt.getSource();
                    //Object newItem = dd.getSelectedItem();
                    r.dropdownChoice = dd.getSelectedItem();
                    println "... -> r.dropdownChoice=[${r.dropdownChoice}]"; // newItem="+newItem.toString();
                    //boolean same = newItem.equals(oldItem);
                    //println(evt.toString()); 
                    println "... dropdown.addActionListener end"
                }
            }
    ); // end of addActionListner 
    
} // end of gui

gui.show();