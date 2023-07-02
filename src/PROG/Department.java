/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PROG;

import javax.swing.JOptionPane;

/**
 *
 * @author dteh69
 */
public class Department extends Assignment {

    private String sDeptName;
    private Employee[] eEmployees = new Employee[10];

    Department(String sName, Employee[] employees) {
        this.sDeptName = sName;
        this.eEmployees = employees;
    }

    Department() {
        this.sDeptName = null;
        this.eEmployees = new Employee[10];
    }

    public void setDeptName(String sDeptName) {
        this.sDeptName = sDeptName;
    }

    public String getDeptName() {
        return sDeptName;
    }

    public void addDeptEmployee(int iID, String sName, double dSalary, String sDeptName) {
        int count = 0;
        if (this.getNumEmployees() == 10) {
            errorMsg("You cannot have more than 10 employees in 1 Department");
        } else {
            if (eEmployees[0] == null) {
                this.eEmployees[0] = new Employee(iID, sName, dSalary, sDeptName);
            } else {
                for (Employee emp : eEmployees) {
                    if (emp != null) {
                        count++;
                    }
                }
                this.eEmployees[count] = new Employee(iID, sName, dSalary, sDeptName);
            }
        }
    }

    public String toFileString() {
        // used for formatting the file when writing back into the file
        // returns a string that is used when writing to the file
        return (this.sDeptName + ">>");
    }

    public Employee[] getDeptEmployees() {
        return eEmployees;
    }

    // number of employees in 1 dept
    public int getNumEmployees() {
        int count = 0;
        for (Employee emp : eEmployees) {
            if (emp != null) {
                if(emp.getName() != null)
                    count++;
            }
        }
        return count;
    }
}
