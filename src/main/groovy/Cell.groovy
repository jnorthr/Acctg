//package io.jnorthr;
// http://mrhaki.blogspot.fr/2009/08/groovy-goodness-bound-and-constrained.html explains constraints
import groovy.transform.*;
import groovy.beans.*

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
 * Cell class description
 *
 * This is a sample Cell with all bits needed to do a gradle project
 *
 */ 
 @Canonical 
 @Bindable 
 public class Cell
 {
   /** 
    * a unique integer value of a cell within all Cells[] array.
    */  
    Integer id=0;


    /**  java date-type variable used to keep date of this transaction */
    Date d = new Date();


    /** an integer value to indicate which action to perform on this transaction 
        A = replace balance with this value
        B = increase balance by this amount
        C = reduce balance by this amount
    */ 
    Character type='A';


   /** 
    * BigDecimal variable describing the value of this transaction
    */  
    BigDecimal amount=0.00;
    
    
   /** 
    * a unique integer value of a client in the Client file.
    */  
    int number=0;


   /** 
    * Text variable describing the reason for this transaction.
    */  
    String purpose='unknown';


   /** 
    * Boolean variable describing a yes/no or true/false condition for this transaction.
    */  
    Boolean flag = false;


   /** 
    * Method to translate type variable into man-readable text.
    * 
    * @return formatted content of 'type' variable
    */     
    String cvtType()
    {
        def ty="unknown"
        switch(type)
        {
            case 'A': ty = 'Balance'
                     break;
            case 'B': ty = 'Income '
                     break;
            case 'C': ty = 'Expense'
                     break;
        } // end of switch
        return ty;
    } // end of cvtType
    
    
   /** 
    * Method to set internal variables from a groovy Binding var.
    * 
    * @return none
    */     
    public setBinding(Binding b)
    {
        this.id = 0;
        
        this.d       = (b.hasVariable("d")) ? b.getVariable("d") : new Date();
        this.type    = (b.hasVariable("type")) ? b.getVariable("type") : ' ';
        this.amount  = (b.hasVariable("amount")) ? b.getVariable("amount") : 0;
        this.number  = (b.hasVariable("number")) ? b.getVariable("number") : 0;
        this.purpose = (b.hasVariable("purpose")) ? b.getVariable("purpose") : " ";
        this.flag    = (b.hasVariable("flag")) ? b.getVariable("flag") : false;               
    } // end of method
    
    
   /** 
    * Method to format internal variables to write to persistent storage.
    * 
    * @return formatted content of internal variables
    */     
    public String toOutput()
    {
        return id + '; "' + d.format("yyyy-MM-dd") + '"; "' + type + '"; ' + amount + '; ' + number + '; "' + purpose + '"; "' + flag + '";';
    }    

   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    @Override
    public String toString()
    {
        return id + ' ' + d.format("yyyy-MM-dd") + ' ' + type + ' ' + amount + ' ' + number + ' ' + purpose + ' ' + flag;
    }  // end of string

   /** 
    * Method to display internal variablesas a Map.
    * 
    * @return Map formatted content of internal variables
    */     
    public Map toMap() {
      Map builder = [id:id,d:d.format("yyyy-MM-dd").toString(),type:type.toString(), amount:amount, number:number,
      purpose:purpose.toString(), flag:flag];
      return builder;
   } // end of method


   /** 
    * Method to print audit log.
    * 
    * @param the text to be said
    * @return void
    */     
    public void say(txt)
    {
        println txt;
    }  // end of method


    /* -
    * here's the rub: if you add ANY constructor to this class, it kills the
    * constructors from the  @Bindable annotation - so default to @Bindable 
    * constructors or write all yourself from scratch ;-{}
        public Cell(groovy.lang.Binding b, boolean flag)
        {
            println "... running Binding Cell constructor - "
            this.setBinding(b)
        }  // end of method
    */

   // ======================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */     
    public static void main(String[] args)
    {
        println "--- starting Cell ---"
        Date dat = Date.parse('yyy-MM-dd','2017-01-01');

        Cell obj = new Cell([d:dat, type:'A', number:123, purpose:'Start', amount:-123.45, id:2, flag:true])

        println "Cell.toString() = [${obj.toString()}]"
        println "Cell.toOutput() = [${obj.toOutput()}]"
        println "Cell.toMap()    = ${obj.toMap()}"
        
        println "\n... try Map constructor"
        dat += 4;
        Map m = [d:dat, type:'C', number:13, purpose:'Bingo', amount:-75.05, id:27, flag:true];
        obj = new Cell(m);
        println "Cell(map).toString() = [${obj.toString()}]"
         
        println "\n--------------------------------" 
        def cellmap = obj.toMap();
        cellmap.each{k,v-> println "... cellmap k=[${k}] v=[${v}]"}
        println "--------------------------------\n"
        
        println "\n... try Binding"
        dat+=24;
        Binding binding = new Binding();
        binding.setVariable("d", dat);
        binding.setVariable("type", "C");
        binding.setVariable("amount", -1.23);
        binding.setVariable("number", 64);
        binding.setVariable("purpose", "reason for deed");
        binding.setVariable("flag", true);

        println "... try using groovy Binding object with values"
        Cell obj2 = new Cell();
        obj2.setBinding(binding)
        println "Cell2.setBinding(binding) = [${obj2.toString()}]\n"
        
        println "... try TupleConstructor: http://mrhaki.blogspot.fr/2011/05/groovy-goodness-canonical-annotation-to.html"
        boolean yn = true;
        char ch= 'C'
        // invisible constructor allows passing a sequence of values with same var.type as each var. declared in this class
        // but seems like char and boolean must be explicit not just 'C' & false
        Cell obj3 = new Cell(17,dat,ch,12.34,77499,"Hi kids",yn);
        println "Cell3 = ${obj3.toString()}"

        Cell obj4 = new Cell(7,dat,ch,12.34); // try constructor without some trailing var.s
        println "Cell4 = ${obj4.toString()}"

        println "\nTry to build a Cell from a Binding - what kind? Bean or groovy.lang ?"
        try
        {
            Cell obj5 = new Cell(binding, true); // try constructor with Binding parm
            println "Cell5 = ${obj5.toString()}"
        } 
        catch(Exception x) 
        { 
            println "... Exception@228="+x.message; 
        }

        println "--- the end of Cell ---"
    } // end of main

} // end of class