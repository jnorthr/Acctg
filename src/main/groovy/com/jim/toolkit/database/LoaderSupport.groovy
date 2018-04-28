package com.jim.toolkit.database;
//http://mrhaki.blogspot.fr/2010/01/groovy-goodness-check-if-string-is.html isInteger() etc

import java.util.Date;
import groovy.transform.*;
import groovy.beans.*
import com.jim.toolkit.tools.DateSupport;
import com.jim.toolkit.Cell;

import groovy.util.logging.Slf4j;
import org.slf4j.*
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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** 
 * LoaderSupport class description
 *
 * This is logic to load a Cell from plain .txt file
 *
 * with expected format:  78   2017-01-21  A    -1.23   1     6   true  'Haircuts Are Us'
 *          			  Id    D a t e  Type  Amount  CCY Client Flag   Reason
 */ 
 @Slf4j
 @Canonical 
 public class LoaderSupport
 {
   /** 
    * Variable name of current class.
    */  
    String classname = "LoaderSupport";

   /** 
    *  DateSupport object to manage, verify and convert both ISO date strings & also dd/mm/ccyy
    */  
    DateSupport ds = new DateSupport();
    
    //			   Id    D a t e  Type  Amount CCY  Client Flag   Reason
	//def tx = """ 78   2017-01-21  A   -1.23   1      6   true "'Haircuts Are Us'" | "'My name is George'"  """;
	String tx = """2017-01-21   A   -1.23   1   6 true  """.toString();

   /** 
    * Holds pieces of a string after spliting by blanks or seimcolons
    */  
    String[] tokens = [];

   /** 
    *  working variable used as an index into text string
    */  
	int i = -1;


   /** 
    *  working variable used as an index into text string
    */  
	int j = -1;


   /** 
    *  working variable used to hold reason comment text string
    */  
	String reason="";



   /** 
    *  working variable used to hold name alias text string
    */  
    String name="";


   /** 
    *  working variable used to hold text found before reason comment text string
    */  
	String t="";

   /** 
    *  working variable used to hold flag that is null by default but may become either true or false
    */  
	def tf=null;

   /** 
    * Variable name of current class.
    */  
    Cell c = new Cell();

   /** 
    * Variable set to true if logging printouts are needed or false if not
    */  
    Boolean logFlag = false;

   // =========================================================================
   /** 
    * Class constructor.
    *
    * @return LoaderSupport object 
    */
    public LoaderSupport()
    {
        say "\nLoaderSupport constructor()"
    } // end of constructor


   /** 
    * Class constructor.
    *
    * @return LoaderSupport object 
    */
    public LoaderSupport(boolean ok)
    {
        logFlag = ok;
        say "\nLoaderSupport debug constructor()"
    } // end of non-def constructor


   /** 
    * Method to count tokens in a comma delimited string and if more than one then tokenize using commas.
    * 
    * @param b is a String to tokenize for our Cell object
    * @return integer count of the number of tokens found if dividable by commas
    */     
    public int commas(String b)
    {
        tokens = b.trim().tokenize(",")
        return tokens.size();
    } // end of method
    

   // =========================================================================
   /** 
    * Load method to create a Cell but only keeps flag,name and reason while others 
    * must be set elsewhere using whats left of text string in this 't' variable
    *
    * @param tx is text string for one row to use to make a Cell. 
    *  like:  78   2017-01-21 A -1.23 6 true "'Haircuts Are Us'" "'Mad Max'"
    * @return Cell object 
    */
    public Cell loadReason(String txt)
    {
		say "... LoaderSupport.loadReason(${txt})" 
		c = new Cell();   
		tx = txt;

        // find index where either true or false tag exists in txt
		i = getLocation(txt);
		say "... getLocation() i=${i} tf=[${tf}] c="+c.toString()

        // if it exists, keep true/false flag
		if (tf != null) { c.flag = tf; }
		
        if (i > -1) 
		{ 
            // Method to keep leading piece of text before true/false flag in 't' var
    		t = getText(i, tx);
		    c.reason = getReason(i, tx);
            c.name = name;

    		say "... i=${i} text:[${t}] c.reason:[${c.reason}] c.name:[${c.name}] reason:[${reason}] name:[${name}]"
    		tx = t;
		    i = -1;
		} // end of if

        say "... load() returns partial c="+c.toString(); 

		return c;
    } // end of method

    
   // ======================================
   /** 
    * Method to find indexes for pieces of text after either true/false flag or return -1 if none.
    * 
    * @param tx is text string for one row to turn into a Cell. 
    * @return int returns -1 if none else positive value of index where keyword ends
    */     
    private int getLocation(String tex)
    {
		def txlc = tex.toLowerCase()
    	int m = txlc.indexOf("false")
    	int n = txlc.indexOf("true")
    	if (m > -1) 
    	{ 
        	m+=6
        	tf = false;
        	return m; 
    	}
    	else
    	{
        	if (n > -1)
        	{
            	n+=5;
            	tf = true;
            	return n;
        	}
    	} // end of else
    
	    return -1;
	} // end of method


   /** 
    * Method to get leading piece of text before true/false flag.
    * 
    * @param i is an integer value where true/false keyword exists in tx. 
    * @param tx is text string for one row to turn into a Cell. 
    * @return String returns text before true/false keyword appears in tx string
    */     
    private String getText(int i, String tx)
    {
	    say "... getText tx.size()=${tx.size()} i=${i}"
    	def piece=""
    	if (i > tx.size() ) { i = tx.size(); }
    	if (i > -1) piece = tx.take(i);  //.trim();
    	say "... getText piece=[${piece}] i=${i}"
    	return piece;
	} // end of method


   /** 
    * Method to get trailing piece of text after true/false flag.
    * 
    * @param i is an integer value where true/false keyword exists in tx. 
    * @param tx is text string for one row to use to make into a Cell. 
    * @return String value of all text after true/false keyword ends
    */     
	public String getReason(int i, String tx)
	{
    	reason="";
        name="";

	    if (i > tx.size() ) { i = tx.size(); }
    	if (i > -1) reason = tx.substring(i).trim();
    	say "... getReason reason=[${reason}] i=${i}"

        def ix = reason.indexOf('|')
        if (ix >-1) { name = reason.substring(ix+1); reason = reason.substring(0,ix);  }

    	reason = deQuote(reason);
    	reason = reason.trim();
        name = deQuote(name);
        name = name.trim();

    	say "... deQuote reason=[${reason}] & name=[${name}]"
    	return reason;
	} // end of method


   /** 
    * Method to get trailing piece of text after true/false flag.
    * 
    * @param w is text string for one token of text string used to make a Cell. 
    * @return Boolean returns true if token is capable of being translated into a non-decimal integer number 
    */     
	public Boolean hasInteger(String w)
	{
		return w.trim().isInteger()
	} // end of method


   /** 
    * Method to determine if a piece of text can be made from an ISO format text into Date object.
    * 
    * @param w is text string for one token of text string used to make a ISO Date object. 
    * @return Boolean returns true if token is capable of being translated into a Date or false if it is bad news
    */     
	public Boolean hasISODate(def w)
	{
		return ds.isIsoDate( w.trim() )
	} // end of method


   /** 
    * Method to determine if a piece of text can be made into a Date object.
    * 
    * @param w is text string for one token of text string used to make a Date object. 
    * @return Boolean returns true if token is capable of being translated into a Date or false if it is bad news
    */     
	public Boolean hasDate(def w)
	{
		return ds.isDate( w.trim() )
	} // end of method


   /** 
    * Method to determine if a piece of text can be made into a BigDecimal number object.
    * 
    * @param w is text string for one token of text string used to make a BigDecimal number object. 
    * @return Boolean returns true if token is capable of being translated into a BigDecimal number object
    */     
	public Boolean hasBigDecimal(String w)
	{
		return w.trim().isBigDecimal(); 
	} // end of method


   /** 
    * Method to remove outer double quotes " and/or outer single ' quotes from piece of text.
    * 
    * @param tx is text string  
    * @return String value of all text after quote marks are removed
    */     
	public String deQuote(String tx)
	{
    	String ans="";
    	ans= tx.trim();
    
	    int j = ans.indexOf('"');
    	int je = ans.lastIndexOf('"');
    	if (j>-1 && je>j) { ans = ans.substring(j+1,je) }

	    int i = ans.indexOf("'");
    	int ie = ans.lastIndexOf("'");
    	if (i>-1 && ie>j) { ans = ans.substring(i+1,ie) }
   		return ans;
	} // end of method


   /** 
    * Method to take a Map as a String then remove outer double quotes " and/or outer single ' quotes from piece of text.
    * 
    * @param tx is text string like [ID:2, DATE:2017-12-17, TYPE:B, AMOUNT:99.00, NUMBER:12, FLAG:false, REASON:Eve's pension, NAME:My name is Fred]  
    *        returned as row from sql select
    * @return Cell values of all decodeable pieces of text after quote marks are removed that can fit in a Cell
    */     
    public Cell reMap(String tx)
    {
        String ans="";
        ans= tx.trim();
    
    	// remove [ ] from string leaving text
        int j = ans.indexOf('[');
        int je = ans.lastIndexOf(']');
        if (j>-1 && je>j) { ans = ans.substring(j+1,je) }


        // divide text string into token list
        int ct = commas(ans)

        say "... reMap received:|${ans}|";
        Cell c = new Cell();

        // show each token stripped of quote marks
        tokens.eachWithIndex{ va, idx -> 
                //String x = obj.deQuote(va);
                //print "... $idx: |${va}|  --->";  
                String entry = va.trim();
                j = entry.indexOf(':');
                String ky = entry.substring(0, j).toLowerCase()
                def val = entry.substring(j +1).trim();
                say " ky=|${ky}| va=|${val}|";

                switch(ky) {
                        case 'id':     c.id = val as Integer
                        break;
                        case 'date':  
                                if (ds.isIsoDate(val))
                                {
                                    c.date = ds.getIsoDate()
                                } // end of if
                                break;

                        case 'type':    c.type = val as Character
                                break;

                        case 'amount':  c.amount = val as BigDecimal
                                break;

                        case 'ccy':     c.ccy = val as Integer
                                break;

                        case 'client':  c.client = val as Integer
                                break;

                        case 'flag':    c.flag = (val == 'false') ? false : true;
                                break;

                        case 'reason':  c.reason = val;
                                        reason = val;
                                break;

                        case 'name':    c.name = val;
                                        name = val;
                                break;
                } // end of switch

                //println ""

        } // end of each

        say "\n... Cell now ="+c.toString()
        return c;
    } // end of method


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=LoaderSupport
tx=${tx}
ds=${ds}
tokens=${tokens}
i=${i}
j=${j}
logFlag=${logFlag}
reason=${reason}
name=${name}
t=${t}
tf=${tf}
Cell=${c.toString()
}
""".toString()
    }  // end of string


   /** 
    * Method to print audit log.
    * 
    * @param the text to be said
    * @return void
    */     
    public void say(txt)
    {
        if (logFlag) { log.info txt; }
    }  // end of method


   // ======================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */     
    public static void main(String[] args)
    {
        println "--- starting LoaderSupport ---"
        LoaderSupport obj = new LoaderSupport(true);
        Cell s = obj.loadReason("789 2017-01-21 A -1.23 2  6 'true'  '  Hello Kids' | 'My name is  Fred'  ");

        // divide text string into token list
		obj.tokens = obj.tx.tokenize();

		// show each token stripped of quote marks
		obj.tokens.eachWithIndex{ va, idx -> 
				String x = obj.deQuote(va);
				println "... $idx: $x";  
		}

		println "... size():"+obj.tokens.size();
		println "... deQuote=[${obj.deQuote(obj.reason)}] i=${obj.i} j=${obj.j}\n"

		if (obj.tokens.size() > 0)
		{
			int ix = 0;
    		if (obj.hasInteger(obj.tokens[0])) 
    		{
        		int value = obj.tokens[0].trim() as Integer
        		ix++;
    		}
    		else
    		{
        		println "tokens[0] not an integer:[${obj.tokens[0]}]"
		    } // end of else

		    while(ix < obj.tokens.size())
    		{
		    	println "... ix=${ix} token="+obj.tokens[ix];
    			ix += 1;
		    }
		} // end of if

		println "\n... test hasInteger(78) method:"+obj.hasInteger("78");
		println "... test hasInteger(7 X) method:"+obj.hasInteger("7 X");
		println "... test hasInteger() method:"+obj.hasInteger(" ");
		println "... test hasInteger(.) method:"+obj.hasInteger(".");
		println "... test hasInteger(-4) method:"+obj.hasInteger("-4");

		println "... test hasBigDecimal(123.456) method:"+obj.hasBigDecimal("123.456")
		println "... test hasBigDecimal(-123) method:"+obj.hasBigDecimal("-123")
		println "... test hasBigDecimal(123.x56) method:"+obj.hasBigDecimal("123.x56")
		println "... test hasBigDecimal() method:"+obj.hasBigDecimal(" ")
		println "... test hasBigDecimal(.) method:"+obj.hasBigDecimal(".")
		println "... test hasBigDecimal(-4) method:"+obj.hasBigDecimal("-4")

		println "... test hasISODate(-4) method:"+obj.hasISODate("-4")
		println "... test hasISODate(2017) method:"+obj.hasISODate("2017")
		println "... test hasISODate(2017-) method:"+obj.hasISODate("2017-")
		println "... test hasISODate(2017-1) method:"+obj.hasISODate("2017-1")
		println "... test hasISODate(2017-1-1) method:"+obj.hasISODate("2017-1-1")
		println "... test hasISODate(2017/1/1) method:"+obj.hasISODate("2017/1/1")
		println "... test hasISODate(25-12-2017) method:"+obj.hasISODate("25-12-2017")

		println "... test hasDate(25-12-2017) method:"+obj.hasDate("25-12-2017")
		println "... test hasDate(25/12/2017) method:"+obj.hasDate("25/12/2017")
		println "... test hasDate(25-12-17) method:"+obj.hasDate("25-12-17")
		println "... test hasDate(12-25-17) method:"+obj.hasDate("12-25-17")
        println "... "+obj.toString();

        String testrow = "[ID:2, DATE:2017-12-17, TYPE:B, AMOUNT:99.00, CCY:1, CLIENT:12, FLAG:false, REASON:Eve's pension, NAME:My name is Jim]";
        s = obj.reMap(testrow);
		println "\n... LoaderSupport.toMap()="+s.toMap()
		println "... LoaderSupport.toString()="+s.toString()
		println "... LoaderSupport.toOutput()="+s.toOutput()


        println "--- the end of Loader ---"
    } // end of main

} // end of class
