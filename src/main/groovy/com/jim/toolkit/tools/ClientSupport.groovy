package com.jim.toolkit.tools;

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
 public class ClientSupport
 {
   /** 
    * Variable name of current class.
    */  
    String classname = "ClientSupport";

   /** 
    * Variable names of clients.
    */  
	Map names = [0:"None", 1:"UK Gov.", 2:"US Gov.", 3:"Esso", 4:"Exxon", 5:'Food', 7:"Tesco", 11:"UK Gov.", 12:"UK Gov."]

   /** 
    * Map of TXN reasons using client number above.
    */  
    Map titles = [1:"Jim's pension", 2:"Jim's pension", 5:"Ice Cream", 6:"Interest", 11:"Eve's pension", 12:"Eve's pension"]


   /** 
    * Map of TXN reasons using client number above.
    */  
    Map income = [1:true, 2:true, 11:true, 12:true, 5:false]


   /** 
    * Handle to connect this module to the H2 database services.
    */  	
    H2TableSupport h2;


   /** 
    * Flag to remember if we already connected this module to the H2 database 'client's services.
    */  	
    Boolean connected = false;

   /** 
    * Variable describing data values to be built into new H2 'clients' table.
    *
    * Boolean 'flag' is true for income clients or false for expense clients
    */  
    String variables = "id int, flag boolean, name varchar, reason varchar ";


   /** 
    * a unique integer value of a cell within all Cells[] array.
    */  
    Integer id=0;

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
    * Default Constructor 
    * 
    * @return ClientSupport object
    */     
    public ClientSupport()
    {
        println "running ClientSupport constructor written by Jim Northrop"
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
        // if client number not in names map, ck H2 clients table; 
        if (!names[cn])
        {
        	boolean b = getH2(cn)
        	if (b) { s = name; }
        }
        else
        {
	        s = names[cn]; 
	    }

        return s;
    }  // end of string


   /** 
    * Method to find reason descriptionfor a client number.
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
        return s;
    }  // end of string


   /** 
    * Method to find flag for a client number.
    * 
    * @param cn Value is integer client number. 
    * @return flag of client for this client number or false if none found
    */     
    public boolean getFlag(def cn)
    {
        boolean s = false;
        // if client number not in income map, ck H2 clients table; 
        if (!income[cn])
        {
            boolean b = getH2(cn)
            if (b) { s = this.flag; }
        }
        else
        {
            s = income[cn]; 
        }

        return s;
    }  // end of string


   /** 
    * Method to connect this module to the H2 database services for a client number.
    * 
	* String stmt = "DROP TABLE IF EXISTS clients"
    * h2.sql.execute(stmt)
	* delete(9);			
	* insert([9, true, 'Lloyds Bank', 'Interest received']);
	*
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
   		} // end of if
   		
   		//insert([10, true, 'Credit Agricole', '@Transferwise Deposit'])

		def row = h2.sql.firstRow('SELECT id, flag, name, reason FROM clients WHERE id='+client.toString())
		if (row==null) { return false; }

		this.id = row.id;
		this.flag = row.flag;
		this.name = row.name;
		this.reason = row.reason;

		//print "... client row:"+row.id;
		//row.values().each{q-> print "[${q}]"}
		//println ""

		return true;
    }  // end of method


   /** 
    * Method to add a client with this client's data;
    * 
    * def insertSql = 'INSERT INTO clients (id, flag, name, reason) VALUES (?, ?, ?, ?)'
	* def params = [10, true, 'Credit Agricole', '@Transferwise Deposit']
	* def keys = h2.sql.executeInsert insertSql, params
	*
    * @param cn List of four values to insert client of a number,then boolean plus two text values. 
	* 		like: [10, true, 'Credit Agricole', '@Transferwise Deposit']
    * @return true or false - true when this cn is in our income Map.
    */     
    public boolean insert(List cn)
    {
        def i = cn[0] as Integer
        if (inMap(i))
        { 
            println "... cannot insert new client ${i} as already there "
            return true
        }
        else
        {
    	    def insertSql = 'INSERT INTO clients (id, flag, name, reason) VALUES (?, ?, ?, ?)'
	   	    //def params = [10, true, 'Credit Agricole', '@Transferwise Deposit']
		    def keys = h2.sql.executeInsert insertSql, cn;
            println "... keys:"+keys;
            return (keys!=null) ? true: false; 
        } // end of else
    }  // end of method


   /** 
    * Method to update a client with this new client's data;
    *
    * @param cn List of four values to insert client of a number,then boolean plus two text values. 
    *       like: [10, true, 'Credit Agricole', '@Transferwise Deposit']
    * @return true or false - true when this row has been updated correctly.
    */     
    public boolean update(List cn)
    {
        def i = cn[0] as Integer
        if (inMap(i))
        { 
            println "... cannot insert new client ${i} as already there "
            String stmt = "UPDATE clients SET name='"+cn[2]+"' where id="+i;
            def keys = h2.sql.executeUpdate stmt;
            return (keys!=null) ? true: false; 
        }
        else
        {
            def insertSql = 'INSERT INTO clients (id, flag, name, reason) VALUES (?, ?, ?, ?)'
            //def params = [10, true, 'Credit Agricole', '@Transferwise Deposit']
            def keys = h2.sql.executeInsert insertSql, cn;
            println "... keys:"+keys;
            return (keys!=null) ? true: false; 
        } // end of else
    }  // end of method


   /** 
    * Method to see if this client number exists, and remove it if it does;
    * 
    * @param cn Value is integer client number to be removed. 
    * @return true or false - true when this 'cn' number is removed from 'clients' table.
    */     
    public boolean delete(int cn)
    {
		//h2.sql.execute "DELETE FROM clients WHERE id = 10"	
		h2.sql.execute "DELETE FROM clients WHERE id = "+cn.toString();
        return true;  //(income[cn]) ? true: false; 
    }  // end of method


   /** 
    * Method to see if this client number is known to us, either true or false;
    * 
    * @param cn Value is integer client number. 
    * @return true or false - true when this cn is known to us using our income Map or H2 clients table.
    */     
    public boolean inMap(def cn)
    {
    	// see if this number is in our income map
        boolean yn = (income[cn]) ? true: false; 

        // not in income map, so ck the H2 clients table for same & if found reply true
        if (!yn)
        {
        	boolean b = getH2(cn);
        	if (b) { yn = true; } // true=yes we know this one!
        }
    }  // end of method


   /** 
    * Method to find if this an income type client for a client number.
    * 
    * @param cn Value is integer client number. 
    * @return tf flag for this client number or false if none found or it's an expense
    */     
    public boolean isIncome(def cn)
    {
    	boolean tf = false
    	if (income[cn])
    	{
	        tf = income[cn]; 
    	} 
    	else
        {
        	boolean b = getH2(cn)
        	if (b) { tf = flag; }
        }

    	return tf;
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
income=${income}        
H2=${H2}
connected=${connected}
variables=${variables}
id=${id}
flag=${flag}
name=${name}        
reason=${reason}
"""
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
        println "--- starting ClientSupport ---"

        ClientSupport obj = new ClientSupport();
        
        println "... get name for client number one using obj.getClient() method:"+obj.getClient(1);        
        println "... get name for client number 2 using obj.getClient() method:"+obj.getClient(2);        
        println "... get name for client number 11 using obj.getClient() method:"+obj.getClient(11);        
        println "... get name for client number 12 using obj.getClient() method:"+obj.getClient(12);        
        println "... get name for unknown client number 74 using obj.getClient() method:"+obj.getClient(74);        
        println ""

        println "... get reason for client number one using obj.getReason() method:"+obj.getReason(1);        
        println "... get reason for client number two using obj.getReason() method:"+obj.getReason(2);        
        println "... get reason for client number 11 using obj.getReason() method:"+obj.getReason(11);        
        println "... get reason for client number 12 using obj.getReason() method:"+obj.getReason(12);        
        println "... get reason for unknown client number 74 using obj.getReason() method:"+obj.getReason(74);        
        println ""

        println "... get info for client number 1 :"+obj.getReason(1)+" from "+obj.getClient(1);        
        println "... get info for client number 2 :"+obj.getReason(2)+" from "+obj.getClient(2);
        println "... get info for client number 11 :"+obj.getReason(11)+" from "+obj.getClient(11);        
        println "... get info for client number 12 :"+obj.getReason(12)+" from "+obj.getClient(12);

        println ""
        println "... see if number 1 is known to us :"+obj.inMap(1);
        println "... see if number 5 is known to us :"+obj.inMap(5);
        println "... see if unknown number 74 is known to us :"+obj.inMap(74);

        println ""
        println "... see if number 1 is an income item :"+obj.isIncome(1);
        println "... see if number 5 is an income item :"+obj.isIncome(5);
        println "... see if unknown number 74 is in income map :"+obj.inMap(74);
        println ""

        println "... find reason from client 10:"+obj.getReason(10)
		boolean b = obj.getH2(10);
		if (b)
		{	
			String ty = (obj.flag) ? "income" : "expense";
			println "... getH2() found client "+obj.id+" "+ty+" from "+obj.name+" for "+obj.reason;
		}
		else
		{ 
			println "... did not find client 10 in H2"
		}

        obj.delete(10);
        obj.insert([10,true,"MBNA Bank","Mastercard"]);
        println "\n... now list all rows in 'clients' table -"

        H2TableSupport h2 = new H2TableSupport('clients');
        h2.select(){e-> println "... ClientSupport row:"+e.toString(); }

        println "--- the end of ClientSupport ---"
    } // end of main

} // end of class        