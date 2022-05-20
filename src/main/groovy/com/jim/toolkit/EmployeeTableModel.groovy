package com.jim.toolkit;

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
 * EmployeeTableModel class description
 *
 * This is a sample with all bits needed to do a model for project
 *
 */
import java.util.List;
import javax.swing.table.AbstractTableModel;
  
public class EmployeeTableModel extends AbstractTableModel
{
    private final List<Employee> employeeList;     
    private final String[] columnNames = ["Id", "Name", "Hourly Rate", "Part Time"];
    private final Class[] columnClass = [ Integer.class, String.class, BigDecimal.class, Boolean.class];
 
    public EmployeeTableModel(List<Employee> employeeList)
    {
        this.employeeList = employeeList;
    }
     
    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }
 
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }
 
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }
 
    @Override
    public int getRowCount()
    {
        return employeeList.size();
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Employee row = employeeList.get(rowIndex);
        if(0 == columnIndex) {
            return row.getId();
        }
        else if(1 == columnIndex) {
            return row.getName();
        }
        else if(2 == columnIndex) {
            return row.getHourlyRate();
        }
        else if(3 == columnIndex) {
            return row.isPartTime();
        }
        return null;
    }
 
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }
 

   /** 
    * Method to set internal variable values for this Employee 
    * 
    * @return void
    */     
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Employee row = employeeList.get(rowIndex);
        if(0 == columnIndex) {
            row.setId((Integer) aValue);
        }
        else if(1 == columnIndex) {
            row.setName((String) aValue);
        }
        else if(2 == columnIndex) {
            row.setHourlyRate((BigDecimal) aValue);
        }
        else if(3 == columnIndex) {
            row.setPartTime((Boolean) aValue);
        }     
    } // end of method
    
    
   /** 
    * Method to display internal variables.
    * 
    * @return formatted content of internal variables
    */     
    @Override
    public String toString()
    {
        return employeeList + ' ' + columnNames  + ' ' + columnClass;
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

        EmployeeTableModel obj = new EmployeeTableModel()

        println "EmployeeTableModel.toString() = [${obj.toString()}]"

        println "--- the end of Cell ---"
    } // end of main

} // end of class
