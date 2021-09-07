//@GrabConfig(systemClassLoader=true)
//@Grab(group='com.h2database', module='h2', version='1.3.176')
package com.jim.toolkit.database;

import groovy.transform.*;

import java.util.Date;
import groovy.sql.Sql
import java.util.Random  

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
 * H2Support class description
 *
 * This is code with all bits needed to ask H2 database to construct and keep data for a project
 *
 */ 
 @Slf4j
 @Canonical 
 public class H2
 {
	/** an O/S specific location for the user's home folder name */ 
	String home = System.getProperty("user.home");    

	/** an O/S specific location for the user's H2 database */ 
	String address = "jdbc:h2:file:${home}/Dropbox/Projects/Acctg/.h2data/acctg;AUTOCOMMIT=ON"; 

   /** 
    * Variable name of current class.
    */  
    String classname = "H2";

   /** 
    * Variable name of current H2 table connection values.
    */  
	def sql 

   /** 
    * Variable set to true if logging printouts are needed or false if not
    */  
    Boolean logFlag = false;


   /** 
    * Default Constructor 
    * 
    * @return H2 object
    */     
    public H2()
    {
        say "... running H2 constructor written by Jim Northrop from home="+home;
		sql = Sql.newInstance(address, "sa", "sa", "org.h2.Driver")
		say "... address="+address;
    } // end of constructor



   /** 
    * Non-Default Constructor 
    * 
    * @param ok is a boolean flag to load into logFlag
    * @return H2 object
    */     
    public H2(boolean ok)
    {
        logFlag = ok;
        say "... running H2 constructor written by Jim Northrop from home="+home;
        sql = Sql.newInstance(address, "sa", "sa", "org.h2.Driver")
        say "... address="+address;
    } // end of constructor


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return """classname=${classname}
user.home=${home}
java.io.File.separator=${java.io.File.separator}
address=${address}
sql=${sql.toString()}
logFlag=${logFlag}
"""
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



   // ======================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */     
    public static void main(String[] args)
    {
        println "--- Start H2 ---"
		H2 obj = new H2(true);
		println obj.toString();
        
        println "--- the end of H2 ---"
    } // end of main

} // end of class
//=============================
