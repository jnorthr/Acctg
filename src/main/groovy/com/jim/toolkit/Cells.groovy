package com.jim.toolkit;

import groovy.transform.*;
import com.jim.toolkit.tools.DateSupport;
import groovy.lang.Binding // not import groovy.lang.Binding
import com.jim.toolkit.database.H2TableSupport;

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
 * Cells class description
 *
 * This is a class to hold a list of Cell objects
 *
 */ 
 @Slf4j
 @Canonical 
 public class Cells
 {
    /**  array list of Cell objects */
    Cell[] list = [];

    /**  logic to translate text string into date object using either CCYY-MM-DD or  dd/mm/ccyy */
    DateSupport ds = new DateSupport();
    
    /** set true to give log output */
    boolean logFlag = false;
    
    /** subset array list of a single currency's Cell objects */
    def ccycells = [];

   // =========================================================================
   /** 
    * Class constructor.
    *
    * default is not to load prior Cells if any  */
    public Cells()
    {
        say "Cells constructor()"
    } // end of constructor


   /** 
    * Non-default constructor.
    *
    * default is not to turn on logFile flag
    */
    public Cells(boolean ok)
    {
        logFlag = ok;    
        say "Cells constructor(ok = $ok)"
    } // end of constructor


   /** 
    * Method to re-order Cell objects in cells[] array using Cell date.
    * 
    * @return sorted list of Cells
    */     
    public sort()
    {
        this.list = list.toSorted { a, b -> a.date <=> b.date }
        return list;
    } // end of sort


   /** 
    * Method to re-order Cell objects in cells[] array using Cells whose currency matches ISO code then date.
    * 
    * @param int is the value to match Cell ccy like 1 for EUR/978 or 2 for GBP/826  
    * @return sorted list of Cells
    */     
    public Cell[] sortCCY(int code)
    {
        this.ccycells = []
        list.each{entry-> if (entry.ccy==code) { ccycells.add(entry);}  }
        this.ccycells = ccycells.toSorted { a, b -> a.date <=> b.date }
        return this.ccycells
    } // end of sort


   /** 
    * Method to add new Cell object to array.
    * 
    * @param the Cell map to be saved
    * @return Cell just created after push onto our list[]
    */     
    public Cell add(Map m)
    {
        say "... Cells.add(Map m) @107"
        Cell nc = new Cell(m)

        list += nc;
        return nc;
    } // end of add


   /** 
    * Method to add new Cell object to array.
    * 
    * @param the Cell object to be saved
    * @return Cell just created including assigned ID  after push onto our list[]
    */     
    public Cell add(Cell ce)
    {
        say "... Cells.add(Cell ce) @123"
        list += ce;
        say "... list.size()=${list.size()};"
        return ce;
    } // end of add



   /** 
    * Method to add new Cell object to array using a binding.
    * 
    * @param binding of the Cell array to be saved
    * @return Cell just created including assigned ID after push onto our list[]
    */     
    public Cell add(groovy.lang.Binding b)
    {
        say "... Cells.add(Binding b) @139"
        Cell nc = new Cell()
        nc.setBinding(b);
        list += nc;
        return nc;
    } // end of add


   /** 
    * Most simple implementation to translate date string
    *
    * is this a date in iso format of yyyy-mm-dd ? 
    * 
    * @param the potential Date string to translate
    * @return Date from translation or today's date if it's not convertable
    */     
    public Date cvt(String s) 
    {
        say "... Cells.cvt(${s})"
        say "... DateSupport.isIsoDate(${s})="+ds.isIsoDate(s);

        if (ds.isIsoDate(s))
        {
            say "... DateSupport.getIsoDate()=|${ds.getIsoDate()}|"
            return ds.getIsoDate();
        }
        else
        {
            return new Date()
        }
    } // end of cvt

    
   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        def s = "";
        list.each{s+=it.toString();s+='\n';}
        return s;
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
    * Method to display how many entries in this cells list.
    * 
    * @return integer count of how many entries in this cells list
    */     
    public int size()
    {
        return list.size();
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
        println "--- starting Cells ---"

        Cells obj = new Cells(true)

        Date dat = obj.cvt('2017-09-21');
        obj.say "... Cells.cvt(2017-09-21)="+dat.toString();
        
        Cell ce = new Cell([id:1, date:dat, type:'C', amount:31.50, ccy:1, client:74, flag:true, reason:'Honda Jazz Insurance', name:'Mad Max'])
        obj.add(ce);
        obj.say  "Cells = |${obj.toString()}|"
        

        dat = obj.cvt('2017-02-02');
        int j = 3;
        5.times{
            obj.add([id:j, date:dat, type:'B', amount:121.44, ccy:2, client:2, flag:true, reason:'Pension', name:'Mad Max']);
            dat+=7;
            j+=1;
        }

        dat = obj.cvt('2016-12-22');
        ce = new Cell([id:8, date:dat, type:'C', amount:5.45, ccy:3, client:0, flag:false, reason:'PC Mag.', name:'Mad Max'])

        // add a Cell object to Cells list;
        obj.add(ce);

        // try Map add
        obj.say  "... try Map"
        dat+=28;
        Map ma = [id:9, date:dat, type:'C', amount:25.78, ccy:2, client:1, flag:false, reason:'Burgers R Us', name:'Mad Max']
        obj.add(ma);
        
        
        obj.say  "... try Binding"
        dat+=2;
        Binding binding = new Binding();
        binding.setVariable("id", 2);
        binding.setVariable("date", dat);
        binding.setVariable("type", "A");
        binding.setVariable("amount", -1.23);
        binding.setVariable("ccy", 3);
        binding.setVariable("client", 64);
        binding.setVariable("flag", true);
        binding.setVariable("reason", "reason for deed");
        binding.setVariable("name", "Mad Max");
        ce = new Cell(); 
        ce.setBinding(binding);
        obj.add(ce); // add a Cell
        obj.say  "... ce="+ce.toString();

        binding.setVariable("id", 12);
        obj.add(binding); // add a Binding
                
        obj.sort();        
        obj.say "Cells after sort() = \n${obj.toString()}"
        
        // Create a subset list of just entries for a single currency ordered by type  
        obj.sortCCY(1).each{cea-> obj.say "... entry from sortCCY(1)=|${cea}|"} 

        // now you can walk thru the new currency list
        //obj.ccycells.each{cea-> println "... entry from sortCCY(3)=|${cea}|"}

        obj.say "\n... Test the += logic for Cell"
        obj = new Cells()
        dat = obj.cvt('2017-2-2');
        ce = new Cell([id:11, date:dat, type:'C', amount:0.45, ccy:1, client:76, flag:false, reason:'Mars Bar', name:'Mad Max'])
        obj.add(ce);
        obj.say "... ce="+obj.list[0].toString();

        println "--- the end of Cell ---"
    } // end of main

} // end of class