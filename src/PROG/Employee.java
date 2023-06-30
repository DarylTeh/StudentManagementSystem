/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PROG;

/**
 *
 * @author dteh69
 */
class Employee extends Assignment {
	// declare student properties
	private int iID;
	private String sName;
	private double dSalary;
	private String sDepartment;

	// initialise student property in constructor
	Employee(int iID, String sName, double dSalary, String sDepartment) {
		this.iID = iID;
		this.sName = sName;
		this.dSalary = dSalary;
		this.sDepartment = sDepartment;
	}

	// function overloading for empty student object
	Employee() {
		this.iID = 0;
		this.sName = "";
		this.dSalary = 0;
		this.sDepartment = "";
	}

	public void setID(int iID) {
		// this method sets the mobile of an instance of the student object
		this.iID = iID;
	}

	public int getID() {
		// this method returns the mobile of an instance of the student object
		return iID;
	}

	public void setName(String name) {
		// this method sets the name of an instance of the student object
		this.sName = name.trim();
	}

	public String getName() {
		// this method returns the name of an instance of the student object
		return sName;
	}

	public void setSalary(double dSalary) {
		// this method sets the ID of an instance of the student object
		this.dSalary = dSalary;
	}

	public double getSalary() {
		// this method returns the ID of an instance of the student object
		return dSalary;
	}

	public void setDepartment(String sDepartment) {
		this.sDepartment = sDepartment;
	}

	public String getDepartment() {
		return sDepartment;
	}

	public String toString(int iIDLength, int iNameLength, int iSalaryLength, int iDepartmentLength) {
		// this method adjusts the length of ID, name, salary, department and assigns it
		// to a string
		return adjustLength(Integer.toString(iID), iIDLength) + "     " + adjustLength(sName, iNameLength) + "    $"
				+ adjustLength(Double.toString(dSalary), iSalaryLength) + "     "
				+ adjustLength(sDepartment, iDepartmentLength);
	}

	public String padSpace(String sString, int ilength) {
		// add spaces behind strings so all lengths are equal
		// returns the string with the whitespace-padded string
		while (sString.length() != ilength) {
			sString = sString + " ";
		}
		return sString;
	}

	public String toFileString() {
		// used for formatting the file when writing back into the file
		// returns a string that is used when writing to the file
		return Integer.toString(iID) + ">>" + sName + ">>" + Double.toString(dSalary) + ">>" + sDepartment + ">>";
	}

	public String adjustLength(String sString, int iLength) {
		// this method adjusts the length of the string that is passed in
		// returns a string that has the same length as the other strings
		if (sString.length() <= iLength)
			return sString = padSpace(sString, iLength);
		// if the string's length is more than iLength, it will truncate and return the
		// string
		else {
			return sString = sString.substring(0, iLength - 3) + "...";
		}
	}

	public void sortByID() {
		// sort by moving smaller ID up the array
		for (int i = 0; i < iNumEmployees; i++) {
			// Checking the condition for two elements of the array
			if (employees[i].getID() > employees[i + 1].getID()) {
				// Swapping the elements.
				Employee temp = employees[i];
				employees[i] = employees[i + 1];
				employees[i + 1] = temp;
				i = -1;// make loop go forever until its done
			}
		}
	}
}