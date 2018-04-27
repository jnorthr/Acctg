package com.jim.toolkit;

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
 * Employee class description
 *
 * This is a sample with all bits needed to do a gradle project
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
public class Employee
{ 
	   /** 
	    * a unique integer value of a cell within all Cells[] array.
	    */  
	    Integer id=0;


	   /** 
	    * Text variable describing some name for this transaction.
	    */  
	    String name='unknown';


	   /** 
	    * BigDecimal variable describing the value of this transaction
	    */  
	    BigDecimal hourlyRate=0.00;
    
	    
	   /** 
	    * Boolean variable describing a yes/no or true/false condition for this transaction.
	    */  
	    Boolean partTime = false;


        public Employee(int pid, String pname, BigDecimal phourlyRate, boolean ppartTime)
        {
	       // set internal properties
            this.id = pid;
            this.name = pname;
            this.hourlyRate = phourlyRate;
            this.partTime = ppartTime;
        } // end of constructor
 
    public int getId()
    {
        return id;
    }
 
 
    public String getName()
    {
        return name;
    }
 
    public void setName(String name)
    {
        this.name = name;
    }
 
    public BigDecimal getHourlyRate()
    {
        return hourlyRate;
    }
 
    public void setHourlyRate(BigDecimal hourlyRate)
    {
        this.hourlyRate = hourlyRate;
    }
 
    public boolean isPartTime()
    {
        return partTime;
    }
 
    public void setPartTime(boolean partTime)
    {
        this.partTime = partTime;
    }
 
    
   /** 
    * Method to set internal Id variable for this cell if currently zero or it's Id is already
    * less than the maximum id number already in the core table;
    * 
    * @return int assigned row Id or if zero find the highest max Id in the 'core' H2 table.
    */     
    public int setId(int id)
    {
        this.id = id;
        return id; 
    } // end of method
    
    
   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    @Override
    public String toString()
    {
        return id + ' ' + name  + ' ' + hourlyRate + ' ' + partTime;
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
        println "--- starting Employee ---"

        Employee obj = new Employee(123,'Start', 123.45, true )

        println "Employee.toString() = [${obj.toString()}]"
        println "--- the end of Cell ---"
    } // end of main

} // end of class
