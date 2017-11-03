import com.jim.toolkit.tools.DateSupport;
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
 public class Acctg
 {
    /**  establish new Binding object */
	Binding bind = new Binding();
        

	DateSupport obj = new DateSupport();
	Date dt = new Date();
	Cells cells = new Cells();

    
   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to initialize vars     */
    public Acctg()
    {
        println "\nAcctg constructor()"

		bind.setVariable("c", null);

		println "... Cells cells=new Cells();"
		println cells.toString();
		println ""

		if (obj.isIsoDate('2017-01-01'))
		{
    		dt = obj.getIsoDate();
		}

		Cell c = new Cell([d:dt, type:'A', number:0, purpose:'Start',amount:-123.45])
		println "... add cell:"
		try
		{
			cells+=c;
		} 
		catch(Exception x) 
		{ 
			println "... Exception@65="+x.message; 
		}

		def tx2 = c.toOutput();
		println "... tx2 ="+tx2;

		def tx2s = tx2.split(';')
		println "... tx2s.size()="+tx2s.size();
		println ""
		if (obj.isIsoDate('2017-5-24'))
		{
    		dt = obj.getIsoDate();
		}

		println ""
		println "... add cell:423.45"
		c = new Cell([d:dt, type:'C',number:0, purpose:'fuel',amount:423.45])
		println "... c="+c.toString();

		println ""
		println "... add cells+=c; ="+cells.toString();
		try
		{
			cells+=c;
		} 
		catch(Exception x) 
		{ 
			println "... Exception@93="+x.message; 
		}

		println ""
		if (obj.isIsoDate('2017-04-25'))
		{
    		dt = obj.getIsoDate();
		}
		
		println ""
		c = new Cell([d:dt, type:'B',number:0,purpose:'pension',amount:121.55])
		try
		{
			println "... Acctg.groovy cell@106="+c; 
			cells+=c;
		} 
		catch(Exception x) 
		{ 
			println "... Exception@111="+x.message; 
		}

		println ""
		dt +=7;
		c = new Cell([d:dt, type:'B',number:0,purpose:'pension',amount:121.55])
		cells.plus(c);

    } // end of constructor


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
        println "--- starting Acctg ---"
        
        Acctg obj = new Acctg()
        println "Acctg = \n${obj.toString()}"

        println "--- the end of Acctg ---"
    } // end of main

} // end of class



/*
def sortedByDate = cells.toSorted { a, b -> a.d <=> b.d }
def balance=0;
def file3 = new File('Acctg.txt')

// Or a writer object:
file3.withWriter('UTF-8') { writer ->
  sortedByDate.each{e->
        switch(e.type)
        {
            case 'C': balance -= e.amount;
                      break;
            case 'A': balance = e.amount;
                      break;
            case 'B': balance += e.amount;
                      break;
        } // end of switch

        println e.toString()+" = "+balance;
        //writer.write(e.toOutput()+'\n')
  } // end of each
} // end of file3

println "--- the end ---"
*/