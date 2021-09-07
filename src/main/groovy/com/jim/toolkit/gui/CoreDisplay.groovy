package com.jim.toolkit.gui;
import com.jim.toolkit.database.H2TableSupport;
import com.jim.toolkit.database.H2RowSupport;
import com.jim.toolkit.database.H2TableMethods;
import com.jim.toolkit.tools.ClientSupport;

import groovy.swing.SwingBuilder 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import groovy.util.logging.Slf4j;
import org.slf4j.*

@Slf4j
public class CoreDisplay extends JFrame {

    //============================================== instance variables
    JTextArea _resultArea = new JTextArea(18, 120);
    H2TableSupport h2 = new H2TableSupport('core');
    H2TableMethods h2tm = new H2TableMethods();
    ClientSupport cs = new ClientSupport();
    
    //Group the radio buttons.
    ButtonGroup bg = new ButtonGroup();

    JRadioButton zeroButton = new JRadioButton("None");   // 0
    JRadioButton oneButton = new JRadioButton("ID");     // 1
    JRadioButton twoButton = new JRadioButton("CCY,ID");     // 4
    JRadioButton threeButton = new JRadioButton("CLIENT,DATE"); // 7
    JRadioButton fourButton = new JRadioButton("DATE,FLAG DESC,CLIENT");   // 8
    JRadioButton fiveButton = new JRadioButton("CCY,DATE,CLIENT,ID");   // 9
    JRadioButton sixButton = new JRadioButton("CCY,DATE,TYPE");     // 10

   /** 
    * Method to translate our internal button variable of 1,2,4,5,6 or 3 into man-readable text.
    */     
    public void setButton(int ourcode)
    {
        switch(ourcode)
        {
            case '0': zeroButton.setSelected(true); // No seq.
                     break;
            case '1': oneButton.setSelected(true);  // ID
                     break;
            case '4': twoButton.setSelected(true);  // CCY, ID
                     break;
            case '7': threeButton.setSelected(true);    // CLIENT,DATE
                     break;
            case '8': fourButton.setSelected(true); // DATE, FLAG DESC, CLIENT
                     break;
            case '9': fiveButton.setSelected(true); // CCY, DATE, CLIENT, ID
                     break;
            case '10': sixButton.setSelected(true); // CCY, DATE, TYPE
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
        def ty=ourcode.toString()

        switch(ourcode)
        {
            case '0': ty = 'Unk'
                     break;
            case '1': ty = 'EUR'
                     break;
            case '2': ty = 'GBP'
                     break;
            case '3': ty = 'USD'
                     break;
        } // end of switch

        return ty;
    } // end of cvtCcy
    
                    
   /** 
    * Method to translate our internal type of A,B or C into man-readable text.
    * 
    * @return formatted content of type variable
    */     
    String cvtType(String ourcode)
    {
        def ty="Unknown"
        switch(ourcode)
        {
            case 'A': ty = 'Balance'
                     break;
            case 'B': ty = 'Income'
                     break;
            case 'C': ty = 'Expense'
                     break;
        } // end of switch
        return ty;
    } // end of cvttype

                    
   /** 
    * Method to translate our internal client number into man-readable names.
    * 
    * @return formatted content of client number
    */     
    String getName(int ourcode)
    {
        String ty="$ourcode Unknown"
        try{

            boolean b = cs.getClient(ourcode);
            if (b && ourcode!=0)
            {   
                ty = cs.name;
                //obj.say "... getH2() found client "+obj.id+" "+obj.ccy+" "+ty+" from "+obj.name+" for "+obj.reason;
            }
            else
            {
                ty = " "
            }

        }
        catch(Exception x) 
        {
            ty = x.message; 
        }

        return ty;
    } // end of cvttype




    //====================================================== constructor
    public CoreDisplay() {
        this.setLocation(160, 300);
        // Sets JTextArea font and color.
        Font font = new Font("monospaced", Font.BOLD, 12);
        _resultArea.setFont(font);
        _resultArea.setForeground(Color.BLUE);
        _resultArea.setEditable(false);

		_resultArea.setText("");
		
        JScrollPane scrollingArea = new JScrollPane(_resultArea);

        JPanel buttonArea = new JPanel();
        JLabel jl = new JLabel("Ordering : ")
        buttonArea.add(jl);


        // logic run when choice is made to order Client page by Id sequence            
        zeroButton.setMnemonic(KeyEvent.VK_0);
        zeroButton.setSelected(true);
        bg.add(zeroButton);
        buttonArea.add(zeroButton);
        zeroButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(0)
            }
        }); // end of listener


        oneButton.setMnemonic(KeyEvent.VK_1);
        bg.add(oneButton);
        buttonArea.add(oneButton);
        oneButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(1)
            }
        }); // end of listener


        twoButton.setMnemonic(KeyEvent.VK_2);
        bg.add(twoButton);
        buttonArea.add(twoButton);
        twoButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(4)
            }
        }); // end of listener

        threeButton.setMnemonic(KeyEvent.VK_3);
        bg.add(threeButton);
        buttonArea.add(threeButton);
        threeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(7)
            }
        }); // end of listener

        fourButton.setMnemonic(KeyEvent.VK_4);
        bg.add(fourButton);
        buttonArea.add(fourButton);
        fourButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(8)
            }
        }); // end of listener

        fiveButton.setMnemonic(KeyEvent.VK_5);
        bg.add(fiveButton);
        buttonArea.add(fiveButton);
        fiveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(9)
            }
        }); // end of listener


        sixButton.setMnemonic(KeyEvent.VK_6);
        bg.add(sixButton);
        buttonArea.add(sixButton);
        sixButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                loader(10)
            }
        }); // end of listener


        // --------------------------------------------------
        //... Get the content pane, set layout, add to center
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(scrollingArea, BorderLayout.CENTER);
        content.add(buttonArea, BorderLayout.SOUTH);
        
        //... Set window characteristics.
        this.setContentPane(content);
        this.setTitle("Core Display");
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
        BigDecimal[] total = [0.00, 0.00, 0.00, 0.00];

        //... Set textarea's initial text, scrolling, and border.
        //esultArea.setText("   Id   CCY   Usage   Client  N a m e                        Purpose\n  ___   ___   _______   _________________      Client  __________ _________________\n");
        _resultArea.setText("   Id   CCY     D a t e   Usage       Amount  Client        N a m e                        Purpose                 Name\n  ___   ___   __________  _______   ________  ______   ___________________________    ________________________     _____________\n");

        def ans = h2.select(seq)

        ans.each{e-> 
            Map m = h2tm.decode(e);
                        
            String sb = "";
            sb += m.ID.toString().padLeft(5)
            sb += " ";
            sb += (m.FLAG=="true") ? "Y " : "N "

            def intValue = 0;
            if (m.CCY.isInteger())
            {
                intValue = m.CCY.toInteger();
                sb += cvtCcy(intValue).padLeft(3)
            }
            else
            {
                sb += m.CCY.toString().padLeft(3)
            }
            sb += "   ";

            sb += (m.DATE) ? m.DATE.padRight(12) : "".padRight(12)

            def va = m.TYPE.toString().toUpperCase()
            def t = cvtType(va)
            sb += t.padRight(8)
            //sb += " ";

            if (intValue<1 || intValue>3) {intValue=0;}
            
            if (va=='A')
            {
                total[intValue] = (m.AMOUNT) ? m.AMOUNT as BigDecimal : 0; 
            }
            else
            if (va=='B')
            {
                total[intValue] += (m.AMOUNT) ? m.AMOUNT as BigDecimal : 0; 
            }
            else
            if (va=='C')
            {
                total[intValue] -= (m.AMOUNT) ? m.AMOUNT as BigDecimal : 0; 
            }

            sb += (m.AMOUNT) ? m.AMOUNT.padLeft(10) : "".padLeft(10)
            sb += " ";

            sb += (m.CLIENT) ? m.CLIENT.padLeft(7) : "".padLeft(7)
            sb += "   ";

            if (m.CLIENT.isInteger())
            {
                intValue = m.CLIENT.toInteger();
                sb += getName(intValue).padRight(30)
            }
            else
            {
                sb += "".padRight(30)
            }
            sb += " ";
            sb += (m.REASON) ? m.REASON.padRight(28) : "".padRight(28)

            sb += " ";
            sb += (m.NAME) ? m.NAME.padRight(22) : "".padRight(22)

            _resultArea.append(sb); 
            _resultArea.append("\n"); 
        } // end of each

        total.eachWithIndex{e,ix->        
            _resultArea.append("\n                          ${cvtCcy(ix)} Total:"+e.toString().padLeft(8)); 
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
        JFrame win = new CoreDisplay();
        win.setVisible(true);
/*
        int which = 7
        4.times{
	        win.loader(which++);
	        sleep(5000);
	    } // end of times
*/
        win.loader(10);

    }
}