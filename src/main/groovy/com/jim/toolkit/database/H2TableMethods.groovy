package com.jim.toolkit.database;

//import com.jim.toolkit.database.H2;
import com.jim.toolkit.database.H2TableSupport;
import com.jim.toolkit.Cells;
import com.jim.toolkit.Cell;

import groovy.transform.*;
import groovy.sql.Sql

import java.sql.SQLException;
//import java.util.Date;
//import java.text.SimpleDateFormat
//import static java.util.Calendar.*

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
 * H2TableMethods class description
 *
 * This is code with all bits needed to ask H2 database to construct and keep data for a project
 */ 
 @Slf4j
 @Canonical 
 public class H2TableMethods
 {
    /** 
    * Variable holds connection to current H2 database instance and address pluss tables.
    */  
    H2TableSupport h2;

   /** 
    *  Variable sql is handle to H2 driver and it's database named 'acctg' in user.home folder.
    */  
    Sql sql = null;  
    
   /** 
    * Variable name of current class.
    */  
    String classname = "H2TableMethods";

   /** 
    * Variable set to true if logging printouts are needed or false if not
    */  
    Boolean logFlag = false;

   /** 
    * Variable to influence selection criteria for the load() method of Transaction, but NOT any table other than 'core'
    */  
    int loaderId = -1;


   // Stuff for currency treatment follows
   /** 
    * Map our internal ccy codes into ISO 4217 codes.
    */  
    Map ccys = [1:'EUR', 2:'GBP', 3:'USD']

   /** 
    * Map of our internal ccy codes 1,2 or 3 into a ISO 4217 currency codes as key to currency signs.
    */  
    Map ccysymbols = [1:'€', 2:'£', 3:'$']


   /** 
    * Map of ISO 4217 currency number to currency code.
    */  
    Map numbers = [978:'EUR', 826:'GBP', 840:'USD']


   /** 
    * Map of ISO 4217 currency code to currency number or vice versa.
    */  
    Map codes = ['EUR':978, 'GBP':826, 'USD':840, 978:'EUR',826:'GBP',840:'USD']

   /** 
    * Map of ISO 4217 currency codes as key to currency signs.
    */  
    Map symbols = ['EUR':'€', 'GBP':'£', 'USD':'$', 826:'£', 840:'$', 978:'€' ]


   /** 
    * Default Constructor 
    * 
    * @return H2TableMethods object
    */     
    public H2TableMethods()
    {
        say "... running H2TableMethods constructor written by Jim Northrop";
        h2 = new H2TableSupport();
	    sql = h2.sql;
    } // end of constructor


   /** 
    * Non-Default Constructor 
    * 
    * @return H2TableMethods object
    */     
    public H2TableMethods(boolean tf)
    {
        logFlag = tf;
        say "... running H2TableMethods constructor written by Jim Northrop";
        h2 = new H2TableSupport();
        sql = h2.sql;
    } // end of constructor


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


    //======================================================================
    // Data support methods follow

   /** 
    * Method to copy H2 database 'core' transaction table rows into a Cells container.
    * 
    * @return message tells the fate of this request for table selection
    */     
    public Cells load()
    {
        say "... load() will choose from table 'core'";
        String message ="";
        Cells obj = new Cells();

        try
        {
            Cell c = null;

            // get Transactions in date sequence by default
            String stmt = """SELECT * FROM core ORDER BY CCY, DATE"""
            def ok = (loaderId < 0) ? false : true;

            // override to get Transactions for one ID number where zero or greater
            if (ok) { stmt = """SELECT * FROM core  ORDER BY CCY, DATE WHERE ID = ${loaderId}""" }

            int count = 0;
            Map m = [:]

            sql.eachRow(stmt){row->
                    count+=1;
                    say "... row:"+row.toString();

                    // create a Cell with empty map
                    c = new Cell(m);

                    c.id = row.id;
                    c.date = row.date;
                    c.type = row.type;
                    c.amount = row.amount;
                    c.ccy = row.ccy;
                    c.client = row.client;
                    c.flag = row.flag;
                    c.reason = row.reason;
                    c.name = row.name;
                    obj.add(c);
            };
            
            message = "... selected ${count} rows from H2 database table 'core' ok"
            say message;
        }
        catch (SQLException e) {
            message = "Exception Message :" + e.getLocalizedMessage();
            say "... exception :"+message;
        }       
        catch (Exception x)
        {
            message = "'core' table not found to select rows from - or problem:"+x.message
            say "... exception :"+message;
        } // end of catch

        return obj;
    }  // end of method



   /** 
    * Method to choose H2 database current table rows depending on ID.
    * 
    * @param dbtable holds name of database table to be read
    * @param loaderId used in pick() method only to influence sql stmt to pick all rows for this id
    * @return obj holds row found for this Id
    */     
    public pick(String dbtable, int loaderId)
    {
        String message ="";
        def obj=null;

        try
        {
            String stmt = """SELECT * FROM ${dbtable} WHERE ID = ${loaderId}""";

            int count = 0;
            //Map m = [:]
            obj = sql.firstRow(stmt)
            message = "... selected id ${loaderId} row from H2 database table ${dbtable} ok"
        }
        catch (SQLException e) {
            message = "Exception Message :" + e.getLocalizedMessage();
        }       
        catch (Exception x)
        {
            message = "${dbtable} table not found to pick id ${loaderId} row from - or problem:"+x.message
        } // end of catch

        say message;
        return obj;
    }  // end of method


    //======================================================================
    // start of methods to query metadata about rows in this table


   /** 
    * Method to see number of rows in H2 database default table rows.
    * 
    * @param dbtable holds name of database table to be read
    * @return int tells how many rows found in this table
    */     
    public int count(String dbtable)
    {
        String message ="";
        int count = 0;

        try
        {
            String stmt = """SELECT COUNT(*) as num FROM ${dbtable}"""
            count = sql.firstRow(stmt).num
            message = "... selected ${count} rows from H2 database table ${dbtable} ok"
            say message;
        }
        catch (SQLException e) {
            message = "Exception Message :" + e.getLocalizedMessage();
            say message;
            count = -1;
        }       
        catch (Exception x)
        {
            message = "${dbtable} table not found to select rows from - or problem:"+x.message
            say message;
            count = -1;
        } // end of catch

        return count;
    }  // end of method
    

   /** 
    * Method is Table-level to find maximum value of id column from defined H2 database table if it is found.
    * 
    * @param dbtable holds name of database table to be read
    * @return integer is maximum id number found in any row in this 'name' table
    */     
    public int max()
    {
        return max('core');
    }  // end of method


   /** 
    * Method is Table-level to find maximum value of id column from defined H2 database table if it is found.
    * 
    * @param dbtable holds name of database table to be read
    * @return integer is maximum id number found in any row in this 'name' table
    */     
    public int max(String dbtable)
    {
        if (dbtable==null) { println "\n\nint max(String ${dbtable}) is null \n\n"}
        int max = 0;
        String message ="";

        try
        {
            String stmt = "select id from "+ dbtable + " order by id desc "
            
            // get first row of id descending seq.i.e. the highest row id currently in table
            def res = sql.firstRow(stmt)
            if (res==null)
            {
                max = -1;
                message = "... ${dbtable} table does not exist";
                say message;
            }
            else
            {
                max = res.id;
                message = "... max Id :[${max}]"                      
            }
        }
        catch (SQLException e) {
            message = "Exception Message :" + e.getLocalizedMessage();
            say message;
            max = -1;
        }       
        catch (Exception x)
        {
            message = "${dbtable} table failed to show: caused by problem:"+x.message
            say message;
            max = -1;
        } // end of catch

        say message;
        return max;
    }  // end of method


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
    * Method is to examine given string for known client keys in list and return a Map of discovered values
    * 
    * @param text holds a row like a Map as returned from sql select
    * @return Map of values for known keys found in list
    */     
    public Map decode(String text)
    {    
        Map m = [:] 
        String ans="";
        ans= text.trim();    
        int j = ans.indexOf('[');
        int je = ans.lastIndexOf(']');
        if (j>-1 && je>j) { ans = ans.substring(j+1,je) }

        ['ID','CCY','CLIENT','FLAG','NAME','AMOUNT','REASON','DATE','TYPE'].each{e->
            j = ans.indexOf(e);     
            //print "... e="+e+" and j="+j+" =";
            def s = "";
            if (j > -1)
            {
                j+=e.size()+1;
                s = ans.substring(j);
                je = s.indexOf(',')
                if (je > -1) { s = s.substring(0,je) }
                if (s.size() > 0)   
                { 
                    m[e]= s.collectReplacements(this.replace);
                }      
            } // end of if
            else
            {
                m[e]=""
            }
            
            //println "|"+s+"|";
        } // end of each

        return m;
    } // end of decode


    //------------------------------------------------------------------
    //  Currency methods follow
    //------------------------------------------------------------------


   /** 
    * Method to use our internal currency number like 2 as key to find a currency trading code like 'GBP'.
    * 
    * @param cn Value is integer currency number like 1 or 2 or 3. 
    * @return ISO 'EUR' code as string using currency number 1 as key
    */     
    public String translateCcy(int cn)
    {
        String s = (ccys[cn]) ? ccys[cn] : 0; 
        return s;
    }  // end of method


   /** 
    * Method to use our internal currency number like 2 as key to find a currency trading symbol like '£'.
    * 
    * @param cn Value is integer currency number like 1 or 2 or 3. 
    * @return ISO '€' symbol as string using currency number 1 as key
    */     
    public String translateCcyToSymbol(int cn)
    {
        String s = (ccysymbols[cn]) ? ccysymbols[cn] : '€'; 
        return s;
    }  // end of method


   /** 
    * Method to find currency symbol £ for a currency trading symbol like GBP.
    * 
    * @param cn Value is string currency trading symbol like GBP. 
    * @return Char translation of string 'GBP' into currency symbol '£''
    */     
    public Character getSymbol(String cn)
    {
        Character s = (symbols[cn]) ? symbols[cn] : "?"; 
        return s;
    }  // end of 


   /** 
    * Method to find currency number like 826 for a currency trading code like 'GBP'.
    * 
    * @param cn Value is string currency trading symbol like GBP. 
    * @return ISO number for string 'GBP' giving currency number 826
    */     
    public int getNumber(String cn)
    {
        int s = (codes[cn]) ? codes[cn] : 0; 
        return s;
    }  // end of method


   /** 
    * Method to use currency number like 826 as key to find a currency trading code like 'GBP'.
    * 
    * @param cn Value is integer currency number like 978. 
    * @return ISO 'EUR' code as string using currency number 978 as key
    */     
    public String getCode(int cn)
    {
        String s = (numbers[cn]) ? numbers[cn] : 0; 
        return s;
    }  // end of method


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=${classname}
sql=${sql.toString()}
H2TableSupport=${h2}
"""
    }  // end of string


   /** 
    * Method to print audit log.
    * 
    * @param the text to be said
    * @return void
    */     
    public void say(def txt)
    {
        if (logFlag) { log.info txt.toString(); }
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
        println "--- starting H2TableMethods ---"

        // work on 'core' table first
        H2TableMethods obj = new H2TableMethods(true);
	    println "... H2TableMethods = [${obj.toString()}]"
	    
        //println "... count('core') how many rows :"+obj.count('core');
        //println "... max() Id:"+obj.max('core');
        Cells cells = obj.load();
        println "... Cells.size():"+cells.size();        

/*
		// ----------------------------------------------------------------------------------
		// try using another table named 'Fred'
        obj = new H2TableMethods(true);
        println "... running setUp of Fred:"+obj.setUp('Fred',"id int, date date, type char") 

        String stmt = "INSERT INTO Fred values(:id, :date, :type)"
        obj.sql.execute(stmt, [id:2, date:new Date(), type:'Q'])

        println "... count('Fred') how many rows :"+obj.count('Fred');

        def sells = obj.pick('Fred',2);
        println sells;
        sells.each{ println "... ->"+it.toString();}

        println "... max() Id:"+obj.max('Fred');
        println obj.h2.drop('Fred');           

        // ----------------------------------------------------------------------------------
        // try to decode a row returned from sql select
        println ""
        def txt = "[ID:44, FLAG:true, NAME:Forty Four, REASON:B/C §±!@£%^&*()_+[]{};':|`~]"
        Map m = obj.decode(txt)
        println "\nmap="+m;

        // ----------------------------------------------------------------------------------
        // try to do currency checks        
        def x = obj.getCode(978)
        obj.say "... getCode(int 978) gives us |${obj.getCode(978)}| CCY"
        obj.say "... getSymbol(${x}) gives us |${obj.getSymbol(x)}|"

        x = obj.getCode(840)
        obj.say "... getCode(int 840) gives us |${obj.getCode(840)}| CCY"
        obj.say "... getSymbol(${x}) gives us |${obj.getSymbol(x)}|"
        obj.say "... getNumber(${x}) gives us |${obj.getNumber(x)}|"
        
        x = obj.getCode(766)
        obj.say "... getCode(int 766) gives us |${obj.getCode(766)}| CCY"
        obj.say "... getSymbol(${x}) gives us |${obj.getSymbol(x)}| - missing ?"
        obj.say ""

    	x = obj.translateCcy(1)
        obj.say "... translateCcy(int 1) gives us |${x}| Currency"
	    x = obj.translateCcyToSymbol(1)
        obj.say "... translateCcyToSymbol(int 1) gives us |${x}| Currency"

    	x = obj.translateCcy(2)
        obj.say "... translateCcy(int 2) gives us |${x}| Currency"
	    x = obj.translateCcyToSymbol(2)
        obj.say "... translateCcyToSymbol(int 2) gives us |${x}| Currency"
*/
        println "--- the end of H2TableMethods ---"
    } // end of main

} // end of class


/* - http://mrhaki.blogspot.fr/2016/06/groovy-goodness-turn-map-or-list-as.html
// Original Map structure.
def original = [name:mrhaki, age: 42,reason:'this is GR8' ]
 
// Turn map into String representation:
// [name:mrhaki, age:42]
def mapAsString = original.toMapString()
println "mapAsString=|${mapAsString}|"
 
def map =
    // Take the String value between
    // the [ and ] brackets.
    mapAsString[1..-2]
        // Split on , to get a List.
        .split(', ')
        // Each list item is transformed
        // to a Map entry with key/value.
        .collectEntries { entry ->
            def pair = entry.split(':')
            [(pair.first()): pair.last()]
        }
println     mapAsString[1..-2]
        // Split on , to get a List.
        .split(', ');
         
 
assert map.size() == 3
assert map.name == 'mr haki'
assert map.age == '42'
*/
