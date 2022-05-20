package com.jim.toolkit.tools;

public class DecodeDate{

    def ds = "Sun Feb 04 13:07:29 IST 2018"
    def tokens = []
    def dt = ""
    def yr = ""
    def da = ""
    def dow = ""
    def mo = ""
    def downame = ["sun":1,"mon":2,"tue":3,"wed":4,"thu":5,"fri":6,"sat":7]
    def down = 0;

    def moname = ["jan":1,"feb":2,"mar":3,"apr":4,"may":5,"jun":6,"jul":7,"aug":8,"seo":9,"oct":10,"nov":11,"dec":12]
    def mn = 0;

    def ix = 0;
    def fulldate = ""
    def isodate = ""

    public DecodeDate()
    {
        decode(ds)
    } // end of constructor
    
    // 2018-02-03
    public String reformat(String tx)
    {
    	//println "... DecodeDate.reformat($tx)"
    	def ccyy = tx.substring(0,4)
    	def mm = tx.substring(5,7)
    	def da = tx.substring(8,10)
    	fulldate = "${da}/${mm}/${ccyy}"
    	//println "... DecodeDate.reformated as |${fulldate}|"

        return fulldate;
    } // end of method

    public String decode(String tx)
    {
        tokens = tx.trim().tokenize(" ")
        //println "... there are ${tokens.size()} tokens"
        ix = 0;
        tokens.each{e->
            ix += 1;
            //println "... token ${ix} is |${e}|"
            
            switch (ix)
            {
                case 1: dow = e.toLowerCase();
                        break;
                case 2: mo = e.toLowerCase();
                        break;
                case 3: da = e;
                        break;
                case 6: yr = e;
                        break;                
            } // end of switch    
        } // end of each

        down = downame[dow]
        //println "\n... down=|${down}| for $dow"
        mn = moname[mo]
        //println "... moname=|${mn}| for $mo"
        //println "... day of date for $da"
        //println "... year of date is $yr"        
        fulldate = "${da}/${mn}/${yr}"
        isodate = "${yr}-${mn}-${da}"
        
        return fulldate;
    } // end of method
    
    
    public static void main(String[] args)
    {
        DecodeDate dd = new DecodeDate();
        println "... date is |${dd.fulldate}|"
        String seed = "Mon Jan 14 13:07:29 CET 2018"
        def dat = dd.decode(seed);
        println "... date is |${dat}|  iso date=|${dd.isodate}| from seed:|${seed}|"

        println "--- the end ---"
    } // end of main
} // end of class