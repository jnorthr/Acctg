package com.jim.toolkit.gui;
import com.jim.toolkit.database.H2TableSupport;
import com.jim.toolkit.database.H2TableMethods;
import com.jim.toolkit.tools.ClientSupport;

import groovy.swing.SwingBuilder 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import groovy.util.logging.Slf4j;
import org.slf4j.*

@Slf4j
public class ClientDisplay extends JFrame {

    //============================================== instance variables
    JTextArea _resultArea = new JTextArea(16, 80);
    H2TableSupport h2 = new H2TableSupport('clients');
    H2TableMethods h2tm = new H2TableMethods();
    ClientSupport cs = new ClientSupport();
    
    //Group the radio buttons.
    ButtonGroup bg = new ButtonGroup();
    JRadioButton oneButton = new JRadioButton("Id");
    JRadioButton twoButton = new JRadioButton("Name");
    JRadioButton threeButton = new JRadioButton("Usage");
    JRadioButton fourButton = new JRadioButton("CCY/Id");
    JRadioButton fiveButton = new JRadioButton("CCY/Name");
    JRadioButton sixButton = new JRadioButton("CCY/Usage");

   /** 
    * Method to translate our internal button variable of 1,2,4,5,6 or 3 into man-readable text.
    */     
    public void setButton(int ourcode)
    {
        switch(ourcode)
        {
            case '1': oneButton.setSelected(true);
                     break;
            case '2': twoButton.setSelected(true);
                     break;
            case '3': threeButton.setSelected(true);
                     break;
            case '4': fourButton.setSelected(true);
                     break;
            case '5': fiveButton.setSelected(true);
                     break;
            case '6': sixButton.setSelected(true);
                     break;
        } // end of switch
    } // end of set    

   /** 
    * Method to translate our internal CCY currency variable of 1,2 or 3 into man-readable text.
    * 
    * @return formatted content of CCY variable
    */     
    String cvtCcy(int ourcode)
    {
        def ty="EUR"
        switch(ourcode)
        {
            case '1': ty = 'EUR'
                     break;
            case '2': ty = 'GBP'
                     break;
            case '3': ty = 'USD'
                     break;
        } // end of switch
        return ty;
    } // end of cvtCcy
    
                    
    //====================================================== constructor
    public ClientDisplay() {
        // Sets JTextArea font and color.
        Font font = new Font("monospaced", Font.BOLD, 14);
        _resultArea.setFont(font);
        _resultArea.setForeground(Color.BLUE);
        _resultArea.setEditable(false);

		_resultArea.setText("");
		
        JScrollPane scrollingArea = new JScrollPane(_resultArea);

        JPanel buttonArea = new JPanel();
        JLabel jl = new JLabel("Ordering : ")
        buttonArea.add(jl);

        oneButton.setMnemonic(KeyEvent.VK_I);
        oneButton.setSelected(true);
        bg.add(oneButton);
        buttonArea.add(oneButton);

        // logic run when choice is made to order Client page by Id sequence            
        oneButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(1)
            }
        }); // end of listener


        twoButton.setMnemonic(KeyEvent.VK_N);
        bg.add(twoButton);
        buttonArea.add(twoButton);
        twoButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(2)
            }
        }); // end of listener

        threeButton.setMnemonic(KeyEvent.VK_U);
        bg.add(threeButton);
        buttonArea.add(threeButton);
        threeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(3)
            }
        }); // end of listener

        fourButton.setMnemonic(KeyEvent.VK_4);
        bg.add(fourButton);
        buttonArea.add(fourButton);
        fourButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(4)
            }
        }); // end of listener

        fiveButton.setMnemonic(KeyEvent.VK_5);
        bg.add(fiveButton);
        buttonArea.add(fiveButton);
        fiveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(5)
            }
        }); // end of listener


        sixButton.setMnemonic(KeyEvent.VK_6);
        bg.add(sixButton);
        buttonArea.add(sixButton);
        sixButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(6)
            }
        }); // end of listener


        //... Get the content pane, set layout, add to center
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(scrollingArea, BorderLayout.CENTER);
        content.add(buttonArea, BorderLayout.SOUTH);
        
        //... Set window characteristics.
        this.setContentPane(content);
        this.setTitle("Client Display");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    } // end of constructor


   /** 
    * Method to copy client rows into _resultArea.
    * 
    * @return void
    */     
    public loader()
    {
        return loader(3); // order by name seq.
    } // end of load()

    
   /** 
    * Method to copy client rows into _resultArea.
    * 
    * @return void
    */     
    public loader(int seq)
    {
        setButton(seq);

        //... Set textarea's initial text, scrolling, and border.
        _resultArea.setText("   Id   CCY   Usage     N a m e                        Purpose\n  ___   ___   _______   _________________              ___________________________\n");
        def ans = h2.select(seq)

        ans.each{e-> 
            Map m = h2tm.decode(e);
                        
            String sb = "";
            sb += m.ID.toString().padLeft(5)
            sb += "   ";

            if (m.CCY.isInteger())
            {
                def intValue = m.CCY.toInteger();
                //sb += m.CCY.toString().padLeft(3)
                sb += cvtCcy(intValue).padLeft(3)
                //sb += cs.getCodes(intValue).toString().padLeft(3)
            }
            else
            {
                sb += m.CCY.toString().padLeft(3)
            }
            sb += "   ";

            String ty = (m.FLAG.toString().toLowerCase()=="true") ? "Income " : "Expense"; 
            sb += ty.padRight(9)
            sb += " ";

            sb += (m.NAME) ? m.NAME.padRight(30) : "".padRight(30)
            sb += " ";

            sb += (m.REASON) ? m.REASON.padRight(42) : "".padRight(42)
            sb += " ";

            _resultArea.append(sb); 
            _resultArea.append("\n"); 
        }
    }  // end of method


   /** 
    * Method to print audit log.
    * 
    * @param the text to be said
    * @return void
    */     
    public void say(String txt)
    {
        log.info txt;
    }  // end of method


    
    //============================================================= main
    public static void main(String[] args) {
        JFrame win = new ClientDisplay();
        win.setVisible(true);
        int which = 1
        6.times{
	        win.loader(which++);
	        sleep(8000);
	    } // end of times

        win.loader(1);

    }
}