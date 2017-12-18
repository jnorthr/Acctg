package com.jim.toolkit;

// http://mrhaki.blogspot.fr/2009/08/groovy-goodness-bound-and-constrained.html explains constraints
import groovy.transform.*;
import groovy.beans.*
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
 * Cell class description
 *
 * This is a sample Cell with all bits needed to do a gradle project
 *
 * here's the rub: if you add ANY constructor to this class, it kills the
 * constructors from the  @Bindable annotation - so default to @Bindable 
 * constructors or write all yourself from scratch ;-{}
 *        public Cell(groovy.lang.Binding b, boolean flag)
 *       {
 *           println "... running Binding Cell constructor - "
 *           this.setBinding(b)
 *       }  // end of method
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
    Date date = new Date();


    /** a Char value to indicate which action to perform on this transaction 
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
    * Boolean variable describing a yes/no or true/false condition for this transaction.
    */  
    Boolean flag = false;


   /** 
    * Text variable describing the reason for this transaction.
    */  
    String reason='unknown';


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
    * @param b is a Binding holding key/values for our Cell object
    * @return none
    */     
    public setBinding(Binding b)
    {
        this.id    = (b.hasVariable("id")) ? b.getVariable("id") : 0;        
        this.date    = (b.hasVariable("date")) ? b.getVariable("date") : new Date();
        this.type    = (b.hasVariable("type")) ? b.getVariable("type") : ' ';
        this.amount  = (b.hasVariable("amount")) ? b.getVariable("amount") : 0;
        this.number  = (b.hasVariable("number")) ? b.getVariable("number") : 0;
        this.flag    = (b.hasVariable("flag")) ? b.getVariable("flag") : false;               
        this.reason  = (b.hasVariable("reason")) ? b.getVariable("reason") : " ";
    } // end of method
    
    
   /** 
    * Method to set internal Id variable for this cell if currently zero or it's Id is already
    * less than the maximum id number already in the core table;
    * 
    * @return int assigned row Id or if zero find the highest max Id in the 'core' H2 table.
    */     
    public int setId()
    {
        H2TableSupport ts = new H2TableSupport()
        int max = ts.max();

        if (this.id==0 || !(this.id > max) )
        {
            max+=1;
            this.id = max;
        } // end of if

        return max; 
    } // end of method
    
    
   /** 
    * Method to format internal variables to write to persistent storage.
    * 
    * @return formatted content of internal variables
    */     
    public String toOutput()
    {
        return id + '; "' + date.format("yyyy-MM-dd") + '"; "' + type + '"; ' + amount + '; ' + number + '; "' + flag + '"; "' + reason + '";';
    }    


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    @Override
    public String toString()
    {
        return id + ' ' + date.format("yyyy-MM-dd") + ' ' + type + ' ' + amount + ' ' + number + ' ' + flag + ' ' + reason;
    }  // end of string


   /** 
    * Method to display internal variablesas a Map.
    * 
    * @return Map formatted content of internal variables
    */     
    public Map toMap() 
    {
        Map builder = [id:id, date:date.format("yyyy-MM-dd").toString(), type:type.toString(), amount:amount, number:number, flag:flag, reason:reason.toString()];
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

        Cell obj = new Cell([date:dat, type:'A', number:123, amount:-123.45, id:0, flag:true, reason:'Start'])

        println "Cell.toString() = [${obj.toString()}]"
        println "Cell.toOutput() = [${obj.toOutput()}]"
        println "Cell.toMap()    = ${obj.toMap()}"
        
        println "\n--------------------------------" 
        def cellmap = obj.toMap();
        cellmap.each{k,v-> println "... cellmap k=[${k}] v=[${v}]"}
        println "--------------------------------\n"
        
        println "... try TupleConstructor: http://mrhaki.blogspot.fr/2011/05/groovy-goodness-canonical-annotation-to.html"
        boolean yn = true;
        char ch= 'C'
        println "--- the end of Cell ---"
    } // end of main

} // end of class
