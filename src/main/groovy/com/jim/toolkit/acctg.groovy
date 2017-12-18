package com.jim.toolkit;

import com.jim.toolkit.tools.DateSupport;
import com.jim.toolkit.tools.ClientSupport;

import groovy.transform.*;
import com.jim.toolkit.database.H2TableSupport;

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
    
    
   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to initialize vars     */
    public Acctg()
    {
        println "\nAcctg constructor()"

		cells = h2.load();
    } // end of constructor



   /** 
    * Method to display balance report.
    * 
    * @return final balance after totalling all transactions
    */     
    public BigDecimal report()
    {
    	//cells.each{ce-> println "... report ce="+ce.toString();}

		def sortedByDate = cells.sort();  //.toSorted { a, b -> a.d <=> b.d }
		BigDecimal balance = 0;
		def typeName = ['A':'Balance','B':'Income','C':'Expense']

		println "\n\n====================\n                     Balance Report        Dated: ${dt.format('dd MMMM, yyyy')}"
		println "\n  Row Purpose    Client          D a t e          Value        Balance     Reason"

		def sb = "";

		sortedByDate.each{e->

			def tn = typeName[e.type.toString()]
			def cy = (e.flag.toString().toLowerCase()=="true") ? "Y" : "N"; 
			String na = cs.getClient(e.number);

			sb += e.id.toString().padLeft(5)
			sb += " ";
			sb += tn.padRight(10)
			sb += " ";
			sb += na.padRight(12)
			sb += "  ";
			sb += e.date.format('dd-MM-yyyy').padRight(12)
			sb += e.amount.toString().padLeft(12)
			sb += " ";
			sb += cy.padRight(6)

	        switch(e.type)
    	    {
        	    	case 'C': balance -= e.amount;
            	    	      break;
            		case 'A': balance = e.amount;
                		      break;
            		case 'B': balance += e.amount;
                		      break;
        	} // end of switch

			sb += balance.toString().padLeft(8)
			sb += "  ";
			sb += "   ";
			na = cs.getReason(e.number);
			sb += (na.size() > 0) ? na.padRight(26) : e.reason.padRight(26);
		    println sb.toString();  // +' '+e.toString()+" -> Balance : "+balance;
		    sb = "";
        } // end of each

		println "\n                                                               Balance     "
		dt = new Date();
		println "Dated: ${dt.format('dd MMMM, yyyy')}"+ balance.toString().padLeft(46)
        return balance;
    }  // end of method


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        def s = """classname=Acctg
""";
        cells.each{s+=it.toString();s+='\n';}
        return s;
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
        println "Acctg = \n${obj.toString()}\n-------------------------\n"

  		def bal = obj.report()
		
        println "--- the end of Acctg ---"
    } // end of main

} // end of class