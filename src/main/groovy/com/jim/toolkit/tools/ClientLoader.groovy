//@GrabConfig(systemClassLoader=true)
//@Grab(group='com.h2database', module='h2', version='1.3.176')
package com.jim.toolkit.tools;

import groovy.transform.*;

import java.util.Date;
import groovy.sql.Sql
import java.util.Random  

import com.jim.toolkit.database.H2TableSupport;
import com.jim.toolkit.tools.ClientSupport;

//import groovy.util.logging.Slf4j;
//import org.slf4j.Logger

// @Slf4j
 public class ClientLoader
 {
    /** an O/S specific location for the user's home folder name */ 
    String home = System.getProperty("user.home");    

    /** an O/S specific location for the user's H2 database */ 
    String address = "jdbc:h2:file:${home}/Dropbox/Projects/Acctg/.h2data/acctg;AUTOCOMMIT=ON"; 

   /** 
    * Variable name of current H2 sql connection values.
    */  
    def sql 

   /** 
    * Handle to set of logic for H2 DATABASE table manipulation.
    */  
    H2TableSupport h2

   /** 
    * Variable flag is true when H2 'clients' table connection is available.
    */  
    boolean connected = false;

   /** 
    * Variable name used as temporary handle.
    */  
    def row  

   /** 
    * Handle to external file used to store & load client tokenized records.
    */  
    def file3 = new File('clients.txt')


   /** 
    * Handle to set of logic for client manipulation.
    */  
    ClientSupport cs;


   /** 
    * Holds pieces of a string after spliting by blanks or bars
    */  
    String[] tokens = [];


   /** 
    * Default Constructor 
    * 
    * @return ClientLoader object
    */     
    public ClientLoader()
    {
        println "running ClientLoader constructor written by Jim Northrop"
        sql = Sql.newInstance(address, "sa", "sa", "org.h2.Driver")
        getH2();
    } // end of constructor


   /** 
    * Method to populate H2 'clients' table from external file of lines of tokens in a | delimited string.
    * 
    * @return integer count of the number of clients inserted into H2 table
    */     
    public Integer loadClients()
    {
        cs = new ClientSupport();
        cs.setUp('clients',cs.variables) 
        
        int count = 0;
        List cn = []
        file3.eachLine{e-> 

            if (!e.trim().startsWith("//"))
            {
                def i = splitter(e)
                i = 0;
                tokens.each{t-> if (i!=2) {cn+=t}; i++; }
                i = cs.insert(cn);
                //println "... "+e+" assigned ID of "+i;
                if (i>0) { count+= 1; }
                cn=[]
            } // end of if

        } // end of each

        return count;
    } // end of method


   /** 
    * Method to count tokens in a | delimited string and if more than one then tokenize using bars.
    * 
    * @param b is a String to tokenize for our Client object
    * @return integer count of the number of tokens found if dividable by |
    */     
    public int splitter(String b)
    {
        tokens = b.trim().tokenize("|")
        return tokens.size();
    } // end of method
    

   /** 
    * Method to copy all H2 'clients' rows into an external file of lines of tokens in a | delimited string.
    * 
    * @return integer count of the number of clients saved in an external clients.txt file
    */     
    public Integer saveClients()
    {
        file3.withWriter('UTF-8') { writer ->
            writer.write("// clients\n");
        }

        int ct = 0;
        row = sql.eachRow('SELECT * FROM clients', {e-> encode(e); ct += 1;  })
        return ct;
    } // end of method


   /** 
    * Method to format H2 'clients' rows to write to an external file of lines of tokens in a | delimited string.
    */     
    public encode(def e)
    {
        def num = (e.NUMBER==null) ? "0" : e.NUMBER;
        def tx = "${e.ID}|${e.CCY}|${num}|${e.FLAG}|${e.NAME}|${e.REASON}|"
        println tx;
        file3 << tx;
        file3 << "\n"
    } // end of method



   /** 
    * Method to attach to an H2 'clients' table.
    * 
    * @return boolean true if connection to clients in H2 'clients' table
    */     
    public boolean getH2()
    {
        if (!connected)
        {
            connected = true;    
            h2 = new H2TableSupport();
            h2.select('clients');
        } // end of if

        return true;
    } // end of method
    

   // ======================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */     
    public static void main(String[] args)
    {
        println "--- starting ClientLoader ---"
        ClientLoader obj = new ClientLoader()

        def ct = obj.saveClients();
        println "... "+ct+" clients found"

        println "--- the end ---"
    } // end of main

} // end of class
