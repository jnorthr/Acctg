package com.jim.toolkit.tools;

import groovy.transform.*;
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
 * DateArithmetic class description
 *
 * This is code with all bits needed to validate and increment date strings, usually as dd/mm/ccyy or yyyy-mm-dd mostly ISO date formatting
 *
 */ 
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
        def tempDate = dt;
        tempDate[MONTH] += months; 
        return tempDate;
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
        def tempDate = dt;
        tempDate[DATE] += days; // DAY is base-zero relative
        return tempDate;
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
        def tempDate = dt;
        tempDate[DATE] += 1; // DAY is base-zero relative
        return tempDate;
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

        DateArithmetic obj = new DateArithmetic();
        
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

        println "... otherDate plus 1 MONTHs using obj.bumpMonth() method:"+obj.bumpMonth(otherDate,1);
        println "... otherDate plus 1 MONTHs using obj.bumpMonth() method:"+obj.bumpMonth(otherDate,1);
        println ""

        println "... otherDate plus 1 days using obj.bumpDays() method:"+obj.bumpDays(otherDate,1);
        
        // day of week ?
        //Calendar c = Calendar.getInstance();
        //c.setTime(otherDate);
        //int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        println "\n... dayOfWeek="+obj.getDayOfWeek(otherDate)        //dayOfWeek;
        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
        
        println "... otherDate plus 1 days using obj.bumpDays() method:"+obj.bumpDays(otherDate,1);        
        println "... dow=|${obj.getDayOfWeek(otherDate)}| dayOfWeek="+obj.getDayOfWeek(otherDate);
        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
        
        println "... otherDate plus 1 days using obj.bumpDays() method:"+obj.bumpDays(otherDate,1);        
        println "... dayOfWeek="+obj.getDayOfWeek(otherDate); 
        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
        
        
        println "... otherDate plus 1 days using obj.bumpDays() method:"+obj.bumpDays(otherDate,1);        
        println "... dayOfWeek="+obj.getDayOfWeek(otherDate); 
        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
        
        
        println "... otherDate plus 1 days using obj.bumpDays() method:"+obj.bumpDays(otherDate,1);        
        println "... dayOfWeek="+obj.getDayOfWeek(otherDate); 
        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
        
        
        println "... otherDate plus 0 days using obj.bumpDays() method:"+obj.bumpDays(otherDate,0);        
        println "... dayOfWeek="+obj.getDayOfWeek(otherDate);
        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
        println "" 

        3.times{
            println "\n... otherDate plus 1 days using obj.bumpDays() method:"+obj.bumpDay(otherDate);        
            println "... dayOfWeek="+obj.getDayOfWeek(otherDate); 
	        println "... dayOfWeekName="+obj.getDayOfWeekName(otherDate)
        } // end of times
        
        println "---- the end ---"
        

        println "--- the end of DateArithmetic ---"
    } // end of main

} // end of class        