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
 * DateArithmetic class description
 *
 * This is code with all bits needed to validate and increment date strings, usually as dd/mm/ccyy or yyyy-mm-dd mostly ISO date formatting
 *
 * Strange findings: Beware: Month is zero-relative so January is held in Date object as zero;
 * when adding or subtracting to dat[MONTH] MUST use += syntax
 *
 * Saturday is day 7 with Sunday as day 1 for day of week names.
 */ 
 @Slf4j
 @Canonical 
 public class DateArithmetic
 {
   /** 
    * Variable name of current class.
    */  
    String classname = "DateArithmetic";

   /** 
    * Variable names of days of week = 3 chars.
    */  
	Map down = [1:'Sunday',2:'Monday',3:'Tuesday',4:'Wednesday',5:'Thursday',6:'Friday',7:'Saturday']

   /** 
    * Variable set to true if logging printouts are needed or false if not
    */  
    Boolean logFlag = false;


   /** 
    * Default Constructor 
    * 
    * @return DateArithmetic object
    */     
    public DateArithmetic()
    {
        say "running DateArithmetic constructor written by Jim Northrop"
    } // end of constructor

   /** 
    * Non-Default Constructor 
    * 
    * @param ok - a boolean to set the logFlag to print output log file
    * @return DateArithmetic object
    */     
    public DateArithmetic(boolean ok)
    {
        logFlag = ok;
        say "running DateArithmetic constructor written by Jim Northrop"
    } // end of constructor

   /** 
    * Method to increment then return a Date() object by a number of months.
    * 
    * day is base-zero relative; increment date by one day, days beyond month-maximum causes month&year roll-over
    * month is base-zero relative; increment month beyond 12 causes year roll-over
    *
    * @param dt - the Date() object
    * @param months - a plus/minus integer number of months to increment/decrement the Date() object
    * @return Date object after applying the months
    */     
    public Date bumpMonth(Date dt, int months)
    {
        Date tempDate = dt;
        // strange results if you do NOT use += syntax to add
        tempDate[MONTH] += months; 
        return tempDate;
        //return dt[MONTH] + months; 
    } // end of bumpMonth()


   /** 
    * Method to increment then return a Date() object by a number of days.
    * 
    * day is base-zero relative; increment date by one day, days beyond month-maximum causes month&year roll-over
    *
    * @param dt - the Date() object
    * @param days - a plus/minus integer number of days to increment/decrement the Date() object
    * @return Date object after adding days
    */     
    public Date bumpDays(Date dt, int days)
    {
        return dt + days;
    } // end of bumpDays()


   /** 
    * Method to increment then return a Date() object by one day.
    * 
    * day is base-zero relative; increment date by one day, days beyond month-maximum causes month&year roll-over
    *
    * @param dt - the Date() object
    * @return Date object adter adding one day
    */     
    public Date bumpDay(Date dt)
    {
        return dt + 1;
    } // end of bumpDay()


   /** 
    * Method to retrieve day of week from Date() object.
    *
    * Saturday is day 7 with Sunday as day 1
    * 
    * @param dt - the Date() providing the answer
    * @return integer value of day of week from dt
    */ 
    public String getDayOfWeekName(Date dt)
    {
        // day of week ?
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return down[dayOfWeek];
    } // end of getDayOfWeek()

   /** 
    * Method to retrieve name of day of week from Date() object.
    *
    * Saturday is day 7 with Sunday as day 1
    * 
    * @param dt - the Date() providing the answer
    * @return String value of day of week name from dt where 1=Sun
    */ 
    public int getDayOfWeek(Date dt)
    {
        // day of week ?
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    } // end of getDayOfWeek()


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=DateArithmetic
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
        println "--- starting DateArithmetic ---"

        DateArithmetic obj = new DateArithmetic(true);
        
        //def tokens = datex.trim().split('/')
        def otherDate = new Date();
        int intYr = 2005; // can use or omit cc value of 19 or 20
        int intMo = 1
        int intDay = 27
        otherDate[YEAR] = intYr
        otherDate[MONTH] = intMo -1
        otherDate[DATE] = intDay

        println "... otherDate=|${otherDate}|"

        // if you add more months than 12, then year increments and month rolls-over
        otherDate[MONTH] += 11;
        println "... otherDate MONTH+=11 :|${otherDate}|\n"

        otherDate = obj.bumpMonth(otherDate,1);
        println "... otherDate plus 1 MONTHs using obj.bumpMonth() method:"+otherDate;
        println ""

        otherDate = obj.bumpDay(otherDate);
        println "... otherDate plus 1 days using obj.bumpDays() method:"+otherDate;
        
        println "\n... dayOfWeek="+obj.getDayOfWeek(otherDate)  
        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
        
        otherDate = obj.bumpDay(otherDate);
        println "... otherDate plus 1 days using obj.bumpDays() method:"+otherDate;        
        println "... dow=|${obj.getDayOfWeek(otherDate)}| dayOfWeek="+obj.getDayOfWeek(otherDate);
        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
        
        otherDate = obj.bumpDays(otherDate,1);
        println "... otherDate plus 1 days using obj.bumpDays() method:"+otherDate;        
        println "... dayOfWeek="+obj.getDayOfWeek(otherDate); 
        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
                
        otherDate = obj.bumpDays(otherDate,0);        
        println "... otherDate plus 0 days using obj.bumpDays() method:"+otherDate;        
        println "... dayOfWeek="+obj.getDayOfWeek(otherDate);
        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
        println "" 

        otherDate = new Date();

        3.times{
            println "\n... otherDate=|${otherDate}| plus 1 days using obj.bumpDay() method:"+otherDate;        
            println "... dayOfWeek="+obj.getDayOfWeek(otherDate); 
	        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
            otherDate = obj.bumpDay(otherDate)
        } // end of times

        Date date = Date.parse('yyy-MM-dd','2018-01-01');
        DateArithmetic da = new DateArithmetic(true);
        Date dat = da.bumpDays(date, 7);
        def yr = dat[Calendar.YEAR]
        def mo = dat[Calendar.MONTH]
        def day = dat[Calendar.DATE]
        println "... test 1b:  date=|${date}| dat[YEAR]=|${yr}|  dat[MONTH]=|${mo}|  dat[DATE]=|${day}| dat=|${dat}|";
        
        println "--- the end of DateArithmetic ---"
    } // end of main

} // end of class        
        // day of week ?
        //Calendar c = Calendar.getInstance();
        //c.setTime(otherDate);
        //int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
