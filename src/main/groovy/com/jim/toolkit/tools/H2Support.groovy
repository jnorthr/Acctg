package com.jim.toolkit.tools;

import groovy.transform.*;
import groovy.sql.Sql
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
 * H2Support class description
 *
 * This is code with all bits needed to ask H2 database to construct and keep data for a project
 *
 */ 
 @Canonical 
 public class H2Support
 {
    /** an O/S specific char. as a file path divider */
    String fs = java.io.File.separator;

    /** an O/S specific location for the user's home folder name */ 
    String home = System.getProperty("user.home");
    
    /** an O/S specific location for the user's H2 database */ 
    String address = "jdbc:h2:file:${home}/Dropbox/Projects/Acctg/.h2data/acctg;AUTOCOMMIT=ON";
 
   /** 
    * Variable sql is handle to H2 driver and it's database named 'acctg' in user.home folder.
    * "jdbc:h2:file:~/.h2data/acctg;AUTOCOMMIT=ON"
    */  
	Sql sql = Sql.newInstance(address, "sa", "sa", "org.h2.Driver")
    

    /** 
    * Variable datex of current today's date.
    */  
	Date datex = new Date();


   /** 
    * Variable name of current class.
    */  
    String classname = "H2Support";


   /** 
    * Variable name of current H2 table.
    */  
    String dbtable = "core";


   /** 
    * Default Constructor 
    * 
    * @return H2Support object
    */     
    public H2Support()
    {
        say "running H2Support constructor written by Jim Northrop from home="+home;
    } // end of constructor


   /** 
    * Method to build H2 database table if it is not found.
    * 
    * @return void
    */     
    public void create()
    {
    	try
    	{
    		String stmt = """CREATE TABLE IF NOT EXISTS ${dbtable} (id int auto_increment, dt date, type char, amount DECIMAL(20, 2), number int, purpose text, flag boolean )"""
			sql.execute(stmt)
			say "... create H2 database table ${dbtable} ok"
		}
		catch (Exception x)
		{
			say "${dbtable} table already found or create caused problem:"+x.message
		}
    }  // end of method


   /** 
    * Method to build H2 database table if it is not found.
    * 
    * @param db holds string name of H2 database
    * @return void
    */     
    public void create(db)
    {
    	try
    	{
    		String stmt = """CREATE TABLE IF NOT EXISTS ${db} (id int auto_increment, dt date, type char, amount DECIMAL(20, 2), number int, purpose text, flag boolean )"""
    		sql.execute(stmt)
			dbtable = db 
			say "... create H2 database table ${db} ok"
		}
		catch (Exception x)
		{
			say "${db} table already found or create caused problem:"+x.message
		}
    }  // end of method


   /** 
    * Method to remove H2 database table if it is found.
    * 
    * @param dbtable holds string name of H2 database
    * @return void
    */     
    public drop(db)
    {
    	try
    	{
    		String stmt = """DROP TABLE IF EXISTS ${db}"""
			sql.execute(stmt)
			dbtable = db
			say "... drop H2 database table ${db} ok"
		}
		catch (Exception x)
		{
			say "${db} table not found to drop or caused problem:"+x.message
		}
    }  // end of method


   /** 
    * Method to add row to in-use H2 database table.
    * 
    * @param txt holds string used to populate the reason for this row
    * @return 
    */     
    public add(String txt, Date rowdate, def amt)
    {
    	try
    	{
    		String stmt = """INSERT INTO ${dbtable} values(:id, :dt, :type, :amount, :number, :purpose, :flag)"""
			sql.execute(stmt, [dt:rowdate, type:'B', amount:amt, number:123, purpose: txt, flag:false])
			say "... added row to H2 database table ${dbtable} ok"
		}
		catch (Exception x)
		{
			say "${dbtable} table could not add row problem:"+x.message;
		}
    }  // end of method


   /** 
    * Method to remove a row of in-use H2 database table.
    * 
    * @param which holds int value of the ID of the row to remove
    * @return 
    */     
    public remove(int which)
    {
    	try
    	{
    		String stmt = """DELETE FROM ${dbtable} WHERE ID = ${which} """
    		say stmt;
			sql.execute(stmt)
			sql.execute("COMMIT");
			say "... removed row with ID of ${which} from H2 database table ${dbtable} ok"
		}
		catch (Exception x)
		{
			say "${dbtable} table could not remove row with ID of ${which} due to problem:"+x.message;
		}
    }  // end of method


   /** 
    * Method to remove H2 database table if it is found.
    * 
    * @return void
    */     
    public drop()
    {
    	try
    	{
    		String stmt = """DROP TABLE IF EXISTS ${dbtable}"""
			sql.execute(stmt)
			say "... drop H2 database table ${dbtable} ok"
		}
		catch (Exception x)
		{
			say "${dbtable} table not found to drop or caused problem:"+x.message
		}
    }  // end of method


   /** 
    * Method to see H2 database table rows.
    * 
    * @return void
    */     
    public select()
    {
    	try
    	{
    		String stmt = """SELECT * FROM ${dbtable}"""
    		say stmt;

			sql.eachRow(stmt)
			{row->
				println "row:"+row.toString()
			};
			
			say "... select rows from H2 database table ${dbtable} ok"
		}
		catch (Exception x)
		{
			say "${dbtable} table not found to select rows from - problem:"+x.message
		}
    }  // end of method


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=H2Support
user.home=${home}
dbtable=${dbtable}
java.io.File.separator=${java.io.File.separator}
address=${address}
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
        println "--- starting H2Support ---"

        H2Support obj = new H2Support();
        
        //println "H2Support = [${obj.toString()}]"
        //obj.drop();
                
        println "H2Support = [${obj.toString()}]"
        obj.create();

		println ""
		def date = new Date()
		def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
		println "... sdf="+sdf.format(date)
		println ""

		obj.add(" There is something ±§!@£%^&*()_+-= funny here.   ",date, -123.45);
        date+=7;
        obj.add("magic is here",date, 750.000);
		println ""

		obj.select();
		println ""

		obj.remove(1234);

		println ""
		obj.select();
		
        println "--- the end of H2Support ---"
    } // end of main

} // end of class