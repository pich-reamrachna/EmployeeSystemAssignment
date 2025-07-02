import java.util.Scanner;

public class EmployeeSalaryManagementSystem {

    // The static here can be accessed by any methods. This is the same concepts as the global keyword for variable in Python
    // Arrays to store employee data
    static String[] names = new String[3];                      // Employee names
    static int[] ids = new int[10];                             // Employee IDs
    static double[] salaries = new double[3];                   // Employee base salaries
    static double[] bonuses = new double[3];                    // Bonus amounts in cash
    static double[] salariesAfterBonuses = new double[3];       // Salaries with bonuses
    static int count = 0;                                       // track number of employees currently in the system

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        menu();
    }

    /** Menu **/
    public static void menu() {
        int choice = 0;

        while(choice != 5){
            System.out.println();
            System.out.println("===============================================================");
            System.out.printf("%60s\n", "Welcome to the Main Menu. Select One of the Options Below");
            System.out.println("===============================================================\n");

            // Display menu options
            System.out.println("""
                1. Add Employee Details
                2. Add Bonus to Employee
                3. Display Employee Records
                4. Search For An Employee By ID
                5. Exit
                """);

            // Prompt for user choice
            System.out.print("Enter Option (1-5) >> ");
            String stringChoice = input.nextLine().trim();

            // Check for empty input
            if (stringChoice.isEmpty()) {
                System.out.println("\n* Input cannot be empty. Please enter a number between 1 and 5. *");
                continue;
            }

            try {
                choice = Integer.parseInt(stringChoice);

                // Handle user's menu choice
                switch (choice) {
                    case 1:
                        addEmployeeDetails();
                        break;
                    case 2:
                        calculateSalaryAfterBonus();
                        break;
                    case 3:
                        showEmployeeDetails();
                        break;
                    case 4:
                        searchEmployee();
                        break;
                    case 5:
                        System.out.println("\nExiting Program . . .\n");
                        System.exit(1);
                    default:
                        System.out.println("\n* Invalid Option. Please Choose Options Between 1 to 5. *");
                }

            } catch (NumberFormatException e) {
                System.out.println("\n* Invalid Input. Please enter a number between 1 and 5. *");
            }

        }

    }

    /** Adds a new employee record **/
    public static void addEmployeeDetails() {
        title("Add Employee Details");

        // Check if Employees array is Full
        if(count >= 3) {
            System.out.println("* No more space for more Employees. *");
            return;
        }

        // Input and validate employee name
        String name;
        while (true) {
            System.out.print("\nEnter the Employee's Name >> ");
            name = input.nextLine().trim();

            // Check if name given is valid
            if (isValidName(name)) {
                break;
            }
        }

        System.out.println("* Employee's name has been entered into the record. *");

        // Input and validate employee ID
        int id;
        while (true) {
            System.out.print("\nEnter the Employee's ID >> ");
            String stringId = input.nextLine().trim();

            // Validate ID
            if (isValidInt(stringId)) {
                id = Integer.parseInt(stringId);

                // Check for duplicate IDs
                boolean exists = false;
                for (int i = 0; i < count; i++) {
                    if (ids[i] == id) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    System.out.println("* This ID already exists. Please try again. *");
                    continue;
                }
                break;
            }
        }

        System.out.println("* Employee's ID has been entered into the record. *");

        // Input and validate employee salary
        double salary;
        while (true) {
            System.out.print("\nEnter the Employee's Salary >> ");
            String stringSalary = input.nextLine().trim();

            // Validate Salary
            if (isValidSalary(stringSalary)) {
                salary = Double.parseDouble(stringSalary);
                break;
            }
        }

        System.out.println("* Employee's salary has been entered into the record. *");


        // Find the correct position to insert based on ID
        int insertPos = count;
        for (int i = 0; i < count; i++) {
            if (id < ids[i]) {
                insertPos = i;
                break;
            }
        }

        // Shift records to make space for the new employee
        for (int i = count; i > insertPos; i--) {
            ids[i] = ids[i - 1];
            names[i] = names[i - 1];
            salaries[i] = salaries[i - 1];
            bonuses[i] = bonuses[i - 1];
            salariesAfterBonuses[i] = salariesAfterBonuses[i - 1];
        }

        // Insert new employee at the correct position
        ids[insertPos] = id;
        names[insertPos] = name;
        salaries[insertPos] = salary;
        bonuses[insertPos] = 0;
        salariesAfterBonuses[insertPos] = salary;

        count++;
    }

    /** Apply bonus to an employee based on performance scale **/
    public static void calculateSalaryAfterBonus() {
        title("Apply Bonus");

        if(count == 0){
            System.out.println("* No Employees in the system ... *");
            return;
        }

        int bonusId;
        String stringId;

        // Input and validate employee ID
        while (true) {
            System.out.print("\nEnter employee ID to apply bonus >> ");
            stringId = input.nextLine().trim();

            if (isValidInt(stringId)) {
                bonusId = Integer.parseInt(stringId);
                break;
            }
        }

        boolean found = false;

        // Search for employee by ID
        for(int i = 0; i < count; i++){
            if(ids[i] == bonusId){
                // If employee ID exist
                found = true;

                int bonusPerformanceScale;

                // Input and validate performance scale
                while (true) {
                    System.out.print("\nWhat is the performance of employee ID: "+ bonusId + ", " + names[i] +
                            " on a scale of 1-5? >> ");
                    String performanceInput = input.nextLine().trim();

                    if (isValidInt(performanceInput)) {
                        bonusPerformanceScale = Integer.parseInt(performanceInput);

                        // Allow user to only enter number 1 to 5
                        if (bonusPerformanceScale < 1 || bonusPerformanceScale > 5) {
                            System.out.println("* Please enter a number between 1 and 5 *");
                            continue;
                        }
                        break;
                    }
                }

                // Determine bonus percentage based on performance scale
                double bonusPercentage;
                if (bonusPerformanceScale == 5){
                    bonusPercentage = 20;
                }
                else if (bonusPerformanceScale == 4){
                    bonusPercentage = 15;
                }
                else if (bonusPerformanceScale == 3){
                    bonusPercentage = 10;
                }
                else {
                    bonusPercentage = 0;
                }

                // Calculate new salary and bonus amount
                double oldSalary = salaries[i];
                double bonusAmountInCash = oldSalary * (bonusPercentage / 100);
                double newSalary = oldSalary + bonusAmountInCash;

                // Update records
                bonuses[i] = bonusAmountInCash;
                salariesAfterBonuses[i] = newSalary;

                // Display results
                System.out.println("\n* " +
                        (bonusPercentage == 0 ?
                                "No Bonus Applied to Employee ID: " + bonusId + ", " + names[i] + "." :
                                "Bonus Applied to Employee ID: " + bonusId + ", " + names[i] + ".") +
                        " *\n");

                System.out.println("Bonus Received " + bonusPercentage + "%");
                System.out.printf("Old Salary: $%.2f%n", oldSalary);
                System.out.printf("New Salary: $%.2f%n", newSalary);
                break;
            }
        }

        // If employee ID not found
        if (!found) {
            System.out.println("* Employee Not Found. *");
        }
    }

    /** Displays all employee records **/
    public static void showEmployeeDetails() {
        title("Employee Records");

        if (count == 0) {
            System.out.println("* No Employees are added yet ... *");
            return;
        }

        // Record Header
        System.out.printf("\n%-10s %-20s %-20s %-15s %-20s\n", "ID", "Names", "Salaries ($)", "Bonuses ($)",
                "Salaries After Bonus ($)");

        // Display all employee records
        for(int i=0; i<count; i++){    // This is to iterate the array and print the entire table
            System.out.printf("%-10d %-20s %-20.2f %-15.2f %-20.2f\n",ids[i], names[i],salaries[i], bonuses[i],
                    salariesAfterBonuses[i]);
        }
    }

    /** Searches for an employee by ID **/
    public static void searchEmployee() {
        title("Search Employee");

        if (count == 0) {
            System.out.println("* No employees in the system. *");
            return;
        }

        String stringId;
        int searchId;

        // Input and validate employee ID
        while (true) {
            System.out.print("\nEnter employee ID to search >> ");
            stringId = input.nextLine().trim();

            if (isValidInt(stringId)) {
                searchId = Integer.parseInt(stringId);
                break;
            }

        }

        // Search and display employee details if found
        for (int i = 0; i < count; i++) {
            if (ids[i] == searchId) {
                System.out.println();
                System.out.println("* Employee Found: *");
                System.out.printf("ID: %d\nName: %s\nSalary: $%.2f\nBonus: $%.2f\nSalary After Bonus: $%.2f\n",
                        ids[i], names[i], salaries[i], bonuses[i], salariesAfterBonuses[i]);
                return;
            }
        }

        // if employee not found
        System.out.println("* Employee with ID " + searchId + " not found. *");
    }


    /* ----------------------------------------- Helper Methods ----------------------------------------- */

    /** Displays a formatted title for sections **/
    public static void title(String text) {
        System.out.println();
        System.out.printf("====================================== %s ======================================\n",
                text);
    }

    /** Validates employee name **/
    public static boolean isValidName(String name) {
        // Check if name is an empty string
        if (name.isEmpty()) {
            System.out.println("* Input cannot be empty. Please enter a valid name. *");
            return false;
        }

        // Ensure that name can only contain letters and spaces
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c) && c!= ' ') {
                System.out.println("* Name cannot be anything other than letters and spaces. Please try again. *");
                return false;
            }
        }

        // Check for duplicate names (case-insensitive)
        for (int i = 0; i < count; i++) {
            if (names[i] != null && names[i].equalsIgnoreCase(name)) {
                System.out.println("* An employee with this name already exists. Please try again. *");
                return false;
            }
        }
        return true;
    }

    /** Validates if input is not empty and is a non-negative integer **/
    public static boolean isValidInt(String valueString) {
        // Check if input is empty
        if (valueString.isEmpty()) {
            System.out.println("* Input cannot be empty. Please enter a valid Integer. *");
            return false;
        }

        // Check if input is a non-negative integer
        try {
            int valueInteger = Integer.parseInt(valueString);

            if (valueInteger < 0) {
                System.out.println("* Negative Integer not Allowed. Please try again. *");
                return false;
            }

        } catch (NumberFormatException e) {
            System.out.println("* Invalid Input. Please enter a valid Integer. *");
            return false;
        }
        return true;
    }

    /** Validates if input is non-negative double Salary **/
    public static boolean isValidSalary(String stringSalary) {
        if (stringSalary.isEmpty()) {
            System.out.println("* Input cannot be empty. Please enter a valid number. *");
            return false;
        }

        try {
            double salary = Double.parseDouble(stringSalary);

            if (salary < 0) {
                System.out.println("* Salary cannot be negative. Please try again. *");
                return false;
            }

        } catch (NumberFormatException e) {
            System.out.println("* Invalid Input. Please enter a valid number. *");
            return false;
        }
        return true;
    }

}