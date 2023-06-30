/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PROG;
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
		this.sDeptName = "";
		this.eEmployees = null;
	}

	public void setDeptName(String sDeptName) {
		this.sDeptName = sDeptName;
	}

	public String getDeptName() {
		return sDeptName;
	}

	public void setDeptEmployees(Employee[] employees) {
		this.eEmployees = employees;
	}

	public void addDeptEmployee(Employee employee) {
		this.eEmployees[eEmployees.length] = employee;
	}

	public Employee[] getDeptEmployees() {
		return eEmployees;
	}

	// number of employees in 1 dept
	// total sal of dept
	// avg sal of dept
}