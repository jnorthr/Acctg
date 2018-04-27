package com.jim.toolkit.gui;

import javax.swing.*;
import groovy.transform.*;
import javax.swing.JOptionPane;
import javax.swing.JOptionPane.*

/**
* This Pane support utility offers simple wrappers for several of the JOptionPane
* simple usecases.
*
* @author  Jim Northrop
* @version 1.0
* @since   2017-08-31 
*/
 @Canonical 
 public class Pane
 {
   /** 
    * Variable name of current class.
    */  
    String classname = "Pane";

   /** 
    * Method to ask user about a choice of possibilities.
    * 
    * may want to change the list of possible values to reflect your own choies
    *
    * @param msg Value is text message to show user when asking for choice. 
    * @return selectedValue is null if dialog close button or default if no choice or users choice
    */     
   def getInput(def msg)
   {
        Object[] possibleValues = [ 'First', 'Second', 'Third' ];
        Object selectedValue = JOptionPane.showInputDialog(null, msg, "Input",
             JOptionPane.QUESTION_MESSAGE, null,
             possibleValues, possibleValues[0]);
        return selectedValue;
   } // end of getInput


   /** 
    * Method to ask user about a choice of possibilities.
    * 
    * @param msg Value is text message to show user when asking for choice. 
    * @param possibleValues List of choices to show user when asking for choice. 
    * @return selectedValue is null if dialog close button or default if no choice or users choice
    */     
   def getInput(def msg, Object[] possibleValues)
   {
        Object selectedValue = JOptionPane.showInputDialog(null,
             msg, "Pick One",
             JOptionPane.QUESTION_MESSAGE, null,
             possibleValues, possibleValues[0]);
        return selectedValue;
   } // end of getInput


   /** 
    * Method to advise user about a situation.
    * 
    * @param msg Value is text message to show user. 
    * @return choice - boolean true as YES is only implied choice
    */     
   def message(def msg)
   {
      return !(JOptionPane.showMessageDialog(null, msg, "Please Note", JOptionPane.INFORMATION_MESSAGE));
   } // end of message


   /** 
    * Method to seek user confirmation.
    * 
    * @param msg Value is text to show whan asking to confirm action. 
    * @return choice - boolean true if confirm choice was OK
    */     
   def confirm(def msg)
   {
      return !(JOptionPane.showConfirmDialog(null,
             msg, "Confirm Dialog", JOptionPane.OK_CANCEL_OPTION));
   } // end of confirm


   /** 
    * Method to ask about quitting.
    * 
    * @param msg Value is text to show whan asking to quit. 
    * @return choice - boolean true if YES
    */     
   def quit(def msg)
   {
       def choice = JOptionPane.showOptionDialog(null,
        msg,
        "Quit ?",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE,
        null, null, null);

        println (choice == JOptionPane.YES_OPTION)?"yes":"no";
            //println x;
        return !choice;
   } // end of quit
    

   // ======================================
   /** 
    * Method to run class tests.
    * 
    * @param args Value is string array - possibly empty - of command-line values. 
    * @return void
    */     
    public static void main(String[] args)
    {
        println "--- starting Pane ---"
        Pane jop = new Pane()
        Object[] possibleValues = [ 'Blue Derby', 'Runway Queen', 'Old McDonald' ];
        def ans = jop.getInput("What's ya pick of the ponies ?", possibleValues);
        println "ans = "+ans;
        println "--- the end of job ---"
        System.exit(0);
    } // end of main

} // end of class