package com.jim.toolkit.tools;

import groovy.transform.*;
import groovy.sql.Sql
import java.util.Date;
import java.text.SimpleDateFormat
//import static java.util.Calendar.*

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
 * H2RowSupport class description
 *
 * This is code with all bits needed to ask H2 database to construct and keep data rows for a project table
 *
 */ 
 @Canonical 
 public class H2RowSupport
 { 
   /** 
    * Handle to currently open H2 database.
    */  
    H2Support h2;

   /** 
    * Variable name of current class.
    */  
    String classname = "H2RowSupport";

   /** 
    * Default Constructor 
    *
    * creates an SQL handle to default 'core' table
    * 
    * @return H2Support object
    */     
    public H2RowSupport()
    {
        say "running H2RowSupport constructor written by Jim Northrop";
        h2 = new H2Support();
        h2.create();
    } // end of constructor



   /** 
    * Method to add one row using a Map of key:values to in-use H2 database table.
    *
    * missing or mis-cast values will be changed into correct types.
    * 
    * @param m holds Map of key / values used to populate the reason for this row
    * @return 
    */     
    public add(Map m)
    {
        print "... Map m ="

        if ( m.dt == null ) { m.dt = new Date(); }
        Date md = (m.dt instanceof String ) ? Date.parse('yyy-MM-dd',m.dt)  : m.dt ;

        Character mt = m.type ?: "x";
        
        if ( m.amt == null ) { m.amt = 0.00; }
        BigDecimal mamt = (m.amt instanceof BigDecimal ) ? m.amt : m.amt as BigDecimal;

        if ( m.number == null ) { m.number = 0; }
        Integer mnumber = (m.number instanceof Integer ) ? m.number : m.number as Integer;

        if ( m.txt == null ) { m.txt = ""; }
        String mtxt = (m.txt instanceof String ) ? m.txt : m.txt as String;

        if ( m.flag == null ) { m.flag = false; }
        Boolean mflg = (m.flag instanceof Boolean ) ? m.flag : false;

        boolean yn = add(md, mt, mamt, mnumber, mtxt, mflg);
        println "... add Map gave :"+yn;
    }  // end of method



   /** 
    * Method to add one row to in-use H2 database table.
    * 1; "2013-03-02"; "B"; 121.44; 2; "Pension"; "true";
    * @param txt holds string used to populate the reason for this row
    * @return 
    */     
    public boolean add(Date rowdate, Character ty, BigDecimal amt, Integer num, String txt, Boolean flg)
    {
        boolean ok = true
    	try
    	{
    		String stmt = """INSERT INTO ${h2.dbtable} values(:id, :dt, :type, :amount, :number, :purpose, :flag)"""
			h2.sql.execute(stmt, [dt:rowdate, type:ty, amount:amt, number:num, purpose: txt, flag:flg])
			say "... added row to H2 database table ${h2.dbtable} ok"
		}
		catch (Exception x)
		{
            ok = false;
			say "${h2.dbtable} table could not add row problem:"+x.message;
		}

        return ok;
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
    		String stmt = """DELETE FROM ${h2.dbtable} WHERE ID = ${which} """
    		say stmt;
			h2.sql.execute(stmt)
			h2.sql.execute("COMMIT");
			say "... removed row with ID of ${which} from H2 database table ${h2.dbtable} ok"
		}
		catch (Exception x)
		{
			say "${h2.dbtable} table could not remove row with ID of ${which} due to problem:"+x.message;
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
    		String stmt = """SELECT * FROM ${h2.dbtable}"""
    		say stmt;

			h2.sql.eachRow(stmt)
			{row->
				println "row:"+row.toString()
			};
			
			say "... select rows from H2 database table ${h2.dbtable} ok"
		}
		catch (Exception x)
		{
			say "${h2.dbtable} table not found to select rows from - problem:"+x.message
		}
    }  // end of method


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=${classname}
java.io.File.separator=${java.io.File.separator}
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
        println "--- starting H2RowSupport ---"

        H2RowSupport obj = new H2RowSupport();
        
/*        
		println ""
		def date = new Date()
		def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
		println "... sdf="+sdf.format(date)
		println ""

		obj.add(date, "B",-123.45, 789, " There is something funny here.   ", false);
        date+=7;
        obj.add(date, "C",750.05, 789, "Rent is due", true);
		println ""

		obj.select();
		println ""

		obj.remove(1234);
*/
        println "\n------------------------\n... do Map"
        Map ma = [id:12, dt:"2017-11-22", type:'B',amt:-12345.78,number:43,txt:'hi kids',flag:true]
        obj.add(ma);

        println "\n------------------------\n... get Cells"
        Cells c2 = new Cells();
        c2.cells.each{Cell c -> 
            println "... c="+c.toString()+"; ";

            Map xx = c.toMap();
            xx.each{k,v-> println "k=[${k}] and v=[${v}]"}
            obj.add( xx );
        } // end of each

		println "...\n"
		obj.select();
		
        println "--- the end of H2RowSupport ---"
    } // end of main

} // end of class