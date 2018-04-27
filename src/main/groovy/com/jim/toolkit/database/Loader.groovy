package com.jim.toolkit.database;
//http://mrhaki.blogspot.fr/2010/01/groovy-goodness-check-if-string-is.html isInteger() etc

import java.util.Date;
import groovy.transform.*;
import groovy.beans.*
import com.jim.toolkit.tools.DateSupport;
import com.jim.toolkit.database.LoaderSupport;
import com.jim.toolkit.Cell;
import com.jim.toolkit.Cells;
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
 * Loader class description
 *
 * This is logic to load a Cell from plain .txt file
 *
 * with expected format:   4; "2017-01-19"; "C"; 25.78; 10; "false"; "Burgers R Us";
 */ 
 @Canonical 
 public class Loader
 {
   /** 
    * Variable name of current class.
    */  
    String classname = "Loader";

   /** 
    * a Date we can use to hold plain text dates in ISO format
    */  
    Date datex = Date.parse('yyyy-MM-dd', "2017-11-25" );
    
   /** 
    * Holds pieces of a string after spliting by blanks or seimcolons
    */  
    String[] tokens = [];
    
    /** an O/S specific location for the user's home folder name */ 
    String home = System.getProperty("user.home");   
     
   /** 
    *  true when tokens[] appear possible to make into a Cell
    */  
    boolean verified = false;
    
   /** 
    *  true when tokens[0] appears to hold a possible row Id number but this is optional
    */  
    boolean withId = false        

   /** 
    *  Cell object holding variables for a single transaction row
    */  
    Cell c        
    
   /** 
    *  DateSupport object to manage, verify and convert both ISO date strings & also dd/mm/ccyy
    */  
    DateSupport ds = new DateSupport();
    
   /** 
    *  LoaderSupport object to manage, verify and convert integer, ISO date strings & also BigDecimal values
    */  
	LoaderSupport ls = new LoaderSupport();

    /** a test string full of semi-colons */ 
    String semicolonsample = "4; 2017-01-19; C; 25.78; 10; false; Burgers R Us";

    /** a test string full of semi-colons including trailing ; */ 
    String semicolonsample2 = "4; 2017-01-19; C; 25.78; 10; false; Burgers R Us Again;";

    /** a test string full of semi-colons w/o trailing ; and no optional Id number to start */ 
    String semicolonsample3 = "2017-01-19; C; 25.78; 10; false; Burgers R Us Again";

    /** a test string with minimum number of semi-colons w/o trailing ; and no optional Id number to start */ 
    String semisample6 = " 2017-11-21; A; 0 ";

    /** a test string full of tokens divided by blanks and no optional Id number to start */ 
    String spacesample = "2017-01-21 A -1.23 64 true reason for deed";

    /** a test string full of tokens divided by blanks and an optional Id number to start */ 
    String spacesample2 = "78 2017-01-21 A -1.23 64 true Haircuts";
    
    /** a test string full of tokens divided by blanks and an optional Id number to start & multi-token reason */ 
    String spacesample3 = "78 2017-01-21 A -1.23 6 false Haircuts Are Us";

    /** a test string full of tokens divided by blanks and no optional Id number to start */ 
    String spacesample4 = " 2017-11-21 A 123.45 77 true Haircuts";

    /** a test string of a reduced set of tokens divided by blanks and no optional Id number to start */ 
    String spacesample5 = " 2017-11-21 A 123.45 77 true";

    /** a test string of a minimum set of tokens divided by blanks and no optional Id number to start */ 
    String spacesample6 = " 2017-11-21 A 123.45 ";

   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to initialize vars     */
    public Loader()
    {
        say "\nLoader constructor()"
        c = new Cell();
    } // end of constructor


   /** 
    * Method to count tokens in a ';' delimited string and if more than one then tokenize using semi-colons else use blanks.
    * 
    * @param b is a String to tokenize for our Cell object
    * @return integer count of the number of tokens found if dividable by semi-colons
    */     
    public int semis(String b)
    {
        tokens = b.trim().tokenize(";")
        return tokens.size();
    } // end of method
    

   /** 
    * Method to count tokens in a space delimited string and if more than one then tokenize using  blanks.
    * 
    * @param b is a String to tokenize for our Cell object
    * @return integer count of the number of tokens found if dividable by blanks
    */     
    public int blanks(String b)
    {
        tokens = b.trim().tokenize()
        return tokens.size();
    } // end of method
    

   /** 
    * Method to verify tokens are available in the correct number. The blanks() or semis() method must be 
    * called to fill the token[] list first
    *
    * Needs more work !!!
    * 
    * @return boolean true if the number of tokens are appropriate and well-ordered with a minimum of 3 tokens
    */     
    public boolean verify()
    {
        c = new Cell();
        verified = (tokens.size() < 3) ? false : true;
        
        if (verified) 
        {
        	// if first token is a date then optional Id is missing
        	// 2017-11-21; A; 0 
        	if ( ds.isIsoDate( tokens[0].trim() ) )
        	{
	            withId = false;
	            c.date = ds.getIsoDate();

        		if (tokens[1].trim().size() != 1) 
        		{ 
        			verified = false; 
        		} // A,B or C
        		else
        		{
        			c.type = tokens[1].trim() as Character 
        		}

    	        String bdv = tokens[2].trim(); // BigDecimal Value
    	        if (!ls.hasBigDecimal(bdv))
    	        { 
    	        	verified = false; 
    	        }
    	        else
    	        {
    	        	c.amount = new BigDecimal(bdv.toString())
    	        }

 				// client
         	    if (tokens.size() > 3 && !ls.hasInteger(tokens[3].trim() ) ) 
        	    { 
        	    	verified = false; 
        	    }
        	    else
        	    {
        	    	if (tokens.size() > 3) { c.number = tokens[3].trim().toInteger() }
        	    } // end of else


        	    // check true false
        	    if (tokens.size() > 4)
        	    {
        	    	String tf = tokens[4].trim().toLowerCase();
        	    	if (tf!="true" && tf!="false") 
        	    	{ 
        	    		verified = false; 
        	    	}
        	    	else
        	    	{
        	    		c.flag = (tf=="true")?true:false; 
        	    	}
        	    } // end of if 
        	}
        	else
        	{
        		// else first token is an integer and thus the optionl Id
        		// "78 2017-01-21 A -1.23 6 false Haircuts Are Us"
	            withId = true; 
	            if (!ls.hasInteger(tokens[0].trim() ) ) { verified = false; } // Id

				if (!ls.hasISODate( tokens[1].trim() ) ) { verified = false; } // Date

    	        String bdv = tokens[3].trim(); // BigDecimal Value amount
				if (!ls.hasBigDecimal(bdv)) { verified = false; }

        	    if (tokens.size() > 3 && !ls.hasInteger(tokens[4].trim() )) { verified = false; } // client

        	    // check true false
        	    if (tokens.size() > 4)
        	    {
        	    	String tf = tokens[5].trim().toLowerCase();
        	    	if (tf!="true" && tf!="false") { verified = false; }
        	    } // end of if 
            } // end of else

        } // end of else verified
         
        return verified;
    } // end of method
    
    
   /** 
    * Method to convert token values into Cell values - needs more work
    * 
    * @return boolean true if the token values were stored in the appropriate Cell variable
    */     
    public boolean convert()
    {
        boolean ok = verify();
        if (ok)
		{	// if first token is an integer ass ume it's a row id number
			switch (withId) {
				case true : 
					c.id = tokens[0].trim() as Integer; 
					if ( ds.isIsoDate( tokens[1].trim() ) ) { c.date = ds.getIsoDate(); } 
					break;
				default : 
					break;

			} // end of switch
		} // end of if

        return ok;
    } // end of method


   /** 
    * Method to read all Cell objects from persistent store into our list.
    * 
    * @return int number of rows loaded from Acctg.txt
    */     
    public Cells load()
    {
        def file3 = new File('Acctg.txt')
        H2TableSupport ts = new H2TableSupport()

	    // Use a reader object:
    	int count = 0
        int max = ts.max();
        max+=1;
         
        def line
        def txs 
		Cells group = new Cells();

        if (file3.exists())
        {
        	file3.withReader { reader ->
            	while (line = reader.readLine()) {
                	if (line.trim().startsWith("//"))
                	{
                    	println line;
                	}
                	else
                	{
                    	print "\nCells.load() ="
                    	println line;

                    	// 1; "2013-03-02"; "B"; 121.44; 2; "Pension"; "true";
                    	txs = line.split(';')
                    	println "... txs.size()="+txs.size();
                    
	                    def t1 = txs[0].trim(); // id

                	    // get date
	                    def t2 = txs[1].trim().substring(1); // date
	                    int ct2 = t2.indexOf('"');
    	                if (ct2>-1) { t2 = t2.substring(0,ct2); }
        	            //def dt = cvt(t2)
        	            def dt = new Date(); // fix this later ???
        	            if (ds.isIsoDate(t2))
        				{
            				dt = ds.getIsoDate();
        				}

            	        println "... and t2 date of '${t2}' gave date:"+dt.toString();
                    
                	    // get type
                    	def t3 = txs[2].trim().substring(1,2); // type
                    	println "... t3 type=|${t3}|"

                    	// get amount
                    	println "... amt txs[3]=|${txs[3].trim()}|"  // amount
                    	BigDecimal t4 = txs[3].trim() as BigDecimal;
                    	println "... t4 amt=|${t4}|"
                    
	                    // get number
    	                println "... client txs[4]=|${txs[4].trim()}|"  // client number
        	            BigDecimal t5 = txs[4].trim() as BigDecimal;
            	        println "... client num=|${t5}|"

	                    // get flag
    	                def t6 = txs[5].trim().substring(1,2).toUpperCase();    // flag
        	            boolean f = (t6=='T') ? true : false;                        
            	        println "... flag=|${t6}| f=$f"

	                    // get reason
    	                def t7 = txs[6].trim().substring(1);    // reason 
        	            def ct7 = t7.indexOf('"');
            	        if (ct7>-1) { t7 = t7.substring(0,ct7); }
                	    println "... reason t7=|${t7}|"

	                    println "\n"

	                    c = new Cell([date:dt, type:t3, amount:t4, number:t5, flag:f, reason:t7])
    	                println c.toString();
        	            count++;
            	        group.add(c);
                	} // end of else
              	} // end of while
              
                group.sort();
        	} // end of reader
        } // end of if
        
        return group
   } // end of method



   /** 
    * Method to write all Cell objects to persistent store.
    * 
    * @return int number of rows written to Acctg.txt
    */     
    public int save(Cells cells)
    {
        def file3 = new File('Acctg.txt')
        int count = 0


        cells.each{e-> println "... e=|${e.toString()}|"
        } // end of each

        // Or a writer object:
        file3.withWriter('UTF-8') { writer ->
              cells.each{e->
                    e.id = count+=1;
                    writer.write(e.toOutput()+'\n')
              } // end of each
        } // end of file3

        return count;
    } // end of dump


   /** 
    * Method to write empty persistent store.
    * 
    * @return int number of rows written to Acctg.txt
    */     
    public int clear()
    {
        def file3 = new File('Acctg.txt')
        int count = 0
        Date x = new Date()

        // Or a writer object:
        file3.withWriter('UTF-8') { writer ->
                    writer.write("""// Cleared ${x} \n""")
        } // end of file3

        return count;
    } // end of dump


   /** 
    * Method to copy any rows from Acctg.txt held in Cells[] into the H2 'core' table.
    * 
    * @return integer count of the number of rows copied
    */     
    public int copy()
    {
        Cells sells = new Cells();
        sells = load();
        int count = sells.cells.size();
        H2RowSupport h2 = new H2RowSupport();
        sells.cells.each{e-> h2.add(e); }
        clear();
        return count;
    } // end of method
   
        
   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    @Override
    public String toString()
    {
        return """classname=Loader
datex=${datex.format("yyyy-MM-dd")}
tokens=${tokens}
home=${home}
verified=${verified}        
withId=${withId}
c=${c.toString()}
ds=${ds.toString()}
ls=${ls.toString()}
"""
    }  // end of string

   /** 
    * Method to print audit log.
    * 
    * @param the text to be said
    * @return void
    */     
    public void say(txt)
    {
        println txt;
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
        println "--- starting Loader ---"
        Loader obj = new Loader();
        def ct = obj.copy();
        println "... Loader copied ${ct} rows"
        println "--- the end of Loader ---"
    } // end of main

} // end of class