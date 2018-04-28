package com.jim.toolkit.database;

import groovy.transform.*;
import groovy.sql.Sql
import java.util.Date;
import java.text.SimpleDateFormat
import com.jim.toolkit.Cell;
import com.jim.toolkit.Cells;
import com.jim.toolkit.database.LoaderSupport;

import groovy.sql.*

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
 * H2RowSupport class description
 *
 * This is code with all bits needed to ask H2 database to insert and drop data rows for a given table
 *
 */ 
 @Slf4j
 @Canonical 
 public class H2RowSupport
 { 
   /** 
    * Handle to currently open H2 database.
    */  
    H2TableSupport h2;

   /** 
    * Variable name of current class.
    */  
    String classname = "H2RowSupport";

   /** 
    * Variable name of extra logic to deal with comma-delimited stuff
    */  
    LoaderSupport ls = new LoaderSupport();

   /** 
    * Variable set to true if logging printouts are needed or false if not
    */  
    Boolean logFlag = false;


   /** 
    * Default Constructor 
    *
    * creates an SQL handle to default 'core' table
    * 
    * @return H2TableSupport object
    */     
    public H2RowSupport()
    {
        say "... running H2RowSupport constructor written by Jim Northrop";
        h2 = new H2TableSupport();
        h2.create();
    } // end of constructor


   /** 
    * Non-Default Constructor 
    *
    * creates an SQL handle to default 'core' table
    * 
    * @param ok holds boolean to populate the logFlag
    * @return H2TableSupport object
    */     
    public H2RowSupport(boolean ok)
    {
        logFlag = ok;
        say "... running H2RowSupport constructor written by Jim Northrop";
        h2 = new H2TableSupport();
        h2.create();
    } // end of constructor


   /** 
    * Method to add one row using a Map of key:values to in-use H2 database table.
    *
    * Keys for this Map are id, date, type, amount, ccy, client, flag, reason
    * missing or mis-cast values will be changed into correct types.
    * 
    * @param m holds Map of key / values used to populate the reason for this row
    * @return yn boolean result of true if Map created a row but false if SQL failure
    */     
    public boolean add(Map m)
    {
        say "... Map m ="

        if ( m.id == null ) { m.id = 0; }
        Integer mid = (m.id instanceof Integer ) ? m.id : m.id as Integer;
        mid = setId(mid)

        if ( m.date == null ) { m.date = new Date(); }
        if (m.date instanceof String == true) { println "... add m.date is String "} else{ println "... add m.date is NOT String " }
        Date md = (m.date instanceof String ) ? Date.parse('yyyy-MM-dd',m.date)  : m.date ;

println "... add() Date m.date=|${m.date}|"

        Character mt = m.type ?: "x";
        
        if ( m.amount == null ) { m.amount = 0.00; }
        BigDecimal mamt = (m.amount instanceof BigDecimal ) ? m.amount : m.amount as BigDecimal;

        if ( m.ccy == null ) { m.ccy = 1; }
        Integer mccy = (m.ccy instanceof Integer ) ? m.ccy : m.ccy as Integer;

        // client number
        if ( m.client == null ) { m.client = 0; }
        Integer mclient = (m.client instanceof Integer ) ? m.client : m.client as Integer;

        if ( m.flag == null ) { m.flag = false; }
        Boolean mflg = (m.flag instanceof Boolean ) ? m.flag : false;

        if ( m.reason == null ) { m.reason = ""; }
        String mreason = (m.reason instanceof String ) ? m.reason : m.reason as String;

        if ( m.name == null ) { m.name = ""; }
        String mname = (m.name instanceof String ) ? m.name : m.name as String;

        boolean yn = add(mid, md, mt, mamt, mccy, mclient, mflg, mreason, mname);
        say "... add Map gave :"+yn;
    }  // end of method


   /** 
    * Method to add one row using a Cell of values to in-use H2 database table.
    *
    * @param m holds Cell of values used to populate the reason for this row
    * @return yn boolean result of true if Map created a row but false if SQL failure
    */     
    public boolean add(Cell m)
    {
        say "... Cell m ="+m.toString();

        if ( m.id == null ) { m.id = 0; }
        Integer mid = (m.id instanceof Integer ) ? m.id : m.id as Integer;
        mid = setId(mid)

        if ( m.date == null ) { m.date = new Date(); }
        Date md = (m.date instanceof String ) ? Date.parse('yyy-MM-dd',m.date)  : m.date ;

        Character mt = m.type ?: "U";
        
        if ( m.amount == null ) { m.amount = 0.00; }
        BigDecimal mamt = (m.amount instanceof BigDecimal ) ? m.amount : m.amount as BigDecimal;

        if ( m.ccy == null ) { m.ccy = 1; }
        //  String mccy = (m.ccy instanceof Integer ) ? m.ccy : m.ccy as Integer;

        if ( m.client == null ) { m.client = 0; }
        Integer mclient = (m.client instanceof Integer ) ? m.client : m.client as Integer;

        if ( m.flag == null ) { m.flag = false; }
        Boolean mflg = (m.flag instanceof Boolean ) ? m.flag : false;

        if ( m.reason == null ) { m.reason = ""; }
        String mtxt = (m.reason instanceof String ) ? m.reason : m.reason as String;

        if ( m.name == null ) { m.name = ""; }
        String mname = (m.name instanceof String ) ? m.name : m.name as String;

        boolean yn = add(mid, md, mt, mamt, m.ccy, mclient, mflg, mtxt, mname);
        say "... add Cell gave :"+yn;

        return yn;
    }  // end of method


   /** 
    * Method to add one row to in-use H2 database table.
    * 1; "2013-03-02"; "B"; 121.44; 2; "Pension"; "true";
    * @param id   holds integer to uniquely identify this row - maybe overwritten by setId() method
    * @param rowdate holds Date() object to populate the action date for this row
    * @param ty   holds a single character to populate the business type for this row
    * @param amt  holds BigDecimal value to populate the money for this row
    * @param ccy holds integer value 1,2 or 3 for ISO currency code, where 1=978 for 'EUR' or 2=826 for 'GBP'
    * @param num  holds integer value of client or zero
    * @param flg  holds boolean yes/no for currency
    * @param txt  holds string used to populate the reason for this row
    * @return ok boolean result of true if Map created a row but false if SQL failure
    */     
    public boolean add(Integer id, Date rowdate, Character ty, BigDecimal amt, Integer ccy, Integer num, Boolean flg, String txt, String name)
    {
        boolean ok = true
        int mid = setId(id);

    	try
    	{
    		String stmt = """INSERT INTO ${h2.dbtable} values(:id, :date, :type, :amount, :ccy, :client, :flag, :reason, :name)"""
			h2.sql.execute(stmt, [id:mid, date:rowdate, type:ty, amount:amt, ccy:ccy, client:num, flag:flg, reason: txt, name:name])
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
    *   Method to add row to in-use H2 database table. Unique Row ID implied n supplied by H2 engine.
    *
    *	This method uses the 'ch' action code of 'B' by default
    *
    *   ... a Char value to indicate which action to perform on this transaction 
	*   A = replace balance with this value
    *   B = increase balance by this amount
    *   C = reduce balance by this amount
    * 
    * @param rowdate holds Date() object to populate the action date for this row
    * @param ch holds string used to populate the reason for this row
    * @param amt holds BigDecimal value to populate the money for this row
    * @param txt holds string used to populate the reason for this row
    * @return yn boolean result of true if Map created a row but false if SQL failure
    */     
    public boolean add(Date rowdate, Character ch, BigDecimal amt, String txt)
    {
    	try
    	{
            int mid = setId(id);
            String stmt = """INSERT INTO ${h2.dbtable} values(:id, :date, :type, :amount, :ccy, :client, :flag, :reason, :name)"""
            h2.sql.execute(stmt, [id:mid, date:rowdate, type:ch, amount:amt, ccy:1, client:0, flag:false , reason: txt, name:"" ])
            say "... added row to H2 database table ${h2.dbtable} ok"
            return true
        }
        catch (Exception x)
        {
            say "${h2.dbtable} table could not add row problem:"+x.message;
            return false;
        }
    }  // end of method


   /** 
    * Method to remove a row of in-use H2 database table.
    * 
    * @param which holds int value of the ID of the row to remove
    * @return yn boolean result of true if row was deleted but false if SQL failure
    */     
    public boolean remove(int which)
    {
    	try
    	{
    		String stmt = """DELETE FROM ${h2.dbtable} WHERE ID = ${which} """
    		say stmt;
			h2.sql.execute(stmt)
			h2.sql.execute("COMMIT");
			say "... removed row with ID of ${which} from H2 database table ${h2.dbtable} ok"
            return true
		}
		catch (Exception x)
		{
			say "${h2.dbtable} table could not remove row with ID of ${which} due to problem:"+x.message;
            return false
		}
    }  // end of method


   /** 
    * Method to update flag a row of in-use H2 database table.
    * 
    * @param which holds int value of the ID of the row to updated
    * @return yn boolean result of true if row was updated but false if SQL failure
    */     
    public update(Map m)
    {
        Integer mid = m.id;

        if ( m.date == null ) { m.date = new Date(); }
        if (m.date instanceof String == true) { println "... update m.date is String "} else{ println "... update m.date is NOT String " }
        Date md = (m.date instanceof String ) ? Date.parse('yyy-MM-dd',m.date)  : m.date ;

println "... update() Date m.date=|${m.date}|"

        Character mt = m.type ?: "x";
        
        if ( m.amount == null ) { m.amount = 0.00; }
        BigDecimal mamt = (m.amount instanceof BigDecimal ) ? m.amount : m.amount as BigDecimal;

        if ( m.ccy == null ) { m.ccy = 1; }
        Integer mccy = (m.ccy instanceof Integer ) ? m.ccy : m.ccy as Integer;

        // client number
        if ( m.client == null ) { m.client = 0; }
        Integer mclient = (m.client instanceof Integer ) ? m.client : m.client as Integer;

        if ( m.flag == null ) { m.flag = false; }
        Boolean mflg = (m.flag instanceof Boolean ) ? m.flag : false;

        if ( m.reason == null ) { m.reason = ""; }
        String mreason = (m.reason instanceof String ) ? m.reason : m.reason as String;

        if ( m.name == null ) { m.name = ""; }
        String mname = (m.name instanceof String ) ? m.name : m.name as String;

    	try
    	{
    		//String stmt = "UPDATE "+h2.dbtable+" SET id=12 WHERE AMOUNT = 150.05 "
            String stmt = """UPDATE core SET DATE="${md}", TYPE='${mt}', AMOUNT='${mamt}', CCY=${mccy}, FLAG=${mflg}, CLIENT=${mclient}, NAME='${mname}', REASON='${mreason}' WHERE ID = """+mid;
    		say stmt;
			h2.sql.execute(stmt)
			h2.sql.execute("COMMIT");
			say "... updated row with ID of ${mid} from H2 database table ${h2.dbtable} ok"
            return true
		}
		catch (Exception x)
		{
			say "core table could not update row with ID of ${mid} due to problem:"+x.message;
            return false
		}
    }  // end of method


   /** 
    * Method to determine next internal Id variable for this request if currently zero or it's Id is already
    * less than the maximum id number already in the core table;
    * 
    * @param myId holds int value of the ID of the proposed row to written
    * @return int provided rowId or if zero find the highest max Id in the 'core' H2 table +1 and suggest that.
    */     
    public int setId(myId)
    {
        // don't use H2 as it maybe pointing at a diff.table than 'core'
        H2TableMethods ts = new H2TableMethods()
        int max = ts.max();

        if (myId==0 || !(myId > max) )
        {
            max+=1;
        } // end of if
        else
        {
            max = myId
        } // end of else

        return max; 
    } // end of method


   /** 
    * Method to get row(s) for a specific internal Id variable 
    *
    * Metadata overview: http://groovy-lang.org/databases.html#_reading_rows
    *
    * @param myId holds int value of the ID of the proposed row to read
    * @return List of found row for this rowId or all rows with same Id in the 'core' H2 table.
    */     
    public Cells getId(myId)
    {
		Cells payload = new Cells();
		List columnNames = [];
		List columnTypes = []
		Cell s = new Cell();
        
        String stmt = "SELECT * FROM "+h2.dbtable+" WHERE ID = "+myId+"; "
		h2.sql.eachRow(stmt) { resultSet ->
				String temp = resultSet.toString()
    			say "... row :"+temp;

    			// ... row :[ID:2, DATE:2017-12-17, TYPE:B, AMOUNT:99.00, CCY:3, client:12, FLAG:false, REASON:Eve's pension]
    			s = ls.reMap(temp);
    			payload.add(s);
    			say "; Cell s="+s.toString();

				def md = resultSet.getMetaData()
				columnNames = (1..md.columnCount).collect{ md.getColumnName(it) }
				columnNames.each{e-> say "... column name:"+e; }
				columnTypes = (1..md.columnCount).collect{ md.getColumnTypeName(it) }
				columnTypes.each{e-> say "... column type:"+e; }
		} //end of each

		say "--> s="+s;
        return payload; 
    } // end of method


   /** 
    * Method to find out if we have core row(s) for a specific internal Id variable 
    *
    * @param myId holds int value of the ID of the proposed row to read
    * @return boolean true if rows exist for this rowId in the 'core' H2 table.
    */     
    public boolean hasId(myId)
    {
        boolean payload = false;        
        String stmt = "SELECT * FROM core WHERE ID = "+myId+"; "
        h2.sql.eachRow(stmt) { resultSet ->
            payload = true;
        } // end of each

        return payload; 
    } // end of method


   /** 
    * Method to find out if we have core row(s) for a specific internal Id variable 
    *
    * @param myId holds int value of the ID of the proposed row to read
    * @return content of first row if row exists for this rowId in the 'core' H2 table.
    */     
    public getIdRow(myId)
    {
        def payload = null;        
        String stmt = "SELECT * FROM core WHERE ID = "+myId+"; "

        def row = h2.sql.firstRow(stmt)
        payload = row;
/*
        h2.sql.eachRow(stmt) { resultSet ->
            payload = resultSet.get;
        } // end of each
*/
        return payload; 
    } // end of method

   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=${classname}
java.io.File.separator=${java.io.File.separator}
H2=${h2.toString()}
LoaderSupport=${ls.toString()}
logFlag=${logFlag}
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
        if (logFlag)
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
        println "--- starting H2RowSupport ---"

        H2RowSupport obj = new H2RowSupport(true);
        //println "... updating flag of id 1:"+obj.update(0);
        println " ";
        
        def rower = obj.getId(1);
        println "... Found this row for id one :"+rower;

        Map ma = ['id':10, 'date':'2018-12-25', 'amount':-123.45,'flag':false,'client':88,'name':'JIm Name',type:'A', 'reason':'Be reasonable', 'ccy':2]
        def ok = obj.update(ma);
        println "... update row for id 10 :"+ok;
        rower = obj.getId(10);
        println "... Found this row for id ten :"+rower;

        println "--- the end of H2RowSupport ---"
    } // end of main

} // end of class
