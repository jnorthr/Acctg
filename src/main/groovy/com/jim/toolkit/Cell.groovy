package com.jim.toolkit;

// http://mrhaki.blogspot.fr/2009/08/groovy-goodness-bound-and-constrained.html explains constraints
import groovy.transform.*;
import groovy.beans.*
import com.jim.toolkit.database.H2TableMethods;
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
@Slf4j
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
    * a unique integer value of a currency for this transaction, so 1 is for ISO 978 = 'EUR'.
    */  
    int ccy=1;


   /** 
    * a unique integer value of a client in the Client file.
    */  
    int client=0;


   /** 
    * Boolean variable describing a yes/no or true/false condition for this transaction.
    */  
    Boolean flag = false;


   /** 
    * Text variable describing the reason for this transaction.
    */  
    String reason='unknown';


   /** 
    * Text variable describing the name of author of this transaction.
    */  
    String name='';


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
    * Method to translate our internal CCY currency variable of 1,2 or 3 into man-readable text.
    * 
    * @return formatted content of CCY variable
    */     
    String cvtCcy(int ourcode)
    {
        def ty="Unk"
        switch(ourcode)
        {
            case '1': ty = 'EUR'
                     break;
            case '2': ty = 'GBP'
                     break;
            case '3': ty = 'USD'
                     break;
        } // end of switch
        return ty;
    } // end of cvtCcy
    
    
   /** 
    * Method to set internal variables from a groovy Binding var.
    * 
    * @param b is a Binding holding key/values for our Cell object
    * @return none
    */     
    public setBinding(Binding b)
    {
        this.id      = (b.hasVariable("id"))     ? b.getVariable("id") : 0;        
        this.date    = (b.hasVariable("date"))   ? b.getVariable("date") : new Date();
        this.type    = (b.hasVariable("type"))   ? b.getVariable("type") : ' ';
        this.amount  = (b.hasVariable("amount")) ? b.getVariable("amount") : 0;
        this.ccy     = (b.hasVariable("ccy"))    ? b.getVariable("ccy") : 1;
        this.client  = (b.hasVariable("client")) ? b.getVariable("client") : 0;
        this.flag    = (b.hasVariable("flag"))   ? b.getVariable("flag") : false;               
        this.reason  = (b.hasVariable("reason")) ? b.getVariable("reason") : " ";
        this.name    = (b.hasVariable("name"))   ? b.getVariable("name") : " ";
    } // end of method
    
    
   /** 
    * Method to set internal Id variable for this cell if currently zero or it's Id is already
    * less than the maximum id number already in the core table;
    * 
    * @return int assigned row Id or if zero find the highest max Id in the 'core' H2 table.
    */     
    public int setId()
    {
    	// defaults to looking for highest Id in 'core' H2 table.
        H2TableMethods ts = new H2TableMethods()
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
        return id + '; "' + date.format("yyyy-MM-dd") + '"; "' + type + '"; ' + amount + '; ' + ccy + '; ' + client + '; "' + flag + '"; "' + reason + '" | "'+name+'"; ';
    }    


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    @Override
    public String toString()
    {
        return id + ' ' + date.format("yyyy-MM-dd") + ' ' + type + ' ' + amount + ' ' + ccy + ' ' + client + ' ' + flag + ' "' + reason + '"  "' + name+'"';
    }  // end of string


   /** 
    * Method to display internal variablesas a Map.
    * 
    * @return Map formatted content of internal variables
    */     
    public Map toMap() 
    {
        Map builder = [id:id, date:date.format("yyyy-MM-dd").toString(), type:type.toString(), amount:amount, ccy:ccy, client:client, flag:flag, reason:reason.toString(), name:name.toString()];
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
        log.info txt;
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

        Cell obj = new Cell([id:1199, date:dat, type:'A', amount:-123.45, ccy:1, client:123, flag:true, reason:'Start here.', name:'Fred Mertz'])

        println "Cell.toString() = [${obj.toString()}]"
        println "Cell.toOutput() = [${obj.toOutput()}]"
        println "Cell.toMap()    = ${obj.toMap()}"
        
        println "\n--------------------------------" 
        def cellmap = obj.toMap();
        cellmap.each{k,v-> println "... cellmap k=[${k}] v=[${v}]"}

        assert cellmap['id'] == 1199
        assert cellmap['date'] == "2017-01-01"
        assert cellmap['type'] == 'A'
        assert cellmap['amount'] == -123.45
        assert cellmap['ccy'] == 1
        assert cellmap['client'] == 123
        assert cellmap['flag'] == true
        assert cellmap['reason'] == "Start here."
        assert cellmap['name'] == "Fred Mertz"

        println "--------------------------------\n"
        assert obj.cvtCcy(1) == 'EUR'
        
        println "... try TupleConstructor: http://mrhaki.blogspot.fr/2011/05/groovy-goodness-canonical-annotation-to.html"
        boolean yn = true;
        char ch= 'C'
        println "--- the end of Cell ---"
    } // end of main

} // end of class
