//package io.jnorthr;

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
 * Cell class description
 *
 * This is a sample Cell with all bits needed to do a gradle project
 *
 */ 
 @Canonical 
 public class Cell
 {
    /**  java date-type variable used to keep date of this transaction */
    Date d = new Date();

    /** an integer value to indicate which action to perform on this transaction */ 
    int type=0;
    
   /** 
    * a unique integer value of a client in the Client file.
    */  
    int client=0;


   /** 
    * Text variable describing the reason for this transaction.
    */  
    String purpose='unknown';


   /** 
    * BigDecimal variable describing the value of this transaction
    */  
    BigDecimal amount=0.00;

   /** 
    * a unique integer value of a cell within all Cells[] array.
    */  
    int id=0;


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
            case 0: ty = 'Balance'
                     break;
            case 1: ty = 'Income '
                     break;
            case 2: ty = 'Expense'
                     break;
        } // end of switch
        return ty;
    } // end of cvtType
    
    
   /** 
    * Method to format internal variables to write to persistent storage.
    * 
    * @return formatted content of internal variables
    */     
    public String toOutput()
    {
        return '"' + d.format("yyyy-MM-dd") + '";' + type + ';' + client + ';' + '"' + purpose + '";' + amount + ';' + id + ';'
    }    


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    @Override
    public String toString()
    {
        return cvtType()+' '+d.format("yyyy-MM-dd")+' '+client+' '+purpose+' '+amount+' '+id;
    }  // end of string

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

        Cell obj = new Cell([d:dat, type:0, client:0, purpose:'Start',amount:-123.45,id:2])

        println "Cell = [${obj.toString()}]"
        println "Cell = [${obj.toOutput()}]"

        println "--- the end of Cell ---"
    } // end of main

} // end of class