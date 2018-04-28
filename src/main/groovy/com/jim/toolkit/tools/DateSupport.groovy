package com.jim.toolkit.tools;

import groovy.transform.*;
import groovy.sql.Sql
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
 * DateSupport class description
 *
 * This is code with all bits needed to validate and format date strings as dd/mm/ccyy or yyyy-mm-dd mostly ISO date formatting
 *
 */ 
 @Slf4j
 @Canonical 
 public class DateSupport
 {
    /** an O/S specific char. as a file path divider */
    String fs = java.io.File.separator;

    /** an O/S specific location for the user's home folder name */ 
    String home = System.getProperty("user.home");
    
    
   /** 
    * Variable datex of current today's date. Holds validated isDate text value - when valid
    *
    * in India, it's: |Sun Feb 04 13:07:29 IST 2018|
    */  
    String datex = new Date();


   /** 
    * Variable iso date expression of current today's date. Holds validated isIsoDate text value - when valid
    */  
    String isodate = new Date();


   /** 
    * Variable holding  dd/mm/ccyy date expression of just translated date. Holds validated isIsoDate text value - when valid
    */  
    String ddmmccyyDate = "";

   /** 
    * Variable name of current class.
    */  
    String classname = "DateSupport";


   /** 
    * Variable to identify type of separator in this date string as 1=. 2=/ 3=-
    */  
    int separatorType = 0;


   /** 
    * Variable holds maximum number of days in each month. Added dummy zero as first slot zero, so
    * month values can point directly to it's max limit
    */  
    int[] dim = [0, 31,29,31,30,31,30,31,31,30,31,30,31];


   /** 
    * Variable to hold individual date string tokens when divied by a type of separator in this date string as 1=. 2=/ 3=-
    */  
    def tokens = []


    /** a range of valid characters that can appear in a token */
    def valid = '0'..'9';

    /** set true to give log output */
    boolean logFlag = false;
    

   /** 
    * Default Constructor 
    * 
    * @return DateSupport object
    */     
    public DateSupport()
    {
        say "running DateSupport constructor written by Jim Northrop"
        DecodeDate dd = new DecodeDate();
        datex = dd.decode(datex);
        ddmmccyyDate = datex;
    } // end of constructor


   /** 
    * Non-Default Constructor 
    * 
    * @param  ok holds boolean used to set logging debug flag
    * @return DateSupport object
    */     
    public DateSupport(boolean ok)
    {
    	logFlag = ok;
        say "running DateSupport constructor written by Jim Northrop"

        DecodeDate dd = new DecodeDate();
        datex = dd.decode(datex);
        ddmmccyyDate = datex;
    } // end of constructor

    
   // ======================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */     
    public static void main(String[] args)
    {
        println "--- starting DateSupport ---"

        DateSupport obj = new DateSupport();
        if (obj.isIsoDate("2017-12-23"))
        {
            println "... getIsoDate(1921-12-25)="+obj.getIsoDate("2017-12-23");
        }

        if (obj.isIsoDate("1921-12-25"))
        {
            println "... getIsoDate(1921-12-25)="+obj.getIsoDate("1921-12-25");
        }
        //System.exit(0);

        try{
            obj.hasValidSeparator();             
        }
        catch(RuntimeException x) {
            println "... obj.hasValidSeparator() failed - missing parameter"
        }

        println "... obj.datex=|${obj.datex}|"

        // fails on ... getNextDate error:Unparseable date: "29/09/2017"
        def xx = obj.getNextDate("29/09/2017",7)

        boolean ok = obj.isIsoDate('2017-09-21')
        String ans = obj.getIsoDate('2017-09-21').toString();
        println "... ok=${ok} for 2017-09-21 ans = |${ans}| ddmmccyyDate=|${obj.ddmmccyyDate}|"
        assert ok == true;
        assert ans.startsWith("Thu Sep 21 ") == true

        println "... to convert dd/mm/yyyy strings into a Date() object"

        println "... today is "+obj.datex;
        if (obj.isDate(obj.datex))
        {
            println "... getDate(${obj.datex})="+obj.getDate(obj.datex);
        }
        else
        {
            if (obj.isIsoDate(obj.isodate))
            {
                println "... getIsoDate(${obj.isodate})="+obj.getIsoDate(obj.isodate);
            }
        } // end of else

        ans = obj.isDate("03/02/2018");
        println "... and 03/02/2018 answer was "+ans;
        if (ans)
        {
            println "... getDate(03/02/2018)="+obj.getDate("03/02/2018");
        }
        else
        {
            println "... date 03/02/2018 failed."
        }

        if (obj.isDate(" 7 /09-2017"))
        {
            println "... getDate( 7 /09-2017)="+obj.getDate(" 7 /09-2017");
        }

        if (obj.isDate("/09-2017"))
        {
            println "... getDate(/09-2017)="+obj.getDate("/09-2017");
        }

        if (obj.isDate("17/09-17"))
        {
            println "... getDate(17/09-17)="+obj.getDate("17/09-17");
        }

        if (obj.isDate(" 7 //"))
        {
            println "... getDate( 7 //)="+obj.getDate(" 7 //");
        }

        if (obj.isDate("31/09/2017"))
        {
            println "... getDate(31/09/2017)="+obj.getDate("31/09/2017")+"\n... as "+obj;
        }

        if (obj.isDate("32/09/2017"))
        {
            println "... getDate(32/09/2017)="+obj.getDate("32/09/2017");
        }
 
        if (obj.isDate("2018/32/109 "))
        {
            println "... getDate(2018/32/109 )="+obj.getDate("2018/32/109 ");
        }

        if (obj.isDate("31/09/17"))
        {
            println "... getDate(31/09/17)="+obj.getDate("31/09/17");
        }

        if (obj.isDate("9/31/1917"))
        {
            println "... getDate(9/31/1917)="+obj.getDate("9/31/1917");
        }

        // ========================== ========================
        println ""
        println "... try ISO dates -"

        println "... today is "+obj.isodate;
        if (obj.isIsoDate(obj.isodate))
        {
            println "... getIsoDate(${obj.isodate})="+obj.getIsoDate(obj.isodate);
        }

        if (obj.isIsoDate("30/09/2017"))
        {
            println "... getIsoDate(30/09/2017) ="+obj.getIsoDate("30/09/2017");
        }

        if (obj.isIsoDate("2017-09-30"))
        {
            println "... getIsoDate(2017-09-30)="+obj.getIsoDate("2017-09-30");
        }

        if (obj.isIsoDate("2017-19-30"))
        {
            println "... getIsoDate(2017-19-30)="+obj.getIsoDate("2017-19-30");
        }

        if (obj.isIsoDate("2017-09-39"))
        {
            println "... getIsoDate(2017-09-39)="+obj.getIsoDate("2017-09-39");
        }

        if (obj.isIsoDate("17-09-30"))
        {
            println "... getIsoDate(17-09-30)="+obj.getIsoDate("17-09-30");
        }

        if (obj.isIsoDate("1921-12-25"))
        {
            println "... getIsoDate(1921-12-25)="+obj.getIsoDate("1921-12-25");
        }

        if (obj.isIsoDate("1921-13-31"))
        {
            println "... getIsoDate(1921-13-31)="+obj.getIsoDate("1921-13-31");
        }

        if (obj.isIsoDate("1921- -31"))
        {
            println "... getIsoDate(1921- -31)="+obj.getIsoDate("1921- -31");
        }

        if (obj.isIsoDate("1921-0-31"))
        {
            println "... getIsoDate(1921-0-31)="+obj.getIsoDate("1921-0-31");
        }

        if (obj.isIsoDate("1 21-10-30"))
        {
            println "... getIsoDate(1 21-10-30)="+obj.getIsoDate("1 21-10-30");
        }

        ["1921-10-31","2017/12/31","25.12.2018","2017.12.25", "2 17-4-4", ",", "//","1/1/1", " "].each{e->
            println "... is [${e}] valid ? "+obj.hasValidSeparator(e)+" separatorType=${obj.separatorType} how many tokens ? ${obj.tokens.size()}"
        } // end of each

        ["Hi kids?=","23","0123456789","1 2345","678\n89"].each{v-> 
            println "... |${v}| has valid numbers - valid ? ="+obj.validate(v);
        } // end of each

        DateSupport ds2 = new DateSupport(true);

        println "... today is "+ds2.datex;
        if (ds2.isDate(ds2.datex))
        {
            println "... getDate(${ds2.datex})="+ds2.getDate(ds2.datex);
        }

        println "--- the end of DateSupport ---"
    } // end of main


   /** 
    * Method to test if each char. of a text string has valid allowable numeric character 0-9.
    * 
    * @param s holds string used in a token after splitting by a separator
    * @return boolean true when all characters are valid and allowable but false if a non 0..9 char. found
    */     
    private boolean validate(String s)
    {
        boolean tf = true;
        s.each{e-> 
            //say "|"+e+"|"; 
            if (e in valid) {} else {tf=false;}
        } // end of each
        return tf;
    } // end of method


   /** 
    * Method to test if text string has a valid known date separator
    * 
    * if valid date string then tokens[] holds 3 pieces or 5 pieces
    * and separatorType has a positive value of 1 or 2 or 3 if txt date string contains known date separator of . / - or blank
    *
    * @param txt holds string used to count the separators
    * @return boolean true when a known separator is in string and has two ocurences and tokens[] are set
    */     
    private boolean hasValidSeparator(String txt)
    {
        if (txt==null) { throw new RuntimeException("hasValidSeparator requires string argument - this one is missing."); }

        boolean flag = false;
        datex = txt;
        isodate = txt;
        separatorType = 0;
        tokens=[]

        String t2 = (txt.size() > 10) ? txt.substring(0,9).trim() : txt.trim();
        
        int t2spaces = t2.count(" ");
        int t2dot = t2.count(".");
        int t2slash = t2.count("/");
        int t2dash = t2.count("-");

        if (t2dot==2)
        {
            separatorType = 1;
            tokens = txt.trim().tokenize(".")
        } // end of if

        if (t2slash==2)
        {
            separatorType = 2;
            tokens = txt.trim().split('/')
        } // end of if

        if (t2dash==2)
        {
            separatorType = 3;
            tokens = txt.trim().split('-')
        } // end of if

        // |Sat Feb 03 11:47:05 IST 2018|  found in India
        if (t2spaces==5)
        {
            separatorType = 4;
            tokens = txt.trim().split(' ')
        } // end of if

        if (separatorType > 0 && separatorType < 4 && tokens.size() == 3)
        {
               flag = true;
        } // end of if
        else
        if (separatorType == 4 && tokens.size() == 5)
        {
               flag = true;
        } // end of if

        // test each char. of each token to confirm within 0-9 range
        if (flag && separatorType != 4)
        {
            tokens.each{v-> 
                if (!validate(v.trim())) { flag = false; }
            } // end of each
        } // end of if        
        
        return flag;
    }  // end of method



   /** 
    * Method to test if a text string holding a formatted date in iso format of dd/mm/yyyy into a date object
    * you must call hasValidSeparator() to fill tokens[]
    * 
    * logic uses slash ( / ) separator  
    *
    * @param txt holds iso date string used to translate into an actual date object
    * @return boolean true if txt string can become a valid Date object
    */     
    public boolean isDate(String txt)
    {
        boolean yn = hasValidSeparator(txt);
        datex = txt;

        // confirm string has proper construction of 3 tokens divided by know separators
        boolean flag1 = (yn && tokens.size() == 3) ? true : false;
        
        boolean flag2 = yn && flag1 && (tokens[0].size() == 2 || 1) ? true : false; // see if dd is 1 or 2 char.s
        boolean flag3 = yn && flag1 && (tokens[1].size() == 2 || 1) ? true : false; // see if mm is 1 or 2 char.s
        boolean flag4 = yn && flag1 && (tokens[2].size() == 4 || 2) ? true : false; // see if yy is 4 or 2 char.s

        // if good so far, try to convert token into a numeric equivalent
        if (yn && flag1 && flag2 && flag3 && flag4)
        {
            def intDay = tokens[0].trim().isInteger() ? tokens[0].trim().toInteger() : 0                
            flag2 = (intDay > 0 && intDay < 32) ? true : false; // good day? maybe

            def intMo = tokens[1].trim().isInteger() ? tokens[1].trim().toInteger() : 0                
            flag3 = (intMo > 0 && intMo < 13) ? true : false; // good month? maybe

            // if dd and mm valid, try to see if those values are dd-mm or mm-dd declared
            if (flag2 && flag3)
            {   
                //say "... isDate(${txt}) found intDay=|${intDay}| and intMo=|${intMo}| and dim[intMo]=|${dim[intMo]}|"
                if (intDay > dim[intMo] ) { flag2 = false; say "... flag2=false"; }
            } // end of if

            // do the year
            def intYr = tokens[2].trim().isInteger() ? tokens[2].trim().toInteger() : 0;
            
            // if no cc provided, just assume yy is century 2000
            if (intYr > 0 && intYr < 100) { intYr += 2000; }                 

            // don't let years thru is before 1900 or after 2100
            flag4 = (intYr > 1900 && intYr < 2100) ? true : false;

            // if all tests are true, date looks good
            if (flag1 && flag2 && flag3 && flag4)
            {                
                yn = true;
                datex = txt;
                //say "... can convert string |${txt}| into date - ok"
            } // end of if
            else
            {
                // something's wrong with the declared date string
                yn = false;
            } // end of else
        } // end of if

        if (!yn)
        {
            say "... |${txt}| could not be converted as format dd/mm/yyyy";
            say "... yn=$yn && flag1=$flag1 && flag2=$flag2 && flag3=$flag3 && flag4=$flag4"
        }

        return yn;
    }  // end of method



   /** 
    * Method to convert datex text string of a date in format of dd/mm/yyyy into a date object
    *
    * You must call hasValidSeparator() or isDate() method to fill tokens[]
    * should only do this method if method above returned true
    *
    * @return Date object equivalent of datex txt string 
    */     
    public Date getDate(String txt)
    {
        boolean yn = hasValidSeparator(txt);

        //def tokens = datex.trim().split('/')
        def otherDate = new Date();

        try
        {
            // check each piece to confirm it's a numeric value before converting
            def intDay = tokens[0].trim().isInteger() ? tokens[0].trim().toInteger() : 0                
            def intMo = tokens[1].trim().isInteger() ? tokens[1].trim().toInteger() : 0                
            def intYr = tokens[2].trim().isInteger() ? tokens[2].trim().toInteger() : 0  
            
            // don't let years thru is before 1900 or after 2100
            if (intYr > 0 && intYr < 100) { intYr += 2000; }                 
              
            ddmmccyyDate = "${intDay}/${intMo}/${intYr}"

            // over-write pieces of our date with provided values of ccyy or yy or mm or dd 
            otherDate[YEAR] = intYr
            otherDate[MONTH] = intMo -1
            otherDate[DATE] = intDay
            //say "... converted string |${datex}| into |${otherDate}| ok; intYr=|$intYr| intMo=|$intMo| intDay=|$intDay|"
        } // end of try

        catch (Exception x)
        {
            say "... |${datex}| could not be converted as format dd/mm/yyyy :"+x.message;
        }
        return otherDate;

    }  // end of method


// =================================
// ISO stuff follows

   /** 
    * Method to convert isodate text string of a date in format of yyyy-mm-dd into a date object
    * 
    * you must call hasValidSeparator() to fill tokens[]
    *
    * @return Date object equivalent of iso date txt string 
    */     
    public Date getIsoDate(String txt)
    {
        def otherDate = new Date();
        say "... DateSupport.getIsoDate(${txt}) today's Date is=|${otherDate.toString()}|"

        boolean yn = hasValidSeparator(txt);
        say "... DateSupport yn=${yn}  tokens= tokens[0]=|${tokens[0]}| tokens[1]=|${tokens[1]}| tokens[2]=|${tokens[2]}| "

        try
        {
            // check each piece to confirm it's a numeric value before converting
            def intDay = tokens[2].trim().isInteger() ? tokens[2].trim().toInteger() : 0                
            def intMo = tokens[1].trim().isInteger() ? tokens[1].trim().toInteger() : 0               
            def intYr = tokens[0].trim().isInteger() ? tokens[0].trim().toInteger() : 0  

            say "... DateSupport tokens= tokens[0]=|${tokens[0]}| tokens[1]=|${tokens[1]}| tokens[2]=|${tokens[2]}| "
            say "... DateSupport tokens= intDay=|${intDay}| intMo=|${intMo}| intYr=|${intYr}| "

            // don't let years thru is before 1900 or after 2100
            //if (intYr > 0 && intYr < 100) { intYr += 2000; }                 

            ddmmccyyDate = "${intDay}/${intMo}/${intYr}"

            say "... otherDate today's date is =|${otherDate.toString()}| ddmmccyyDate=|${ddmmccyyDate}|"              
            otherDate[YEAR] = intYr;
            otherDate[MONTH] = intMo - 1;
            otherDate[DATE] = intDay;
            say "... otherDate=|${otherDate.toString()}| after applying tokens"

            say "... converted iso string month |${tokens[1]}| into |${otherDate}| ok; intYr=|${intYr}| intMo=|${intMo}| intDay=|${intDay}|"
            say "    and ddmmccyyDate=|${ddmmccyyDate}|"
        }

        catch (Exception x)
        {
            say "... |${isodate}| could not be converted as format yyyy-mm-dd :"+x.message;
        }
        return otherDate;

    }  // end of method


   /** 
    * Method to test if text string holding a date in iso format of yyyy-mm-dd into a date object
    * 
    * logic uses dash ( - ) separator as defines in ISO 
    *
    * @param txt holds string used to translate into an actual iso date object
    * @return boolean true if txt string can become a valid Date object
    */     
    public boolean isIsoDate(String txt)
    {
        // this fills in the tokens[]
        boolean yn = hasValidSeparator(txt)
        say "... isIsoDate of |${txt}| yn=|$yn|"
        isodate = txt;

        boolean flag1 = (yn && tokens.size() == 3) ? true : false;
        boolean flag2 = flag1 && (tokens[0].size() == 2 || 4) ? true : false; // yyyy
        boolean flag3 = flag1 && (tokens[1].size() == 2 || 1) ? true : false; // mm
        boolean flag4 = flag1 && (tokens[2].size() == 1 || 2) ? true : false; // dd

        if (flag1 && flag2 && flag3 && flag4)
        {
            def intDay = tokens[2].trim().isInteger() ? tokens[2].trim().toInteger() : 0; // day                
            flag4 = (intDay > 0 && intDay < 32) ? true : false;

            def intMo = tokens[1].trim().isInteger() ? tokens[1].trim().toInteger() : 0; // month    
            flag3 = (intMo > 0 && intMo < 13) ? true : false;

            if (flag4 && flag3)
            {   
                say "... intDay=|$intDay| & intMo=|$intMo| "
                if (intDay > dim[intMo] ) { flag4 = false; }
            } // end of if

            def intYr = tokens[0].trim().isInteger() ? tokens[0].trim().toInteger() : 0; // year
            if (intYr >= 0 && intYr < 100) { intYr += 2000; }                 

            flag2 = (intYr > 1900 && intYr < 2100) ? true : false;

            if (flag1 && flag2 && flag3 && flag4)
            {                
                say "... intDay=|$intDay| & intMo=|$intMo|  intYr=|${intYr}| yn=true"
                yn = true;
                isodate = txt;
                //say "... can convert string |${txt}| into iso date - ok"
            } // end of if
            else
            {
                yn = false;
            }
        } // end of if
        else
        {
            yn = false;
        } // end of if

        if (!yn)
        {
            say "... |${txt}| could not be converted as iso format yyyy-mm-dd";
        }

        return yn;
    }  // end of method


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=DateSupport
fs=${fs}        
user.home=${home}
datex=${datex}
isodate=${isodate}
separatorType=${separatorType}
dim=${dim}
tokens=${tokens}
valid=${valid}
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
        if (logFlag) { log.info txt; }
    }  // end of method


   /** 
    * Method to test dates.
    * 
    * @param txt the date text string  
    * @return true boolean if string can be made into a Date or false if not
    */     
    public boolean check(txt)
    {
        say "... check(${txt})"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        boolean ok = false;
        Date todayDate = new Date();
        
        try {
            cal.setTime(dateFormat.parse(txt));
            say "... check Date todayDate=|${todayDate}|";
            ok = true;
        } 
        catch (Exception ex) 
        {
            say "... check error:"+ex.message;
        }
        
        return ok;
    }  // end of method


   /** 
    * Method to test dates.
    * 
    * @param the date text string  ccyy-mm-dd
    * @param days interval  a plus/minus integer value of the number of days to change this date 
    * @return Date if date string was changed to a date then added to or reduced by number of days but NULL if impossible
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
            say "... getNextDate Date now=|${now}|"
        } 
        catch (Exception ex) 
        {
            say "... getNextDate error:"+ex.message;
        }

        return now;
    } // end of getNextDate
    
} // end of class