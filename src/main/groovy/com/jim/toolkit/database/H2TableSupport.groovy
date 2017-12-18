package com.jim.toolkit.database;

import com.jim.toolkit.database.H2;
import com.jim.toolkit.Cells;
import com.jim.toolkit.Cell;

import groovy.transform.*;
import groovy.sql.Sql

import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat
import static java.util.Calendar.*

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
 * H2TableSupport class description
 *
 * This is code with all bits needed to ask H2 database to construct and keep data for a project
 */ 
 @Canonical 
 public class H2TableSupport
 {
    /** an O/S specific char. as a file path divider */
    String fs = java.io.File.separator;

    /** an O/S specific location for the user's home folder name */ 
    String home = System.getProperty("user.home");
    
    /** 
    * Variable holds connection to current H2 database instance and address.
    */  
    H2 h2 = new H2();

   /** 
    *  Variable sql is handle to H2 driver and it's database named 'acctg' in user.home folder.
    */  
    Sql sql = null;  
    
   /** 
    * Variable name of current class.
    */  
    String classname = "H2TableSupport";

   /** 
    * Variable name of current H2 table.
    */  
    String dbtable = "core";

   /** 
    * Variable describing data values to be built into new H2 table.
    */  
    String variables = "id int auto_increment, date date, type char, amount DECIMAL(20, 2), number int, flag boolean, reason varchar ";


   /** 
    * Default Constructor 
    * 
    * @return H2TableSupport object
    */     
    public H2TableSupport()
    {
        //say "... running H2TableSupport constructor written by Jim Northrop from home="+home;
	    sql = h2.sql;
    } // end of constructor


   /** 
    * Non-Default Constructor 
    *
    * new db value alters future non-parameter methods to use this one
    * 
    * @param db holds string name of H2 database table to used in this session
    * @return H2TableSupport object
    */     
    public H2TableSupport(String db)
    {
        //say "... running H2TableSupport constructor written by Jim Northrop from home="+home;
        sql = h2.sql;
        dbtable = db;
    } // end of constructor


   /** 
    * Method is Table-level to build default named H2 database table if it is not found.
    * 
    * Uses the current values of 'dbtable' and 'variables' to build an Sql create stmt;
    *
    * @return message tells the fate of this request for table creation
    */     
    public String create()
    {
        String message ="";

    	try
    	{
    		String stmt = """CREATE TABLE IF NOT EXISTS ${dbtable} ( ${variables} )"""
            //println "... create:[${stmt}]"      
    		sql.execute(stmt)
	   	    message = "... created H2 database table ${dbtable} ok"
	    }
        catch (SQLException e) {
            message = "Exception Message " + e.getLocalizedMessage();
            say message;
        } 	    
        catch (Exception x)
	    {
		    message = "${dbtable} table already found or failed to create: caused by problem:"+x.message
            say message;
        } // end of catch

        return message;
    }  // end of method


   /** 
    * Method is Table-level to build H2 database table if it is not found.
    *
    * new db value alters future non-parameter method to use this one
    *
    * builds new table with schema layout in 'variables' which goes within the () of an sql stmt;
    * 
    * @param db holds string name of H2 database table to build
    * @return message tells the fate of this request for table creation
    */     
    public String create(db)
    {
        dbtable = db;
        return create();
    }  // end of method


   /** 
    * Method is Table-level to build H2 database table if it is not found.
    *
    * new db value alters future non-parameter method to use this one
    * 
    * @param db holds string name of H2 database to be built
    * @param declareString holds sql string of variables to be included in new H2 database table.
    * @return a message telling the fate of this request for table 'db' creation using parms
    */     
    public String create(String db, String declareString)
    {
        dbtable = db;
        variables = declareString;
        return create();
    }  // end of method


   /** 
    * Method is Table-level to remove H2 database table if it is found.
    *
    * new db value alters future non-parameter method to use this one
    * 
    * @param dbtable holds string name of H2 database table to kill 
    *           new db value alters future non-parameter method to use this one
    * @return message tells the fate of this request for table deletion
    */     
    public String drop(db)
    {
        dbtable = db;
        return drop();
    }  // end of method


   /** 
    * Method is Table-level to remove default H2 database table 'core' if it is found.
    * 
    * @return message tells the fate of this request for table deletion
    */     
    public String drop()
    {
        String message ="";

        try
        {
            String stmt = """DROP TABLE IF EXISTS ${dbtable}"""
            sql.execute(stmt)
            message = "... dropped H2 database table ${dbtable} ok"
        }
        catch (SQLException e) {
            message = "Exception Message " + e.getLocalizedMessage();
            say message;
        }       
        catch (Exception x)
        {
            message = "${dbtable} table not found to drop or caused problem:"+x.message
            say message;
        } // end of catch

        //say message;
        return message;
    }  // end of method



   /** 
    * Method to see H2 database specific table rows.
    *
    * new db value alters future non-parameter method to use this one
    * 
    * @param db holds string name of H2 database
    * @return message tells the fate of this request for table selection
    */     
    public select(String db)
    {
		dbtable = db;
		select();
    }  // end of method


   /** 
    * Method to see H2 database default table rows.
    * 
    * @return message tells the fate of this request for table selection
    */     
    public select()
    {
        String message ="";

    	try
    	{
    		String stmt = """SELECT * FROM ${dbtable}"""
            int count = 0;

			sql.eachRow(stmt){row->
                	count+=1;
					println "row:"+row.toString()
			};
			
			message = "... selected ${count} rows from H2 database table ${dbtable} ok"
		}
        catch (SQLException e) {
            message = "Exception Message :" + e.getLocalizedMessage();
            say message;
        }       
		catch (Exception x)
		{
			message = "${dbtable} table not found to select rows from - or problem:"+x.message
            say message;
		} // end of catch

        //say message;
        return message;
    }  // end of method


   /** 
    * Method to see H2 database table rows.
    * 
    * @param stmt holds sql string to run
    * @return message tells the fate of this request for table selection
    */     
    public String select(String stmt, Closure logic)
    {
        String message ="";

        try
        {
            //String stmt = """SELECT * FROM ${h2.dbtable} ORDER BY DATE"""
            message = stmt;
            int count = 0;

            h2.sql.eachRow(stmt)
            {row->
                count += 1;
                logic(row);
            };
            
            message = "... selected ${count} rows from H2 database table ${h2.dbtable} ok"
        }
        catch (SQLException e) {
            message = "Exception Message :" + e.getLocalizedMessage();
            say message;
        }       
        catch (Exception x)
        {
            message = "${h2.dbtable} table not found to select rows from - problem:"+x.message
            say message;
        } // end of catch

        //say message;
        return message;
    }  // end of method


   /** 
    * Method to see H2 database default table rows.
    * 
    * @param logic Closure to apply to each returned row of H2 database table
    * @return message tells the fate of this request for table selection
    */     
    public select(Closure logic)
    {
        String message ="";

        try
        {
            String stmt = """SELECT * FROM ${h2.dbtable}"""
            message =  stmt;
            int count = 0;

            h2.sql.eachRow(stmt){row->
                count+=1;
                logic(row)
            };
            
            message =  "... selected ${count} rows from H2 database table ${h2.dbtable} ok;"
        }
        catch (SQLException e) {
            message = "Exception Message :" + e.getLocalizedMessage();
            say message;
        }       
        catch (Exception x)
        {
            message = "${h2.dbtable} table not found to select rows from - or problem:"+x.message
            say message;
        } // end of catch

        //say message;
        return message;
    }  // end of method


   /** 
    * Method to copy H2 database default table rows into a Cells container.
    * 
    * @return message tells the fate of this request for table selection
    */     
    public Cells load()
    {
        String message ="";
        Cells obj = new Cells();
        Cell c = null;

        try
        {
            String stmt = """SELECT * FROM ${dbtable} ORDER BY DATE"""
            int count = 0;
            Map m = [:]
            sql.eachRow(stmt){row->
                    count+=1;
                    //println "row:"+row.toString()+" row.reason=|${row.reason}|";
                    c = new Cell(m);

                    c.id = row.id;
                    c.date = row.date;
                    c.type = row.type;
                    c.amount = row.amount;
                    c.number = row.number;
                    c.flag = row.flag;
                    c.reason = row.reason;
                    //c = new Cell(m);
                    obj.add(c);
            };
            
            message = "... selected ${count} rows from H2 database table ${dbtable} ok"
        }
        catch (SQLException e) {
            message = "Exception Message :" + e.getLocalizedMessage();
            say message;
        }       
        catch (Exception x)
        {
            message = "${dbtable} table not found to select rows from - or problem:"+x.message
            say message;
        } // end of catch

        //say message;
        return obj;
    }  // end of method


   /** 
    * Method is Table-level to find maximum value of id column from defined H2 database table if it is found.
    * 
    * @return integer is maximum id number found in any row in this 'name' table
    */     
    public max()
    {
    	int max = 0;
        String message ="";

        try
        {
        	String stmt = "select id from "+ dbtable + " order by id desc "
            // say "... max:[${stmt}]"      
            def res = sql.firstRow(stmt)
            max = res.id;
            message = "... max Id :[${max}]"      
        }
        catch (SQLException e) {
            message = "\nException Message :" + e.getLocalizedMessage();
            say message;
        }       
        catch (Exception x)
        {
            message = "\n${dbtable} table failed to show: caused by problem:"+x.message
            say message;
        } // end of catch

        //say message;
        return max;
    }  // end of method


   /** 
    * Method to construct a new  table.
    * 
    * @param the table to be built 
    * @return void
    */     
    public void setup(String table, String declareString)
    {
        dbtable = table;

        // throw away prior version
        say drop();

        // make a new version
        say create(table, declareString);        
    } // end of method


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=${classname}
user.home=${home}
dbtable=${dbtable}
java.io.File.separator=${fs}
sql=${sql.toString()}
h2=${h2}
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
        println "--- starting H2TableSupport ---"
        H2TableSupport obj = new H2TableSupport();

		if (args.size()>0) 
        {
            String s = args[0].toLowerCase();
            if (s=='drop')
            {
                obj.drop();
            }
            else
            {
                println "... args0="+s+" and table name will be "+args[0];
                obj.setup(args[0], args[1]);
            } // end of else

    	} // end of if
        
	    println "... H2TableSupport = [${obj.toString()}]"

        println "... maximum Id:"+obj.max();
        obj.select();   //load();
        println "--- the end of H2TableSupport ---"
    } // end of main

} // end of class
