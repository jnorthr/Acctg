package com.jim.toolkit;

import groovy.transform.*;

/** 
 * Parser class description
 *
 * This is a class to hold a look into JSON objects
 *
 */ 
// @Canonical 
public class Parser
{
   /** 
    * Variable name of current class.
    */  
    String classname = "Parser";

    def	brackets = 0;
    def list=[]; 
    String projectDir ="/Users/jimnorthrop/Dropbox/Projects/Acctg";
    boolean tf = false;
    def tfField = "";

    boolean flag = false;
    String flagfield="";

    boolean flag2 = false;
    String flag2field="";

    String jsonText = """
    ${projectDir} { 
        README.md()
        .gitignore
        .travis.yml
        src: {
            main{
                groovy{
                    Tools.groovy
                }
                resources{}
            }
            test {
                ToolsTest.groovy
            }
        }
        
        'images'{}
    }       
""";

	public void see()
	{
	    print "| ${brackets} { open |"
    	    print tfField;
            tfField="";
	} // end of see

	public load()
	{
		println "... Parse JSON Text samples for counts of pairs of {} braces used in json"
		println "----------------------------"
		println jsonText;
		println "----------------------------"
		see();

		jsonText.trim().each{e->
    		e.each{ea->
           	switch(ea)
        	{
            case '"' :  see();
                        if (flag) { flagfield+='"'; print flagfield; flagfield=""; flag=false; } else { flag=true; flagfield='"'; }
                        break;

            case "'" :  see();
                        if (flag2) { flag2field+="'"; print flag2field; flag2field=""; flag2=false; } else { flag2=true; flag2field="'"; }
                        break;

            case '{' :  brackets++;
                        print '{';
                        see();                        
                        break;
                        
            case '}' :  brackets--;
                        print '}';
                        see();                        
                        break;
            case ':' :  break;

/*
            case ':' :  see();
                        print ':';
                        break;
*/                        
                        
            case ' ' :  if (flag) {flagfield+=' ';}
                        else
                        if (flag2) {flag2field+=' ';}
                        break;
            
            //case '\n' : break;
            
            default:    if (flag) 
                        { 
                            flagfield+=ea; 
                        }
                        else
                        if (flag2) 
                        { 
                            flag2field+=ea; 
                        }
                        else
                        {
                            tfField += ea;
                            print ea;
                        }
                        break;               
        	} // end of switch
	    } // end of each
	  } // end of each

	  println "----------------------------"
    } // end of load


   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    public String toString()
    {
        return classname;
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
        println "--- starting Parser ---"
        Parser obj = new Parser();
        obj.load();
        println "--- the end of Parser ---"
    } // end of main

} // end of class