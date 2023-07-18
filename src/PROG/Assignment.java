/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PROG;

import java.awt.*;
import javax.swing.plaf.FontUIResource;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/*
 *
 * @author dteh69
 */
public class Assignment {

    static int iNumEmployees, iNumDepartments, iNumEmpInDept = 0;
    static Employee[] employees = new Employee[10];
    static Department[] departments = new Department[10];
    static ArrayList<String> deptNames = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        int iChoice = 0, i = 0, iMaxIDLength = 0, iMaxNameLength = 0, iMaxSalaryLength = 0, iMaxDepartmentLength = 0, iEmptyDept = 0;
        String sTemp, sName, sID, sSalary, sDepartment;
        double dTotalSalary = 0, dAverageSalary = 0;
        boolean bChangesMade = false, bDeleteDeptLater = false;
        //begin read file and make employees
        do {
            //check if any changes made to the array that needs to be re-written in the file
            if (bChangesMade) {
                //write the new information into the file
                writeFile();
                bChangesMade = false;
                for (int x = 0; x < iNumDepartments; x++) {
                    departments[x] = new Department();
                    iNumEmpInDept = departments[x].getNumEmployees();
                    for (int j = 0; j < iNumEmpInDept; j++) {
                        employees[j] = new Employee();
                    }
                }
                iNumDepartments = 0;
                iNumEmployees = 0;
                iNumEmpInDept = 0;
            }
            readFile();
            //display the main menu
            iChoice = mainMenu();
            switch (iChoice) {
                //selection of choices 1-7
                case 1://display employees
                    sTemp = (String) JOptionPane.showInputDialog(null, "Choose the department", "NCS Employees", JOptionPane.PLAIN_MESSAGE, null, deptNames.toArray(), departments[0]);
                    for (int x = 0; x < iNumDepartments; x++) {
                        if (departments[x].getDeptName().equals(sTemp)) {
                            employees = departments[x].getDeptEmployees();
                            iNumEmpInDept = departments[x].getNumEmployees();
                        }
                    }
                    if (iNumEmpInDept == 0) {
                        errorMsg("There are no employees to display");
                    } else {
                        if (iNumEmpInDept > 1) {
                            for (int j = 0; j < iNumEmpInDept - 1; j++) {
                                // Checking the condition for two
                                // simultaneous elements of the array
                                if (employees[j].getID() > employees[j + 1].getID()) {
                                    // Swapping the elements.
                                    Employee temp = employees[j];
                                    employees[j] = employees[j + 1];
                                    employees[j + 1] = temp;
                                    j = -1;
                                }
                            }
                        }
                        // set and adjust the id, name, salary and department lengths
                        iMaxNameLength = getMaxLengths(1);
                        iMaxIDLength = getMaxLengths(2);
                        iMaxSalaryLength = getMaxLengths(3);
                        iMaxDepartmentLength = getMaxLengths(4);
                        sID = employees[0].adjustLength("ID", iMaxIDLength);
                        sName = employees[0].adjustLength("Name", iMaxNameLength);
                        sSalary = employees[0].adjustLength("Salary", iMaxSalaryLength);
                        sDepartment = employees[0].adjustLength("Department", 10);
                        // set the header for displaying
                        sTemp = "S/N   " + sID + "    " + sName + "    " + sSalary + "      " + sDepartment + "\n";
                        for (i = 0; i < iNumEmpInDept; i++) {
                            dTotalSalary += employees[i].getSalary();
                            sTemp = sTemp + (String.format("%02d", i + 1)) + "   " + employees[i].toString(iMaxIDLength,
                                    iMaxNameLength, iMaxSalaryLength, iMaxDepartmentLength) + "\n";
                        }
                        dAverageSalary = dTotalSalary / iNumEmpInDept;
                        sTemp = sTemp + "\n\n Total Salary: " + String.format("%.2f", dTotalSalary)
                                + "     Average Salary: " + String.format("%.2f", dAverageSalary);
                        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Monospaced", Font.BOLD, 12)));
                        JOptionPane.showMessageDialog(null, sTemp);
                        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 12)));
                    }
                    dTotalSalary = 0;
                    dAverageSalary = 0;
                    break;
                //search        
                case 2:
                    //find dept using input and go to correct array of employees
                    sTemp = (String) JOptionPane.showInputDialog(null, "Choose the department", "NCS Employees", JOptionPane.PLAIN_MESSAGE, null, deptNames.toArray(), departments[0]);
                    for (int x = 0; x < iNumDepartments; x++) {
                        if (departments[x].getDeptName().equals(sTemp)) {
                            employees = departments[x].getDeptEmployees();
                            iNumEmpInDept = departments[x].getNumEmployees();
                        }
                    }
                    if (iNumEmpInDept == 0) {
                        errorMsg("There are no employees to search for");
                    } else {
                        //starts partial search for the name of the employee 
                        i = validateRecordByID("Enter the ID or name to search.", null);//get a valid name 
                        if (i == -2) {
                            break;
                        } else if (i == -1) {
                            errorMsg("Employee cannot be found.");
                        } else {
                            //find the image corresponding to the employee's name
                            JOptionPane.showMessageDialog(null, "ID: " + employees[i].getID() + "\nName: " + employees[i].getName() + "\nSalary: " + employees[i].getSalary() + "\nDepartment: " + employees[i].getDepartment(),
                                    "NCS Employees",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    break;
                //delete
                case 3:
                    sTemp = (String) JOptionPane.showInputDialog(null, "Choose the department", "NCS Employees", JOptionPane.PLAIN_MESSAGE, null, deptNames.toArray(), departments[0]);
                    for (int x = 0; x < iNumDepartments; x++) {
                        if (departments[x].getDeptName().equals(sTemp)) {
                            employees = departments[x].getDeptEmployees();
                            iNumEmpInDept = departments[x].getNumEmployees();
                            if (iNumEmpInDept == 1) { //get ready to set every to null
                                bDeleteDeptLater = true;
                                iEmptyDept = x;
                            }
                        }
                    }
                    if (iNumEmpInDept == 0) {
                        errorMsg("There are no employees to delete");
                    } else {
                        //starts partial search for the name of the employee
                        i = validateRecordByID("Enter the ID or name to delete.", null);//get a valid name  
                        if (i == -2) {
                            break;
                        } else if (i == -1) {
                            errorMsg("Employee cannot be found.");
                        } else {
                            //confirm if the employee selected is correct
                            int response = JOptionPane.showConfirmDialog(null, "Do you want to delete " + employees[i].getName() + "?", "NCS Employees",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (response == JOptionPane.YES_OPTION) {
                                //replace current info with next employee's info
                                while (i < (iNumEmpInDept - 1)) {

                                    employees[i] = employees[i + 1];
                                    i++;
                                }
                                JOptionPane.showMessageDialog(null, "Employee has been deleted successfully.");
                                employees[i] = new Employee(0, null, 0, null); //set last elements to null
                                iNumEmpInDept = iNumEmpInDept - 1;
                                iNumEmployees = iNumEmployees - 1;
                                bChangesMade = true;
                                if (bDeleteDeptLater) {
                                    departments[iEmptyDept] = new Department();
                                    bDeleteDeptLater = false;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    break;
                //add
                case 4: //start the full name search to check if name is already inside the array

                    sName = validateName("Enter the name to check if this employee exists.", null, "add");//get valid name 
                    if (sName == null) {
                        break;
                    } else if (sName.equals("dupe"))// duplicate
                    {
                        errorMsg("The employee is already in the list.");
                    } else//not duplicate
                    {
                        //get the new employee object ready for assigning values
                        if (sName != null) {
                            sID = (validateID("Enter the employee ID.", null, "add"));//get a valid id
                            if (sID != null && !sID.equals("dupe")) {
                                sSalary = (validateSalary("Enter the salary.", null));//get a valid salary
                                if (sSalary != null) {
                                    sDepartment = (validateDepartment("Enter the department.", null));//get a valid department
                                    if (sDepartment != null) {
                                        //if new dept then make new dept and add employee
                                        for (int j = 0; j < iNumDepartments; j++) {
                                            if (departments[j].getDeptName().equalsIgnoreCase(sDepartment)) {
                                                //if existing dept, just add employee
                                                if (bChangesMade == false) {
                                                    if (departments[j].getNumEmployees() < 10) {
                                                        departments[j].addDeptEmployee(Integer.parseInt(sID), sName, Double.parseDouble(sSalary), sDepartment);
                                                        JOptionPane.showMessageDialog(null, sName + " has been added successfully.");
                                                        iNumEmployees = iNumEmployees + 1;
                                                    } else {
                                                        errorMsg("You cannot have more than 10 employees in 1 Department");
                                                    }
                                                    bChangesMade = true;
                                                }
                                            }

                                            if (bChangesMade == false) {
                                                if (!departments[j].getDeptName().equalsIgnoreCase(sDepartment)) { //if this a new department, create the dept array and throw employee inside
                                                    if (j == iNumDepartments - 1) {
                                                        departments[iNumDepartments] = new Department();
                                                        departments[iNumDepartments].setDeptName(sDepartment);
                                                        departments[iNumDepartments].addDeptEmployee(Integer.parseInt(sID), sName, Double.parseDouble(sSalary), sDepartment);
                                                        JOptionPane.showMessageDialog(null, sName + " has been added successfully. WOOHOO");
                                                        iNumDepartments = iNumDepartments + 1;
                                                        iNumEmployees = iNumEmployees + 1;
                                                        bChangesMade = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
//                //edit        
//                case 5:
//                    sTemp = (String) JOptionPane.showInputDialog(null, "Choose the department", "NCS Employees", JOptionPane.PLAIN_MESSAGE, null, deptNames.toArray(), departments[0]);
//                    for (int x = 0; x < iNumDepartments; x++) {
//                        if (departments[x].getDeptName().equals(sTemp)) {
//                            employees = departments[x].getDeptEmployees();
//                            iNumEmpInDept = departments[x].getNumEmployees();
//                            if (iNumEmpInDept == 1) { //get ready to set every to null
//                                bDeleteDeptLater = true;
//                                iEmptyDept = x;
//                            }
//                        }
//                    }
//                    if (iNumEmpInDept == 0) {
//                        errorMsg("There are no employees to edit");
//                    } else {
//                        //start partial search for the name of the employee
//                        i = validateRecordByID("Enter the ID or name to edit.", null);
//                        if (i == -2) {
//                            break;
//                        } else if (i == -1) {
//                            errorMsg("Employee cannot be found.");
//                        } else {
//                            sName = (validateName("Enter the new name.", employees[i].getName(), "edit"));//get a valid name
//                            if (sName != null) {
//                                sID = (validateID("Enter the id.", Integer.toString(employees[i].getID()), "edit"));//get a valid id
//                                if (sID != null) {
//                                    sSalary = (validateSalary("Enter the salary.", Double.toString(employees[i].getSalary())));//get a valid salary
//                                    if (sSalary != null) {
//                                        sDepartment = (validateDepartment("Enter the department.", employees[i].getDepartment()));//get a valid department
//                                        if (sDepartment != null) {
//                                            JOptionPane.showMessageDialog(null, employees[i].getName() + " has been edited successfully.");
//
//                                            //if new dept then make new dept and add employee
//                                            for (int j = 0; j < iNumDepartments; j++) {
//                                                if (bChangesMade == false) {
//                                                    if (departments[j].getDeptName().equalsIgnoreCase(sDepartment)) {
//                                                        //if existing dept, just add employee
//                                                        if (departments[j].getNumEmployees() < 10) {
//                                                            departments[j].addDeptEmployee(Integer.parseInt(sID), sName, Double.parseDouble(sSalary), sDepartment);
//                                                            JOptionPane.showMessageDialog(null, sName + " has been added successfully.");
//                                                            //need to remove old employee element
//                                                            employees[i] = new Employee();
//                                                            for (int x = 0; x < iNumDepartments; x++) {
//                                                                if (departments[x].getDeptName().equals(sTemp)) {
//                                                                    departments[x].setDeptEmployees(employees);
//                                                                }
//                                                            }
//                                                        } else {
//                                                            errorMsg("You cannot have more than 10 employees in 1 Department");
//                                                            bDeleteDeptLater = false;
//                                                            j = iNumDepartments;
//                                                        }
//                                                        bChangesMade = true;
//                                                    }
//                                                }
//                                                if (bChangesMade == false) {
//                                                    if (!departments[j].getDeptName().equalsIgnoreCase(sDepartment)) { //if this a new department, create the dept array and throw employee inside
//                                                        if (j == iNumDepartments - 1) {
//                                                            departments[iNumDepartments] = new Department();
//                                                            departments[iNumDepartments].setDeptName(sDepartment);
//                                                            departments[iNumDepartments].addDeptEmployee(Integer.parseInt(sID), sName, Double.parseDouble(sSalary), sDepartment);
//                                                            JOptionPane.showMessageDialog(null, sName + " has been added successfully. WOOHOO");
//                                                            //need to remove old employee element
//                                                            employees[i] = new Employee();
//                                                            for (int x = 0; x < iNumDepartments; x++) {
//                                                                if (departments[x].getDeptName().equals(sTemp)) {
//                                                                    departments[x].setDeptEmployees(employees);
//                                                                }
//                                                            }
//                                                            iNumDepartments = iNumDepartments + 1;
//                                                            bChangesMade = true;
//                                                        }
//                                                        j = iNumDepartments;
//                                                    }
//                                                }
//                                            }
//                                            if (bDeleteDeptLater) {
//                                                departments[iEmptyDept] = new Department();
//                                                bDeleteDeptLater = false;
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    break;
//                //exit      

                case 5:
                    JOptionPane.showMessageDialog(null, "Program Terminated.\nThank You!");
                    break;
                //table       
                case 6:
                    ArrayList<Employee> allEmployees = new ArrayList<Employee>();
                    for (int x = 0; x < iNumDepartments; x++) { // loop thru all the depts and copy all the employees into 1 array
                        employees = departments[x].getDeptEmployees();
                        iNumEmpInDept = departments[x].getNumEmployees();
                        for (int j = 0; j < iNumEmpInDept; j++) {
                            allEmployees.add(employees[j]);
                        }
                    }
                    if (iNumEmployees == 0) {
                        errorMsg("There are no employees to display");
                    } else {
                        String[] header = {"S/N", "ID", "NAME", "SALARY", "DEPARTMENT"};
                        DefaultTableModel data = new DefaultTableModel(header, 0);
                        for (i = 0; i < iNumEmployees; i++) {
                            data.addRow(new Object[]{1 + i, allEmployees.get(i).getID(), allEmployees.get(i).getName(),
                                allEmployees.get(i).getSalary(), allEmployees.get(i).getDepartment()});
                        }
                        JTable table = new JTable(data);
                        table.setAutoCreateRowSorter(true);
                        JOptionPane.showMessageDialog(null, new JScrollPane(table));
                        break;
                    }
            }

        } while (iChoice
                != 5);
    }//end of main

    public static void readFile() throws IOException {
        // read the AllArrays text file and assigns data to Employee objects/array
        File info = new File("AllArrays.txt");
        Scanner readFile = new Scanner(info);
        readFile.useDelimiter(">>");
        String sTemp, sName, sID, sSalary, sDepartment = "";
        deptNames.clear();
        // check if file is empty
        if (!readFile.hasNext()) {
            errorMsg("The file 'AllArrays.txt' is empty.");
            iNumEmployees = 0;
            iNumDepartments = 0;
        } else {
            sTemp = readFile.next();
            iNumDepartments = Integer.parseInt(sTemp);
            sTemp = readFile.next();
            iNumEmployees = Integer.parseInt(sTemp);
        }
        // read the next iNumDepartments and create departments with only name
        for (int x = 0; x < iNumDepartments; x++) {
            sDepartment = readFile.next().trim();
            departments[x] = new Department();
            departments[x].setDeptName(sDepartment);
            deptNames.add(departments[x].getDeptName());
        }
        for (int i = 0; i < iNumEmployees; i++) { //for every line in the employee part of the file
            if (readFile.hasNext()) {
                sID = readFile.next().trim();
                if (readFile.hasNext()) {
                    sName = readFile.next().trim();
                    if (readFile.hasNext()) {
                        sSalary = readFile.next().trim();
                        if (readFile.hasNext()) {
                            sDepartment = readFile.next().trim();
                            for (int x = 0; x < iNumDepartments; x++) { //keep looping every dept to see which dept to put employee in
                                if (departments[x].getDeptName().equalsIgnoreCase(sDepartment)) { // add employee if dept name corresponds
                                    departments[x].addDeptEmployee(Integer.parseInt(sID), sName, Double.parseDouble(sSalary), sDepartment);
                                }
                            }
                        } else {
                            errorMsg("The file doesnt have completed data.");
                        }
                    } else {
                        errorMsg("The file doesnt have completed data.");
                    }
                } else {
                    errorMsg("The file doesnt have completed data.");
                }
            } else {
                errorMsg("The file doesnt have completed data.");
            }
        }
    }

    public static void writeFile() throws IOException {
        //re-write the data in the array back into the file
        String sTemp = "";
        //if any dept employee array is empty just delete the dept
        for (int i = 0; i < iNumDepartments; i++) {
            if (departments[i].getNumEmployees() == 0) {
                iNumDepartments = iNumDepartments - 1;
                departments[i] = new Department();
            }
        }
        try (FileWriter fileWriter = new FileWriter("AllArrays.txt")) {
            fileWriter.write(iNumDepartments + ">>" + iNumEmployees + ">>" + System.lineSeparator());
            for (int i = 0; i < iNumDepartments; i++) {
                if (departments[i].getDeptName() != null) {
                    sTemp += departments[i].toFileString();
                }
            }
            fileWriter.write(sTemp + System.lineSeparator());
            for (int x = 0; x < iNumDepartments; x++) {
                employees = departments[x].getDeptEmployees();
                for (int i = 0; i < departments[x].getNumEmployees(); i++) {
                    if (employees[i].getName() != null) {
                        sTemp = employees[i].toFileString();
                        fileWriter.write(sTemp + System.lineSeparator());
                    }
                }
            }
            fileWriter.write(">EndOfFile<");
        }
    }

    public static int mainMenu() {
        //lists options 1 to 7 and gets input for selection
        //returns a string that will be used in other sections of main()
        String sChoice, sInput, sError = "";
        int iChoice;
        boolean bError = false;
        do {
            sChoice = JOptionPane.showInputDialog(null,
                    sError + "1-Display Employees\n2-Search Employees\n3-Delete Employee\n4-Add New Employee\n5-Exit\n6-Display table of employees\n\n", "NCS Employees",
                    JOptionPane.INFORMATION_MESSAGE);
            if (sChoice == null) {
                bError = true;
            } else {
                sInput = sChoice;
                if (sInput.length() != 1) {
                    bError = true;
                    sError = "The option must only be one character\n\n";
                } else if (sInput.compareTo("1") < 0 || sInput.compareTo("7") > 0) {
                    bError = true;
                    sError = "The option must be an integer from 1-7\n\n";
                } else {
                    bError = false;
                }
            }
        } while (bError);
        iChoice = Integer.parseInt(sChoice);
        return iChoice;
    }

    public static void errorMsg(String errType) {
        //method shows an error message with the error type
        JOptionPane.showMessageDialog(null,
                "Error: " + errType,
                "ERROR",
                JOptionPane.ERROR_MESSAGE);
    }

    public static String validateName(String sText, String sName, String sTask) {
        //method will check for any record using "full search", or no record is found
        //returns String sName if no record is found, and returns "dupe" if a record is found
        boolean bError = false;
        String sError = "";
        int i;
        do {
            sName = (String) JOptionPane.showInputDialog(null, sError + sText, "NCS Employees", JOptionPane.QUESTION_MESSAGE, null, null, sName);

            if (sName != null) {
                sName = sName.trim();
                if (sName.length() < 3) {
                    bError = true;
                    sError = "Name length cannot be less than 3\n\n";
                } else {
                    bError = false;
                }
                //check if the employee name is already inside the array
                if (sTask.equals("add")) {
                    for (i = 0; i < iNumEmpInDept; i++) {
                        if (employees[i].getName().equalsIgnoreCase(sName)) {
                            bError = true;
                            sError = "The employee name already exists\n\n";
                        }
                    }
                }
            }
        } while (bError);
        return sName;
    }

    public static int validateRecordByID(String sText, String sID) {
        //method will check for duplicated records using "partial search", or if a single record is found
        //returns an integer i which is used to find the employee object in the employee class
        int i, iTemp = 0, iNamesFound;
        boolean bError;
        String sError = "";
        do {
            bError = false;
            iNamesFound = 0;
            sID = (String) JOptionPane.showInputDialog(null, sError + sText, "NCS Employees", JOptionPane.QUESTION_MESSAGE, null, null, sID);
            if (sID != null) {
                if (Pattern.matches("[a-zA-Z]+", sID)) {
                    if (sID != null) {
                        if (sID.length() < 1) {
                            bError = true;
                            sError = "Name length cannot be less than 1\n\n";
                        } else {
                            //partial search for the names of the employee using contains() method
                            for (i = 0; i < iNumEmpInDept; i++) {
                                if (employees[i].getName().toUpperCase().contains(sID.toUpperCase())) {
                                    iTemp = i;
                                    iNamesFound = iNamesFound + 1;
                                    if (employees[i].getName().length() == sID.length()) {
                                        return i;
                                    }
                                }
                            }
                            if (iNamesFound > 1) {
                                bError = true;
                                sError = "Multiple records were found, try entering the full name\n\n";
                            } else {
                                bError = false;
                            }
                        }
                    }
                } else {
                    if (sID.length() < 1) {
                        bError = true;
                        sError = "ID length cannot be less than 1\n\n";
                    } else {
                        //partial search for the names of the employee using contains() method
                        for (i = 0; i < iNumEmpInDept; i++) {
                            if (Integer.toString(employees[i].getID()).contains(sID)) {
                                iTemp = i;
                                iNamesFound = iNamesFound + 1;
                                if (Integer.toString(employees[i].getID()).length() == sID.length()) {
                                    return i;
                                }
                            }
                        }
                        if (iNamesFound > 1) {
                            bError = true;
                            sError = "Multiple records were found, try entering the full ID\n\n";
                        } else {
                            bError = false;
                        }
                    }
                }
            }
        } while (bError);
        //if one employee found, then return the i value for the employee's place in the array
        if (iNamesFound == 1) {
            return iTemp;
        } //if user pressed cancel or the exit button
        else if (sID == null) {
            return -2;
        } //if no record is found then return -1
        else {
            return -1;
        }
    }

    public static String validateDepartment(String sText, String sDepartment) {
        //method will validate the department input
        //returns the department if it is valid; department length is more than 3
        boolean bError = false;
        String sError = "";
        do {
            sDepartment = (String) JOptionPane.showInputDialog(null, sError + sText, "NCS Employees", JOptionPane.QUESTION_MESSAGE, null, null, sDepartment);

            if (sDepartment != null) {
                sDepartment = sDepartment.trim();
                if (sDepartment.length() < 2) {
                    bError = true;
                    sError = "Department length cannot be less than 2\n\n";
                } else {
                    bError = false;
                }
            }

        } while (bError);
        return sDepartment;
    }

    public static String validateSalary(String sText, String sSalary) {
        // method will validate the salary input
        // returns the salary if it is valid; salary length is more than 3
        boolean bError = false;
        String sError = "";
        do {
            bError = false;
            sSalary = (String) JOptionPane.showInputDialog(null, sError + sText, "NCS Employees",
                    JOptionPane.QUESTION_MESSAGE, null, null, sSalary);

            if (sSalary != null) {
                if (Pattern.matches("-?\\d+(\\.\\d+)?", sSalary)) {
                    sSalary = sSalary.trim();
                    if (Double.parseDouble(sSalary) <= 100) {
                        bError = true;
                        sError = "We don't underpay our staff\n\n";
                    } else if (Double.parseDouble(sSalary) > 9999999) {
                        bError = true;
                        sError = "We don't have that much money to pay our staff";
                    } else {
                        bError = false;
                    }
                } else {
                    bError = true;
                    sError = "Salary cannot be empty\n\n";
                }
            } else {
                bError = true;
                sError = "Salary must be a number\n\n";
            }

        } while (bError);
        return sSalary;
    }

    public static String validateID(String sText, String sID, String sTask) {
        //method will check for any record using "full search", or no record is found
        //returns String sName if no record is found, and returns "dupe" if a record is found
        boolean bError = false;
        String sError = "";
        int i;
        do {
            sID = (String) JOptionPane.showInputDialog(null, sError + sText, "NCS Employees", JOptionPane.QUESTION_MESSAGE, null, null, sID);

            if (sID != null) {
                sID = sID.trim();
                if (sID.length() < 2) {
                    bError = true;
                    sError = "ID length cannot be less than 2\n\n";
                } else {
                    if (!Pattern.matches("[0-9]+", sID)) {
                        bError = true;
                        sError = "ID must be a number\n\n";
                    } else {
                        //check if the employee ID is already inside the array
                        if (sTask.equals("add")) {
                            bError = false;
                            for (i = 0; i < iNumEmpInDept; i++) {
                                if (employees[i].getID() == Integer.parseInt(sID)) {
                                    bError = true;
                                    sError = "The employee ID already exists\n\n";
                                }
                            }
                        }
                    }
                }

            }
        } while (bError);
        return sID;
    }

    public static int getMaxLengths(int iChoice) {
        //method will get the max lengths of name and salary
        //returns integer values after getting the maximum lengths
        int iLength = 0;
        for (int i = 0; i < iNumEmpInDept; i++) {
            switch (iChoice) {
                //get the max length
                case 1:
                    if (employees[i].getName() != null) {
                        if (employees[i].getName().length() > iLength) {
                            iLength = employees[i].getName().length();
                            if (iLength < 6) {
                                iLength = 6;
                            } else if (iLength > 20) {
                                iLength = 20;
                            }
                        }
                    }
                    break;
                case 2:
                    if (employees[i].getID() != -1) {
                        if (Integer.toString(employees[i].getID()).length() > iLength) {
                            iLength = Integer.toString(employees[i].getID()).length();
                            if (iLength < 6) {
                                iLength = 6;
                            } else if (iLength > 20) {
                                iLength = 20;
                            }
                        }
                    }
                    break;
                case 3:
                    if (employees[i].getSalary() != -1) {
                        if (Double.toString(employees[i].getSalary()).length() > iLength) {
                            iLength = Double.toString(employees[i].getSalary()).length();
                            if (iLength < 6) {
                                iLength = 6;
                            } else if (iLength > 20) {
                                iLength = 20;
                            }
                        }
                    }
                    break;
                case 4:
                    if (employees[i].getDepartment() != null) {
                        if (employees[i].getDepartment().length() > iLength) {
                            iLength = employees[i].getDepartment().length();
                            if (iLength < 6) {
                                iLength = 6;
                            } else if (iLength > 20) {
                                iLength = 20;
                            }
                        }
                    }
                    break;
            }
        }
        return iLength;
    }
}
