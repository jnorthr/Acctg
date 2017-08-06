import groovy.transform.*;

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
    def c = [];

    /**  holds last unique id of prior cell just created; add 1 before assigning to next cell */
    def nextID = 0;


    
   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to load prior Cells if any     */
    public Cells()
    {
        println "\nCells constructor()"
        def ct = load(c);
        println "... loaded $ct rows"
    } // end of constructor


   /** 
    * Method to add new Cell object to array.
    * 
    * @param the Cell array to be saved
    * @return void
    */     
    public void add(Map m)
    {
        nextID++;
        m['id']=nextID
        Cell nc = new Cell(m)

        c.add(nc);
    } // end of add


	// Most simple implementation of toString.
	public Date cvt(String s) 
	{
    	Date dat = Date.parse('yyy-MM-dd',s);
	} // end of cvt


   /** 
    * Method to read all Cell objects from persistent store into our list.
    * 
    * @param the Cells array to be loaded
    * @return void
    */     
    public int load(list)
    {
        def file3 = new File('Acctg.txt')

        // Use a reader object:
        int count = 0
        def line
        def tx2s 
        file3.withReader { reader ->
            while (line = reader.readLine()) {
                print line;
                tx2s = line.split(';')
                print "   => tx2s.size()="+tx2s.size();
                def t5 = tx2s[0].substring(1);
                int ct5 = t5.indexOf('"');
                if (ct5>-1) { t5 = t5.substring(0,ct5-1); }
                def dt = cvt(t5)
                println " and date:"+dt.toString();
                Cell c = new Cell([d:dt, type:2, client:2, purpose:'loaded',amount:2.00])
                println c.toString();
                count++;
              } // end of while
        } // end of reader
        println "... loaded $count Cell entries"
        return count;        
    } // end of load
    

   /** 
    * Method to write all Cell objects to persistent store.
    * 
    * @param the Cell array to be saved
    * @return void
    */     
    public void dump(ca)
    {
        def file3 = new File('Acctg.txt')

        // Or a writer object:
        file3.withWriter('UTF-8') { writer ->
              ca.each{e->
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
    	c.each{s+=it.toString();s+='\n';}
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

        println "Cells = ${obj}"

        Date dat = Date.parse('yyy-MM-dd','2017-01-01');

        Cell ce = new Cell([d:dat, type:0, client:0, purpose:'Start',amount:-123.45,id:0])

        obj.c.add(ce);
        
        dat = Date.parse('yyy-MM-dd','2017-02-02');
        5.times{
            dat+=7;
            obj.add([d:dat, type:1, client:0, purpose:'pension',amount:121.44]);
        }
        println "Cells = ${obj.toString()}"

        obj.dump(obj.c);
        
        println "--- the end of Cell ---"
    } // end of main

} // end of class