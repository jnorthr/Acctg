package com.jim.toolkit.tools;

import com.jim.toolkit.database.H2TableSupport;
import com.jim.toolkit.database.H2TableMethods;
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
 * ClientSupport class description
 *
 * This is code with all bits needed to determine names of clients we deal with
 *
 */ 
 @Slf4j
 public class ClientSupport
 {
   /** 
    * Variable name of current class.
    */  
    String classname = "ClientSupport";

   /** 
    * Variable names of clients.
    */  
	Map names = [0:"None"]

   /** 
    * Map of TXN reasons using client number above.
    */  
    Map titles = [:]

   /** 
    * Handle to connect this module to the H2 database services.
    */  	
    H2TableSupport h2;

   /** 
    * Handle to connect this module to the H2 database logical services.
    */  	
	H2TableMethods h2tm;

   /** 
    * Flag to remember if we already connected this module to the H2 database 'client's services.
    */  	
    Boolean connected = false;

   /** 
    * Variable describing data values to be built into new H2 'clients' table.
    *
    * int NUMBER is 1 for 'EUR' ccy; or number 2 for 'GBP' ccy; or number 3 for 'USD' ccy
    * Boolean 'flag' is true for income clients or false for expense clients
    */  
    String variables = "id int, ccy int, number int, flag boolean, name varchar, reason varchar ";

   /** 
    * a unique integer value of a cell within all Cells[] array.
    */  
    Integer id=0;

   /** 
    * Integer variable using our internal codes 1,2 or 3 to describe the ISO currency number for this transaction.
    */  
    int ccy=1; // EUR

   /** 
    * Boolean variable describing a yes/no or true/false condition for this transaction.
    * true = income action while false = expense action
    */  
    Boolean flag = false;

   /** 
    * Text variable describing the author for this transaction.
    */  
    String name='';

   /** 
    * Text variable describing the reason for this transaction.
    */  
    String reason='';

   /** 
    * Boolean variable describing a yes/no or true/false condition for logging purposes.
    * true = write to logfile uing log.info methods
    */  
	boolean logInfo = false;
	
   /** 
    * Default Constructor 
    * 
    * @return ClientSupport object
    */     
    public ClientSupport()
    {
        say "running ClientSupport constructor written by Jim Northrop"
        boolean b = getH2(0)
    } // end of constructor

   /** 
    * Non-Default Constructor 
    * 
    * @param tf is a boolean flag to set the debug logging
    * @return ClientSupport object
    */     
    public ClientSupport(boolean tf)
    {
        logInfo = tf;
        say "running ClientSupport debug constructor written by Jim Northrop"
        boolean b = getH2(0)
    } // end of constructor


   /** 
    * Method to find name for a client number.
    * 
    * @param cn Value is integer client number. 
    * @return name of client for this client number or number if none foound
    */     
    public String getClient(def cn)
    {
        String s = "";
        name = "";

        // if client number in names map, use it else ck H2 clients table; 
        if (names[cn])
        {
            s = names[cn]; 
        }
        else
        {
            boolean b = getH2(cn)
            if (b) { s = name; }
	    }

        s = s.collectReplacements(this.replace);

        return s;
    }  // end of method


   /** 
    * Method to find reason description for a client number.
    * 
    * @param cn Value is integer client number. 
    * @return TXN reason for this client number or number if none foound
    */     
    public String getReason(def cn)
    {
        String s = "";
        if (!titles[cn])
        {
        	boolean b = getH2(cn)
        	if (b) { s = reason; }
        }
        else
        {
	        s = (titles[cn]) ? titles[cn] : ""; 
	    }

        this.reason = s.collectReplacements(this.replace);
        return s;
    }  // end of method


   /** 
    * Method to find income/expense flag for a client number. Defaults to false if Id in unknown.
    * 
    * @param cn Value is integer client number. 
    * @return flag of client for this client number or false if none found
    */     
    public boolean getFlag(def cn)
    {
        boolean s = false;
        
        // if client number is in H2 clients table, use that; 
        boolean b = getH2(cn)
        if (b) { s = this.flag; }
        
        return s;
    }  // end of method


   /** 
    * Method to find ISO currency number for a client number. Defaults to 1 if Id in unknown.
    * 
    * @param cn Value is integer client number. 
    * @return int value of client's currency number for this client number or 1 (Euro) if none found
    */     
    public int getCurrency(def cn)
    {
        int ccy = 1;

        // if client number is in H2 clients table, use that; 
        boolean b = getH2(cn)
        if (b) { ccy = this.ccy; }
        
        return ccy;
    }  // end of method


   /** 
    * Method to connect this module to the H2 database services for a client number.
    * 
	* String stmt = "DROP TABLE IF EXISTS clients"
    * h2.sql.execute(stmt)
	* delete(9);			
	* insert([9, 1, true, 'Lloyds Bank', 'Interest received']);
	*
    * @param client Value is integer client number. 
    * @return TXN reason for this client number or number if none foound
    */     
    public boolean getH2(int client)
    {
    	if (!connected)
    	{
    		connected = true;	
			h2 = new H2TableSupport();
			h2.create('clients', variables);
            h2.select('clients');
			h2tm = new H2TableMethods();
   		} // end of if
   		
   		//        Id  CCY  Flag   N a m e            R e a s o n
   		//insert([10,   1, true, 'Credit Agricole', '@Transferwise Deposit'])
        this.id = 0;
        this.ccy = 1; // 2= 'GBP'
        this.flag = false;

        this.name = "";
        this.reason = "";

		def row = h2.sql.firstRow('SELECT id, ccy, flag, name, reason FROM clients WHERE id='+client.toString())
		if (row==null) { return false; }

		this.id = row.id;
        this.ccy = row.ccy; // 2= 'GBP'
		this.flag = row.flag;

        this.name = row.name.collectReplacements(this.replace);
		this.reason = row.reason.collectReplacements(this.replace);

		say "... client row:"+row.id;
		//row.values().each{q-> print "[${q}]"}

		return true;
    }  // end of method



   /** 
    * Method to construct a new table with sql variable string removing any prior existing table of same name
    *
    * @param the table to be built 
    * @param sql syntax of new table so you can build tables of different column characteristics 
    * @return text description of what happened during create() method
    */     
    public String setUp(String table, String declareString)
    {
        // throw away prior version
        def msg = h2.drop(table);
        say msg;

        // make a new version
        msg = h2.create(table, declareString);   
        return msg;     
    } // end of method


   /** 
    * Logic to convert single quotes into unique char. for storage
    */
    def replacement = 
    {
        // Change single quote to ±
        if (it == /'/) {
            /±/
        } else {
            null
        }
    } // end of closure


   /** 
    * Logic to convert ± chars into single quote ' after storage
    */
    def replace = 
    {
        // Change double quote to eat
        if (it == /±/) {
            /'/
        } else {
            null
        }
    } // end of closure


   /** 
    * Method to add a client with this client's data;
    * 
    * def insertSql = 'INSERT INTO clients (id, ccy, flag, name, reason) VALUES (?, ?, ?, ?, ?)'
	* def params = [10, 1, true, 'Credit Agricole', '@Transferwise Deposit']
	* def keys = h2.sql.executeInsert insertSql, params
	*
    * @param cn List of five values to insert client of two numbers,then boolean plus two text values. 
	* 		like: [10, 1, true, 'Credit Agricole', '@Transferwise Deposit']
    * @return integer of Id just written or zero if unsuccessful.
    */     
    public Integer insert(List cn)
    {
        def i = cn[0] as Integer
        if (i==0)
        {
        	i = h2tm.max('clients') + 1;
        	cn[0] = i;
        } // end of if
        
        if (inMap(i))
        { 
            say "... cannot insert new client ${i} as already there "
            return -1
        }
        else
        {
            cn[3]=cn[3].collectReplacements(this.replacement)
            cn[4]=cn[4].collectReplacements(this.replacement)
    	
            def insertSql = 'INSERT INTO clients (id, ccy, flag, name, reason) VALUES (?, ?, ?, ?, ?)'
	   	    //def params = [10, 978, true, 'Credit Agricole', '@Transferwise Deposit']

		    def keys = h2.sql.executeInsert insertSql, cn;
            say "... keys:"+keys;
            return (keys!=null) ? i : 0; 
        } // end of else
    }  // end of method


   /** 
    * Method to update a client with this new client's data;
    *
    * @param cn List of four values to insert client of a number,then boolean plus two text values. 
    *       like: [10, 1, true, 'Credit Agricole', '@Transferwise Deposit']
    * @return integer of Id just written or zero if unsuccessful.
    */     
    public Integer update(List cn)
    {
        def i = cn[0] as Integer
        def num = cn[1] as Integer // Currency number like 1 for 978 / EUR

        cn[3]=cn[3].collectReplacements(replacement)
        cn[4]=cn[4].collectReplacements(replacement)

        say "... ClientSupport.update() for client ${i} "
        def stmt = """UPDATE clients SET CCY='${num}', FLAG='${cn[2]}', NAME='${cn[3]}', REASON='${cn[4]}' where ID=""" + i;

        say "... ClientSupport.update() stmt="+stmt.toString();
        def keys = h2.sql.executeUpdate stmt.toString();
        return (keys!=null) ? i : 0; 

    }  // end of method


   /** 
    * Method to see if this client number exists, and remove it if it does;
    * 
    * @param cn Value is integer client number to be removed. 
    * @return text message showing results of delete attempt
    */     
    public String delete(int cn)
    {
        String msg = "Id. ${cn} delete request was "
		def ok = h2.sql.execute "DELETE FROM clients WHERE id = "+cn.toString();
        return msg+= (ok) ? " not successful" : "successful";   
    }  // end of method


   /** 
    * Method to see if this client number is known to us, either true or false;
    * 
    * @param cn Value is integer client number. 
    * @return true or false - true when this cn is known to us using our income Map or H2 clients table.
    */     
    public boolean inMap(def cn)
    {
        return getH2(cn);
    }  // end of method


   /** 
    * Method to find if this an income type client for a client number.
    * 
    * @param cn Value is integer client number. 
    * @return tf flag for this client number or false if none found or it's an expense
    */     
    public boolean isIncome(def cn)
    {
    	boolean tf = false;
        boolean b = getH2(cn)
        if (b) { tf = flag; }

    	return tf;
    }  // end of method


   /** 
    * Method to find if this an income type client for a client number.
    * 
    * @param cn Value is integer client number. 
    * @return name of type so that if 'flag' is true then it's 'Income' else it's 'EXpense'
    */     
    public String getTypeName(def cn)
    {
        boolean b = isIncome(cn)
        return (b) ? "Income":"Expense";;
    }  // end of method


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=ClientSupport
names=${names}        
titles=${titles}        
H2=${h2}
H2TM=${h2tm}
connected=${connected}
variables=${variables}
id=${id}
ccy=${ccy}
flag=${flag}
name=${name}        
reason=${reason}
logInfo=${logInfo}
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
		if (logInfo)
		{
	        log.info txt;
	    }
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
        println "--- starting ClientSupport ---"
        ClientSupport obj = new ClientSupport(true);

        // ---------------------------------------------------------------------------------- 
        // this will remove then recreate clients table
        println "... running setUp of clients using .setUp() :"+obj.setUp('clients',obj.variables) 

        //work with client 999
        obj.delete(999);
        obj.insert([999, 3, true, "MBNA 'Bank'", "Master 'Card'"]);
        obj.say "\n... now list all rows in 'clients' table -"


        obj.say "... find reason from client 999 :"+obj.getReason(999)
        boolean b = obj.getH2(999);
        if (b)
        {   
            String ty = (obj.flag) ? "income" : "expense";
            obj.say "... getH2() found client "+obj.id+" "+obj.ccy+" "+ty+" from "+obj.name+" for "+obj.reason;
        }
        else
        { 
            obj.say "... did not find client 999 in H2"
        }

        def results = obj.h2.select('clients', 999);
        println "... clients results is a class of "+results.getClass();
        results.each{r-> println "... result:|${r.toString()}| " }
        
        obj.say "... get name for client number 999 using obj.getClient() method:"+obj.getClient(999);        
        obj.say "... get name for unknown client number 74 using obj.getClient() method:"+obj.getClient(74);        
        obj.say ""

        obj.say "... get reason for client number 999 using obj.getReason() method:"+obj.getReason(999);        
        obj.say "... get reason for unknown client number 74 using obj.getReason() method:"+obj.getReason(74);        
        obj.say ""

        obj.say "... see if number 1 is known to us :"+obj.inMap(1);
        obj.say "... see if number 999 is known to us :"+obj.inMap(999);
        obj.say "... see if unknown number 74 is known to us :"+obj.inMap(74);

        obj.say ""
        obj.say "... see if number 1 is an income item :"+obj.isIncome(1);
        obj.say "... see if number 5 is an income item :"+obj.isIncome(5);
        obj.say "... see if unknown number 74 is in income map :"+obj.inMap(74);
        
        obj.say "\n------------------------\n"

        // list all clients
        H2TableSupport h2 = new H2TableSupport('clients');
        h2.select(){e-> obj.say "... ClientSupport row:"+e.toString(); }

        println "--- the end of ClientSupport ---"
    } // end of main

} // end of class        
