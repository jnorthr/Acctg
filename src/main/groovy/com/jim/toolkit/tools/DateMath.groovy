package com.jim.toolkit.tools;

import groovy.transform.*;

import java.util.Date;
import java.text.SimpleDateFormat
import static java.util.Calendar.*
 
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
 * DateMath class description
 *
 * This is code with all bits needed to calculate and format date strings as dd/mm/ccyy or yyyy-mm-dd mostly ISO date formatting
 *
 */ 
 @Slf4j
 @Canonical 
 public class DateMath
 {
   /** 
    * Variable name of current class.
    */  
    String classname = "DateMath";

   /** 
    * Variable date expression of a prior date. 
    */
    Date historyDate = new Date();

   /** 
    * Variable date expression of a future date. 
    */
    Date futureDate = new Date();

   /** 
    * Variable date expression of an ISO date. 
    */
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

   /** 
    * Calendar Variable used in decoding dates. 
    */
    Calendar cal = Calendar.getInstance();

   /** 
    * Variable holding todays date. 
    */
    Date todayDate = new Date();

   /** 
    * Variable set to true if logging printouts are needed or false if not
    */  
    Boolean logFlag = false;

   /** 
    * Default Constructor 
    * 
    * @return DateMath object
    */     
    public DateMath()
    {
        say "running DateMath constructor written by Jim Northrop"
        setup();
    } // end of constructor


   /** 
    * Non-Default Constructor 
    * 
    * @param ok - boolean to set logFlag to produce log file output
    * @return DateMath object
    */     
    public DateMath(boolean ok)
    {
        logFlag = ok;
        say "running DateMath constructor written by Jim Northrop"
        setup();
    } // end of constructor


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=${classname}
historyDate=${historyDate}
futureDate=${futureDate}
dateFormat=${dateFormat.toString()}
Calendar=${cal}
todayDate=${todayDate}
"""
    }  // end of string


   /** 
    * Method to verify dates.
    * 
    * @param the date text string  like "2017-12-13"
    * @return boolean true if date in txt string can be converted to a Date() obj
    */     
    public boolean check(txt)
    {
        cal = Calendar.getInstance();
        boolean ok = false;
        
        try {
            // parse in expected format ccyy-mm-dd
            cal.setTime(dateFormat.parse(txt));
            say "... check(${txt}) date can be converted into a Date() object using CCYY-MM-DD format"
            ok = true;
        } 
        catch (Exception ex) 
        {
            say "... check error:"+ex.message;
        }

        return ok;
    }  // end of method


   /** 
    * Method to setup two dates, 1)a future date seven days ahead of today and a history date seven days behind today.
    * 
    * @return Date() object with value of today.
    */     
    public Date setup()
    {
        todayDate = new Date();
        historyDate = todayDate - 7;
        futureDate = todayDate + 7;
        
        //  Date has before and after methods and can be compared to each other as follows:
        if(todayDate.after(historyDate) && todayDate.before(futureDate)) 
        {
            say "... todayDate=|${todayDate}|"
            say "... historyDate=|${historyDate}| was seven days ago"
            say "... futureDate=|${futureDate}| seven days ahead"
        } // end of if

        return todayDate;
    }  // end of method


   /** 
    * Method to translate date then add/subtract number of days.
    * 
    * @param the date text string  ccyy-mm-dd
    * @param noOfDays int interval - a plus/minus integer value of the number of days to change this date 
    *
    * @return Date - the consequence of taking this date string to add/subtract noOfDays
    */ 
    private Date getNextDate(String givenDate,int noOfDays) 
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date now = null;
        
        try {
            cal.setTime(dateFormat.parse(givenDate));
            cal.add(Calendar.DATE, noOfDays);
            now = cal.getTime();
            say "... getNextDate(String ${givenDate},int ${noOfDays}) has date now=|${now}|"
        } 
        catch (Exception ex) 
        {
            say "... getNextDate error:"+ex.message;
        }

        return now;
    } // end of getNextDate
    
    
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
        println "--- Starting DateMath ---"
        println "... To do math on a date like dd/mm/yyyy string"

        DateMath obj = new DateMath(true);
        println ""
        println "\n... doing check() logic="+obj.check("2017-12-13");
        println "\n... doing getNextDate(2017-12-31) minus 7 days so result =|${obj.getNextDate("2017-12-31",-7)}|";

        Date d = obj.getNextDate("2017-12-31",-7);
        println "\n... Date d =|${d}|";
        
        println "\nDateMath="+obj.toString();

        println "--- the end of DateSupport ---"
    } // end of main

} // end of class