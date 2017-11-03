import groovy.transform.*;
import com.jim.toolkit.tools.DateSupport;
import groovy.lang.Binding // not import groovy.lang.Binding
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
    def cells = [];

    /**  holds last unique id of prior cell just created; add 1 before assigning to next cell */
    def nextID = 0;

    /**  logic to translate text string into date object using either CCYY-MM-DD or  dd/mm/ccyy */
    DateSupport ds = new DateSupport();
    
   // =========================================================================
   /** 
    * Class constructor.
    *
    * default is to load prior Cells if any     */
    public Cells()
    {
        println "\nCells constructor()"
        def ct = load();
        println "... loaded $ct rows"
    } // end of constructor


   /** 
    * Method to re-order Cell objects in cells[] array.
    * 
    * @return sorted list of Cells
    */     
    public void sort()
    {
        this.cells = cells.toSorted { a, b -> a.d <=> b.d }
    } // end of sort


   /** 
    * Method to add new Cell object to array.
    * 
    * @param the Cell array to be saved
    * @return Cell just created
    */     
    public Cell add(Map m)
    {
        nextID++;
        m['id']=nextID
        Cell nc = new Cell(m)

        cells.add(nc);
        return nc;
    } // end of add


   /** 
    * Method to add new Cell object to array.
    * 
    * @param the Cell array to be saved
    * @return Cell just created
    */     
    public Cell plus(Cell ce)
    {
        println "... Cells.plus(Cell ce)"
        nextID++;
        ce['id']=nextID
        cells.add(ce);
        return ce;
    } // end of add


   /** 
    * Method to add new Cell object to array from a Map.
    * 
    * @param the Cell array to be saved
    * @return Cell just created
    */     
    public Cell plus(Map m)
    {
        println "... Cells.plus(Map m)"
        nextID++;
        m['id']=nextID
        Cell nc = new Cell(m)

        cells.add(nc);
        return nc;
    } // end of add


   /** 
    * Method to add new Cell object to array using a binding.
    * 
    * @param binding of the Cell array to be saved
    * @return Cell just created
    */     
    public Cell plus(groovy.lang.Binding b)
    {
        println "... this.plus(Binding b)"
        nextID++;
        b['id']=nextID
        Cell nc = new Cell()
        nc.setBinding(b);
        cells.add(nc);
        return nc;
    } // end of add


    // Most simple implementation to translate date string.
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
    * Method to read all Cell objects from persistent store into our list.
    * 
    * @param the Cells array to be loaded
    * @return void
    */     
    public int load()
    {
        def file3 = new File('Acctg.txt')

        // Use a reader object:
        int count = 0
        def line
        def txs 
        file3.withReader { reader ->
            while (line = reader.readLine()) {
                if (line.trim().startsWith("//"))
                {
                    println line;
                }
                else
                {
                    print "\nCells.load() ="
                    println line;

                    // 1; "2013-03-02"; "B"; 121.44; 2; "Pension"; "true";
                    txs = line.split(';')
                    println "... txs.size()="+txs.size();
                    
                    def t1 = txs[0].trim(); // id

                    def t2 = txs[1].trim().substring(1); // date
                    int ct2 = t2.indexOf('"');
                    if (ct2>-1) { t2 = t2.substring(0,ct2); }
                    def dt = cvt(t2)
                    println "... and t2 date of '${t2}' gave date:"+dt.toString();
                    
                    // get type
                    def t3 = txs[2].trim().substring(1,2); // type
                    println "... t3 type=|${t3}|"

                    // get amount
                    println "... amt txs[3]=|${txs[3].trim()}|"  // amount
                    BigDecimal t4 = txs[3].trim() as BigDecimal;
                    println "... t4 amt=|${t4}|"
                    
                    // get number
                    println "... client txs[4]=|${txs[4].trim()}|"  // client number
                    BigDecimal t5 = txs[4].trim() as BigDecimal;
                    println "... client num=|${t5}|"

                    // get purpose
                    def t6 = txs[5].trim().substring(1);    // reason / purpose
                    def ct6 = t6.indexOf('"');
                    if (ct6>-1) { t6 = t6.substring(0,ct6); }
                    println "... purpose t6=|${t6}|"

                    
                    // get flag
                    def t7 = txs[6].trim().substring(1,2).toUpperCase();    // flag
                    boolean f = (t7=='T') ? true : false;                        
                    println "... flag=|${t7}| f=$f"

                    println "\n"

                    Cell c = new Cell([d:dt, type:t3, amount:t4, number:t5, purpose:t6, flag:f])
                    println c.toString();
                    count++;
                    cells += c;
                } // end of else
              } // end of while
              
              this.sort();
        } // end of reader
        
        println "... loaded $count Cell entries"
        return count;        
    } // end of load
    

   /** 
    * Method to write all Cell objects to persistent store.
    * 
    * @return void
    */     
    public void dump()
    {
        def file3 = new File('Acctg.txt')
        int count = 0

        this.sort();

        // Or a writer object:
        file3.withWriter('UTF-8') { writer ->
              this.cells.each{e->
                e.id = count+=1;
                writer.write(e.toOutput()+'\n')
              } // end of each
        } // end of file3
    } // end of dump

    
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

        Cell ce = new Cell([d:dat, type:'C', number:74, purpose:'Honda Jazz Insurance', amount:31.50, id:0, flag:true])
        obj.cells.add(ce);
        println "Cells = \n${obj.toString()}"
        

        dat = obj.cvt('2017-02-02');
        5.times{
            obj.add([d:dat, type:'B', number:2, purpose:'Pension', amount:121.44, flag:true]);
            dat+=7;
        }

        dat = obj.cvt('2016-12-22');
        ce = new Cell([d:dat, type:'C', number:0, purpose:'PC Mag.', amount:5.45, id:0, flag:false])
        obj.cells.add(ce);

        // try Map add
        println "... try Map"
        dat+=28;
        Map ma = [d:dat, type:'C', number:10, purpose:'Burgers R Us', amount:25.78, id:0, flag:false]
        ce = new Cell(ma)
        obj.cells+=ce;
        dat+=2;
        obj.plus(ma);
        
        
        println "... try Binding"
        dat+=2;
        Binding binding = new Binding();
        binding.setVariable("d", dat);
        binding.setVariable("type", "A");
        binding.setVariable("amount", -1.23);
        binding.setVariable("number", 64);
        binding.setVariable("purpose", "reason for deed");
        binding.setVariable("flag", true);

        ce = new Cell(); 
        ce.setBinding(binding);
        obj.cells+=ce;
        println "... ce="+ce.toString();
        obj.plus(binding);

        // try to 
        obj.cells+=ce;
        println "Cells = \n${obj.toString()}"
        obj.plus(ce);
                
        obj.sort();        
        println "Cells after sort() = \n${obj.toString()}"

        println "... test the += logic for Cell"
        dat = obj.cvt('2017-2-2');
        ce = new Cell([d:dat, type:'C', number:76, purpose:'Mars Bar', amount:0.45, id:80, flag:false])
        obj.cells+=ce;
        obj.cells.add(ce);

        obj.dump();   //obj.cells);

		// obj.plus(Map m)
		// obj.plus(Binding b)
		// obj.plus(Cell c);        
        println "--- the end of Cell ---"
    } // end of main

} // end of class