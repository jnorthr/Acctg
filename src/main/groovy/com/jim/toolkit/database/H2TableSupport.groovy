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

import groovy.util.logging.Slf4j;
import org.slf4j.*

import java.sql.ResultSet;

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
 @Slf4j
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
    * Variable describing data values to be built into new Transaction H2 'core' table.
    */  
    String variables = "id int auto_increment, date date, type char, amount DECIMAL(20, 2), ccy int, client int, flag boolean, reason varchar, name varchar ";


   /** 
    * Variable set to true if logging printouts are needed or false if not
    */  
    Boolean logFlag = false;
    

   /** 
    * A fixed constant to influence the ordering of rows chosen in a select() method
    * in this case - no ordering needed, just take rows as they come
    */  
    public static final int NONE = 0;

   /** 
    * A fixed constant to influence the ordering of rows chosen in a select() method
    * in this case - ID ordering needed, just take rows as they come in that sequence
    */  
    public static final int ID_SEQ = 1;

   /** 
    * A fixed constant to influence the ordering of rows chosen in a select() method
    * in this case - ordering by name is needed
    */      
    public static final int NAME_SEQ = 2;

   /** 
    * A fixed constant to influence the ordering of rows chosen in a select() method
    * in this case - ordering by boolean flag value is needed
    */  
    public static final int FLAG_SEQ = 3;

   /** 
    * A fixed constant to influence the ordering of rows chosen in a select() method
    * in this case - Currency CCY then ID ordering needed, just take rows as they come in that sequence
    */  
    public static final int CCY_ID_SEQ = 4;

   /** 
    * A fixed constant to influence the ordering of rows chosen in a select() method
    * in this case - Currency CCY then Name ordering needed, just take rows as they come in that sequence
    */  
    public static final int CCY_NAME_SEQ = 5;

   /** 
    * A fixed constant to influence the ordering of rows chosen in a select() method
    * in this case - Currency CCY then Flag then name ordering needed, just take rows as they come in that sequence
    */  
    public static final int CCY_FLAG_SEQ = 6;

   /** 
    * A fixed constant to influence the ordering of rows chosen in a select() method
    * in this case - ordering by client number is needed
    */      
    public static final int CLIENT_SEQ = 7;

   /** 
    * A fixed constant to influence the ordering of rows chosen in a select() method
    * in this case - ordering by client number is needed
    */      
    public static final int FLAG_CLIENT_SEQ = 8;

   /** 
    * A fixed constant to influence the ordering of rows chosen in a select() method
    * in this case - ordering by client number is needed
    */      
    public static final int CCY_CLIENT_SEQ = 9;

   /** 
    * A fixed constant to influence the ordering of rows chosen in a select() method
    * in this case - ordering by client number is needed
    */      
    public static final int CCY_DATE_TYPE_SEQ = 10;



   /** 
    * Default Constructor 
    * 
    * @return H2TableSupport object
    */     
    public H2TableSupport()
    {
        say "... running H2TableSupport constructor written by Jim Northrop from home="+home;
	    sql = h2.sql;
    } // end of constructor


   /** 
    * Non-Default Constructor 
    *
    * @param ok boolean to set logFlag for logging purposes
    * @return H2TableSupport object
    */     
    public H2TableSupport(boolean ok)
    {
        logFlag = ok;
        say "... running H2TableSupport Non-Default constructor written by Jim Northrop from home="+home;
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
        say "... running H2TableSupport constructor for table="+db;
        sql = h2.sql;
        dbtable = db;
    } // end of constructor


   /** 
    * Non-Default Constructor 
    *
    * new db value alters future non-parameter methods to use this one
    * 
    * @param db holds string name of H2 database table to used in this session
    * @param ok boolean to set logFlag for logging purposes
    * @return H2TableSupport object
    */     
    public H2TableSupport(String db, boolean ok)
    {
        logFlag = ok;
        say "... running H2TableSupport constructor for table="+db;
        sql = h2.sql;
        dbtable = db;
    } // end of constructor


   /** 
    * Method is Table-level to build 'dbtable' named H2 database table if it is not found.
    * 
    * Uses the current values of 'dbtable' and 'variables' to build an Sql create stmt;
    *
    * @return message tells the fate of this request for table creation
    */     
    public String create()
    {
    	return create(dbtable);
    }  // end of method


   /** 
    * Method is Table-level to build H2 database table named 'db' if not found.
    *
    * builds new table with schema layout in 'variables' which goes within the () of an sql stmt;
    * 
    * @param db holds string name of H2 database table to build
    * @return message tells the fate of this request for table creation
    */     
    public String create(String db)
    {
        String message ="";

    	try
    	{
    		String stmt = """CREATE TABLE IF NOT EXISTS ${db} ( ${variables} )"""
            say "... create:[${stmt}]"      
    		sql.execute(stmt)
	   	    message = "... created H2 database table ${db} ok"
	    }
        catch (SQLException e) {
            message = "Exception Message " + e.getLocalizedMessage();
            say message;
        } 	    
        catch (Exception x)
	    {
		    message = "${db} table already found or failed to create: caused by problem:"+x.message
            say message;
        } // end of catch

        return message;
    }  // end of method


   /** 
    * Method is Table-level to build H2 database table if it is not found.
    *
    * @param db holds string name of H2 database to be built
    * @param declareString holds sql string of variables to be included in new H2 database table.
    * 	like: id int auto_increment, date date, name varchar, amount DECIMAL(10, 2)
    * @return a message telling the fate of this request for table 'db' creation using parms
    */     
    public String create(String db, String declareString)
    {
        variables = declareString;
        return create(db);
    }  // end of method


   /** 
    * Method is Table-level to remove H2 database table if it is found.
    *
    * @param dbtable holds string name of H2 database table to kill 
    * @return message tells the fate of this request for table deletion
    */     
    public String drop(String db)
    {
        String message ="";

        try
        {
            String stmt = """DROP TABLE IF EXISTS ${db}"""
            sql.execute(stmt)
            message = "... dropped H2 database table ${db} ok"
        }
        catch (SQLException e) {
            message = "Exception Message " + e.getLocalizedMessage();
        }       
        catch (Exception x)
        {
            message = "${db} table not found to drop or caused problem:"+x.message
        } // end of catch

        say message;
        return message;
    }  // end of method


   /** 
    * Method is Database-level to find and report on all tables found within this H2 database when it is found.
    *
    * @return ResultSet of all tables found in this database>
    */     
    public ResultSet showAll()
    {
        String message ="";
        ResultSet rs = null;

        try
        {
            String stmt = "SHOW TABLES "
            rs = sql.execute(stmt)
            message = "... showing all H2 database tables"
        }
        catch (Exception x)
        {
            message = "... No tables found to SHOW or caused problem:"+x.message
        } // end of catch

        say message;
        return rs;
    }  // end of method

   /** 
    * Method is Table-level to remove default H2 database table 'core' if it is found.
    * 
    * @return message tells the fate of this request for table deletion
    */     
    public String drop()
    {
    	return drop(dbtable);
    }  // end of method


    // end of table create / delete methods
    //======================================================================
    // Data select methods follow


    /** 
    * Method to see H2 database 'db' table-specific rows.
    *
    * new db value alters future non-parameter method to use this one
    * 
    * @param db holds string name of H2 database table
    * @return list holding every row from the table selection in the order desired
    */     
    public java.util.ArrayList select(String db)
    {
		return select(db, H2TableSupport.NONE);
    }  // end of method


   /** 
    * Method to see how many rows in default H2 database table in a different sequence.
    * 
    * @param seq holds integer value zero thru four to identify the sequence of output
    * @return list holding every row from the table selection in the order desired
    */     
    public java.util.ArrayList select(int seq)
    {
    	return select(dbtable, seq)
    }  // end of method


    /** 
    * Method to see H2 database specific table rows without ordering.
    * 
    * @return list holding every row from the table selection in no particular order 
    */     
    public java.util.ArrayList select()
    {
        return select(dbtable, H2TableSupport.NONE);
    }  // end of method


    /** 
    * Method to see specific table rows in H2 database in a chosen order.
    *
    * new db value alters future non-parameter method to use this one
    * 
    * @param db holds string name of H2 database table
    * @param seq holds integer value zero thru four to identify the sequence of output
    * @return list holding every row from the table selection in the order desired
    */     
    public java.util.ArrayList select(String db, int seq)
    {
        java.util.ArrayList rows = []
        String message ="";
        int count = 0;
        String stmt = """SELECT * FROM ${db} """

/*
        if (db == 'core')
        {
            if (seq == H2TableSupport.NAME_SEQ || seq == H2TableSupport.FLAG_SEQ || seq == H2TableSupport.CCY_NAME_SEQ || seq == H2TableSupport.CCY_FLAG_SEQ)
            {
                seq = H2TableSupport.ID_SEQ;
            } // end of if 
        }
*/
        switch(seq)
        {
            // zero
            case H2TableSupport.NONE:
                                            break;
            // one
            case H2TableSupport.ID_SEQ:     stmt += "ORDER BY ID"
                                            break;
            // two
            case H2TableSupport.NAME_SEQ:   stmt += "ORDER by NAME, ID"
                                            break;
            // three
            case H2TableSupport.FLAG_SEQ:   stmt += "ORDER BY FLAG DESC, NAME "
                                            break;
            // four
            case H2TableSupport.CCY_ID_SEQ: stmt += "ORDER BY CCY, ID"
                                            break;
            // five
            case H2TableSupport.CCY_NAME_SEQ:stmt += "ORDER BY CCY, NAME, ID"
                                            break;
            // six
            case H2TableSupport.CCY_FLAG_SEQ:stmt += "ORDER BY CCY, FLAG DESC, NAME, ID"
                                            break;
            // seven
            case H2TableSupport.CLIENT_SEQ:   stmt += "ORDER by CLIENT, DATE"
                                            break;
            // eight
            case H2TableSupport.FLAG_CLIENT_SEQ:   stmt += "ORDER by DATE, FLAG DESC, CLIENT"
                                            break;
            // nine
            case H2TableSupport.CCY_CLIENT_SEQ:stmt += "ORDER BY CCY, DATE, CLIENT, ID"
                                            break;
            // ten
            case H2TableSupport.CCY_DATE_TYPE_SEQ:stmt += "ORDER BY CCY, DATE, TYPE"
                                            break;

        } // end of switch

        say "... H2TableSupport.select(String $db, int $seq) stmt=|${stmt}|"
    	try
    	{
			sql.eachRow(stmt){row->
                count+=1;
                say "... row="+row.toString();
                rows << row.toString();
            };

			say "... counted ${count} rows in ${db} "			
			message = "... selected ${count} rows from H2 database table ${db} ok"
		}
        catch (SQLException e) {
            message = "Exception Message :" + e.getLocalizedMessage();
            rows << message;
        }       
		catch (Exception x)
		{
			message = "${db} table not found to select rows from - or problem:"+x.message
            rows << message;
		} // end of catch

        say message;
        return rows;
    }  // end of method


   /** 
    * Method to see H2 database table rows.
    * 
    * It is job of 'logic' to return an a modified row to update the returnable 'rows' list
    *
    * @param stmt holds sql string to run
    * @param logic Closure to apply to each returned row of H2 database table
    * @return list holding every row from the provided statement for table selection
    */     
    public java.util.ArrayList select(String stmt, Closure logic)
    {
        String message ="";
        def rows = []

        try
        {
            //String stmt = """SELECT * FROM ${dbtable} ORDER BY DATE"""
            message = stmt;
            int count = 0;

            h2.sql.eachRow(stmt)
            {row->
                count += 1;
                rows << logic(row.toString());
            };
            
            message = "... selected ${count} rows from H2 database table ${dbtable} ok"
        }
        catch (SQLException e) {
            message = "Exception Message :" + e.getLocalizedMessage();
            //say message;
            rows << message;
        }       
        catch (Exception x)
        {
            message = "${dbtable} table not found to select rows from - problem:"+x.message
            //say message;
            rows << message;
        } // end of catch

        say message;
        return rows;
    }  // end of method


   /** 
    * Method to examine H2 database current table rows passing each row to 'logic'.
    *
    * It is job of 'logic' to update the returnable 'rows' list if needed
    * 
    * @param logic Closure to apply to each returned row of H2 database table
    * @return list holding any row that 'logic' has added to 'rows' list
    */     
    public java.util.ArrayList select(Closure logic)
    {
        String message ="";
        def rows = []

        try
        {
            String stmt = """SELECT * FROM ${dbtable}"""
            message =  stmt;
            int count = 0;

            h2.sql.eachRow(stmt){row->
                count+=1;
                logic(row.toString())
            };
            
            message =  "... selected ${count} rows from H2 database table ${dbtable} ok;"
        }
        catch (SQLException e) {
            message = "Exception Message :" + e.getLocalizedMessage();
            //say message;
            rows << message;
        }       
        catch (Exception x)
        {
            message = "${dbtable} table not found to select rows from - or problem:"+x.message
            //say message;
            rows << message;
        } // end of catch

        say message;
        return rows;
    }  // end of method

    // end of table data select methods
	//======================================================================


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=${classname}
user.home=${home}
logFlag=${logFlag}
dbtable=${dbtable}
variables=${variables}
java.io.File.separator=${fs}
sql=${sql.toString()}
h2=${h2}
"""
    }  // end of string


   /** 
    * Method to print audit log.
    * 
    * @return boolean flag thats true if code needs to write to log file
    */     
    public boolean needLogging()
    {
        return logFlag
    }  // end of method


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
        println "--- starting H2TableSupport ---"

        H2TableSupport obj = new H2TableSupport(true);

		if (args.size()>0) 
        {
            String s = args[0].toLowerCase();
            if (s=='drop')
            {
            	if (args.size()>1)
            	{
	                obj.drop(args[1]);
                    if ( obj.needLogging() ) { println obj.select(); }  
                    System.exit(0);
            	}
            	else
            	{
	                obj.drop();            		
                    if ( obj.needLogging() ) { println obj.select(); }  
                    System.exit(0);
            	}
            }
            else
            {
	            if (s=='make')
	            {
					H2TableMethods h2tm = new H2TableMethods();
    	            h2tm.setUp(args[1], 0);
                    System.exit(0);
    	        }
                else
                {
                    if (s=='show')
                    {
                        obj.select(args[1], 10);
                        System.exit(0);
                    } // end of if
                }
            } // end of else

    	} // end of if

        def rs = obj.showAll();
        println "... rs="
        rs.each{s-> obj.say "... showAll() row ="+ s; };

        // ----------------------------------------------------------------------------
        if ( obj.needLogging() ) { println "... H2TableSupport = [${obj.toString()}]" }
        if ( obj.needLogging() ) { println obj.select(); }   

        obj = new H2TableSupport('core',true);
        def results = obj.select(3);
        if ( obj.needLogging() ) 
        { 
            obj.say "... core results="+results.getClass(); 
            results.each{r-> println "... result:|${r.toString()}| " }
        } // end of if 

		// ----------------------------------------------------------------------------
        H2TableMethods h2tm = new H2TableMethods();
        if ( obj.needLogging() ) 
        { 
            obj.say "... running setUp of Fred:" 
        }
        
        h2tm.setUp('Fred',"id int, date date, type char")
		
        // try using another table named 'Fred'
        obj = new H2TableSupport('Fred',true);

        String stmt = "INSERT INTO Fred values(:id, :date, :type)"
        obj.sql.execute(stmt, [id:2, date:new Date(), type:'Q'])
        if ( obj.needLogging() ) 
        { 
            obj.say obj.select();   
            obj.say obj.drop('Fred');           
        } // end of if

        println "--- the end of H2TableSupport ---"
    } // end of main

} // end of class
