package com.jim.toolkit;

import groovy.util.logging.Slf4j;
import org.slf4j.*

import com.jim.toolkit.tools.ClientSupport;

import groovy.transform.*;
import com.jim.toolkit.database.H2TableSupport;
import com.jim.toolkit.database.H2TableMethods;

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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, eiter express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** 
 * Cells class description
 *
 * This is a class to hold a list of Cell objects
 *
 */
 @Slf4j 
 @Canonical 
 public class Acctg
 {
   /** 
    * Variable name of current class.
    */  
    String classname = "Acctg";

    /**  establish a support object to work with dates & times */
	ClientSupport cs = new ClientSupport();

    /**  establish new Date object */
	Date dt = new Date();

    /**  create object filled with all prior entries */
	Cells cells = new Cells();

    /**  establish Sql connection object to 'core' table */
	H2TableSupport h2 = new H2TableSupport();
    
    /**  establish connection object for load of 'core' table */
	H2TableMethods h2tm = new H2TableMethods();

   /** 
    * Variable holding report of most recent currency.
    */  
    String payload = "";

   /** 
    * Variable set to true if logging printouts are needed or false if not
    */  
    Boolean logFlag = false;
    
   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to initialize vars     */
    public Acctg()
    {
        if (logFlag) 
        { 
        	log.info "\nAcctg constructor()";
        }
		cells = h2tm.load();
    } // end of constructor


   /** 
    * Class constructor.
    *
    * non-default constructor to initialize vars     */
    public Acctg(Boolean ok)
    {
        logFlag = ok;

        if (logFlag) 
        { 
            log.info "\nAcctg constructor()";
        }

        cells = h2tm.load();
    } // end of non-default constructor


    /*
     * Save a string of data to a writer as external UTF-8 file with provided filename 'fn'
     */
    public boolean save(String fn, String data)
    {
        if (logFlag) 
        { 
            log.info "... saving "+fn;
        }
        
        try{
	        // Or a writer object:
    	    new File(fn).withWriter('UTF-8') { writer ->
        	        writer.write(data)
        	} // end of write    
	    }
	    catch (Exception e)
	    {
	        if (logFlag) 
    	    { 
        	    log.info "... save(${fn}) exception due to malformed groovy script :"+e.message;
        	}		    
	    	throw new RuntimeException(e.message) 
	    }
	    
        if (logFlag) 
        { 
            log.info "... saved ${data.size()} bytes to "+fn+'\n-----------------------\n';
        }
        return true;
    } // end of method


   /** 
    * Method to display balance report.
    * 
    * @return final balance after totalling all transactions
    */     
    public BigDecimal report(int curr)
    {
        if (logFlag) 
        { 
        	cells.each{ce-> log.info "... report ce="+ce.toString();}
        }

		def sb = "";

		def sortedByDate = cells.sortCCY(curr);  //.toSorted { a, b -> a.d <=> b.d }

		BigDecimal balance = 0;
		BigDecimal total = 0;
        String currency = cvtCcy(curr)

		def typeName = ['A':'Balance','B':'Income','C':'Expense']

        sb += "==                                      ${currency} Balance Report             \n"
        sb += ".${dt.format('dd MMMM, yyyy')}\n[source,groovy,linenums]\n----\n"
		sb += "Row Purpose    Client          D a t e          Value CCY     Balance         Total       Reason\n"


		sortedByDate.each{e->

			def tn = typeName[e.type.toString()]
			def cy = (e.flag.toString().toLowerCase()=="true") ? "Y" : "N"; 
			String na = cs.getClient(e.client);
            currency = cvtCcy(e.ccy)

			sb += e.id.toString().padLeft(3)
			sb += " ";
			sb += tn.padRight(10)
			sb += " ";
			sb += na.padRight(12)
			sb += "  ";
			sb += e.date.format('dd-MM-yyyy').padRight(12)
			sb += e.amount.toString().padLeft(12)
            sb += " ";
            sb += currency.padLeft(1)
			sb += cy.padLeft(4)

	        switch(e.type)
    	    {
        	    	case 'C': if (!e.flag) { balance -= e.amount; }
        	    			  total -= e.amount; 
            	    	      break;

            		case 'A': balance = e.amount;
            				  total =  e.amount;
                		      break;

            		case 'B': if (!e.flag) { balance += e.amount; }
            				  total += e.amount; 
                		      break;
        	} // end of switch


			sb += balance.toString().padLeft(8)
            sb += " ";
            sb += currency
			sb += "  ";
			sb += total.toString().padLeft(8)
            sb += " ";
            sb += currency
			sb += "   ";
			na = cs.getReason(e.client);
			sb += (na.size() > 0) ? na.padRight(26) : e.reason.padRight(26);
			sb += "\n";

		    payload += sb.toString();  // +' '+e.toString()+" -> Balance : "+balance;
		    sb = "";
        } // end of each

		//payload += "\n                                                               Balance     "
		dt = new Date();
		payload += "\n                                      Report Totals : "+ balance.toString().padLeft(15)+' '+currency+ total.toString().padLeft(10)+' '+currency

		payload += "\n----\n"

        return balance;
    }  // end of method



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
    

   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        def s = """classname=Acctg
dt=${dt}
cells=${cells}
h2=${h2}
h2tm=${h2tm}
logFlag=${logFlag}
""";
        return s.toString();
    }  // end of string

    
   // ======================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */     
    public static void main(String[] args)
    {
        println "--- starting Acctg ---"
        
        Acctg obj = new Acctg()
        //println "Acctg = \n${obj.toString()}\n-------------------------\n"

  		def bal = obj.report(1)
  		bal = obj.report(2)
  		bal = obj.report(3)
        String pl = "= Personal Reports\njnorthr <jim@google>\n:icons: font\n\n"+obj.payload;
  		obj.save("xxx.adoc", pl)
		
        println "--- the end of Acctg ---"
    } // end of main

} // end of class