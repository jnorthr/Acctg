package com.jim.toolkit;

import groovy.transform.*;
import com.jim.toolkit.tools.DateSupport;
import groovy.lang.Binding // not import groovy.lang.Binding
import com.jim.toolkit.database.H2TableSupport;

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
 @Canonical 
 public class Cells
 {
    /**  array list of Cell objects */
    Cell[] cells = [];

    /**  logic to translate text string into date object using either CCYY-MM-DD or  dd/mm/ccyy */
    DateSupport ds = new DateSupport();
    
   // =========================================================================
   /** 
    * Class constructor.
    *
    * default is not to load prior Cells if any  */
    public Cells()
    {
        println "\nCells constructor()"
        //def ct = load();
        //println "... loaded $ct rows"
    } // end of constructor


   /** 
    * Method to re-order Cell objects in cells[] array using Cell date.
    * 
    * @return sorted list of Cells
    */     
    public sort()
    {
        this.cells = cells.toSorted { a, b -> a.date <=> b.date }
        return cells;
    } // end of sort


   /** 
    * Method to re-order Cell objects in cells[] array using Cells whose flag matches tf then date.
    * 
    * @param the value to match Cell flag 
    * @return sorted list of Cells
    */     
    public sortFlag(boolean tf)
    {
        def activeCells = [];
        int ct = 0;
        cells.each{ea-> ct++; if ( ea.flag == tf ) { activeCells + ea; } }
        Cell[] ce = activeCells.toSorted { a, b -> a.date <=> b.date }
        println "... sortFlag(${tf}) input=${ct} output="+activeCells.size();
        return ce;
    } // end of sort


   /** 
    * Method to add new Cell object to array.
    * 
    * @param the Cell map to be saved
    * @return Cell just created after push onto our list[]
    */     
    public Cell add(Map m)
    {
        println "... Cells.add(Map m) @92"
        Cell nc = new Cell(m)

        cells += nc;
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
        println "... Cells.add(Cell ce) @111"
        cells += ce;
        println "... cells.size()=${cells.size()};"
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
        println "... Cells.add(Binding b) @140"
        Cell nc = new Cell()
        nc.setBinding(b);
        cells += nc;
        return nc;
    } // end of add


   /** 
    * Most simple implementation to translate date string
    * 
    * @param the potential Date string to translate
    * @return Date from translation or today's date if it's not convertable
    */     
    public Date cvt(String s) 
    {
        if (ds.isIsoDate(s))
        {
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
        cells.each{s+=it.toString();s+='\n';}
        return s;
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
        println "--- starting Cells ---"

        Cells obj = new Cells()

        Date dat = obj.cvt('2017-09-21');

        Cell ce = new Cell([id:0, date:dat, type:'C', amount:31.50, number:74, flag:true, reason:'Honda Jazz Insurance'])
        //ce.setId();

        obj.add(ce);
        println "Cells = \n${obj.toString()}"
        

        dat = obj.cvt('2017-02-02');
        5.times{
            obj.add([id:0, date:dat, type:'B', amount:121.44, number:2, flag:true, reason:'Pension']);
            dat+=7;
        }

        dat = obj.cvt('2016-12-22');
        ce = new Cell([id:0, date:dat, type:'C', amount:5.45, number:0, flag:false, reason:'PC Mag.'])
        //ce.setId();
        obj.add(ce);

        // try Map add
        println "... try Map"
        dat+=28;
        Map ma = [id:0, date:dat, type:'C', amount:25.78, number:1, flag:false, reason:'Burgers R Us']
        obj.add(ma);
        
        
        println "... try Binding"
        dat+=2;
        Binding binding = new Binding();
        binding.setVariable("date", dat);
        binding.setVariable("type", "A");
        binding.setVariable("amount", -1.23);
        binding.setVariable("number", 64);
        binding.setVariable("flag", true);
        binding.setVariable("reason", "reason for deed");

        ce = new Cell(); 
        ce.setBinding(binding);
        obj.add(ce); // add a Cell
        println "... ce="+ce.toString();

        obj.add(binding); // add a Binding
                
        obj.sort();        
        println "Cells after sort() = \n${obj.toString()}"

        println "... test the += logic for Cell"
        dat = obj.cvt('2017-2-2');
        ce = new Cell([id:0, date:dat, type:'C', amount:0.45, number:76, flag:false, reason:'Mars Bar'])
        obj.add(ce);

        println "--- the end of Cell ---"
    } // end of main

} // end of class