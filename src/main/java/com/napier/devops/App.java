package com.napier.devops;

import java.sql.*;
import java.util.ArrayList;

public class App {
    /**
     * Connection to MySQL database.
     */
    // Renamed to 'db' for consistency with common database connection naming and the earlier method logic.
    private Connection db = null;

    // The rest of the methods are placed below main for standard Java convention
    // but their placement doesn't affect functionality.

    // ----------------------------------------------------------------------
    // METHOD: main
    // ----------------------------------------------------------------------

    /**
     * Main entry point for the application.
     */
    public static void main(String[] args) {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Extract employee salary information
        ArrayList<Employee> employees = a.getAllSalaries();

        // Test the size of the returned data
        if (employees != null && !employees.isEmpty()) {
            System.out.println("--------------------------------------------------");
            System.out.println("Show all employees with current salaries retrieved: " + employees.size());
            System.out.println("--------------------------------------------------");

            // Print the salaries
            a.printSalaries(employees);
        } else {
            System.out.println("Failed to retrieve or list employee salary data.");
        }

        //
        Department salesDept = a.getDepartment("Sales");
        if (salesDept != null) {
            System.out.println("\n--- Salaries for Department: " + salesDept.dept_name + " ---");
            ArrayList<Employee> salesEmployees = a.getSalariesByDepartment(salesDept);

            if (salesEmployees != null && !salesEmployees.isEmpty()) {
                a.printSalaries(salesEmployees);
            } else {
                System.out.println("No employees found in the " + salesDept.dept_name + " department.");
            }
        }


        // Disconnect from database
        a.disconnect();
    }

    // ----------------------------------------------------------------------
    // METHOD: printSalaries  <-- EXISTING METHOD
    // ----------------------------------------------------------------------

    /**
     * Prints a list of employees.
     *
     * @param employees The list of employees to print.
     */
    public void printSalaries(ArrayList<Employee> employees) {
        // Handle null input
        if (employees == null) {
            System.out.println("No employees to print.");
            return;
        }

        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees) {
            // Handle null employee in list
            if (emp == null) continue;

            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    // ----------------------------------------------------------------------
    // DATA RETRIEVAL METHODS
    // ----------------------------------------------------------------------

    /**
     * Gets all the current employees and salaries.
     *
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalaries() {
        try {
            // Create an SQL statement
            Statement stmt = db.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    // ----------------------------------------------------------------------
    // METHOD: getSalariesByDepartment  <-- NEW METHOD IMPLEMENTATION
    // ----------------------------------------------------------------------

    /**
     * Retrieves a list of all current employees and their salaries for a specific department.
     * * @param dept The Department object containing the department number (`dept_no`).
     * @return An ArrayList of Employee objects with current salary information.
     */
    public ArrayList<Employee> getSalariesByDepartment(Department dept) {
        ArrayList<Employee> employeesList = new ArrayList<>();

        // Ensure the Department object is valid before proceeding
        if (dept == null) {
            System.out.println("Department object is null. Cannot retrieve salaries.");
            return employeesList;
        }

        // Define the parameterized SQL query.
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, s.salary " +
                "FROM employees e " +
                "JOIN salaries s ON e.emp_no = s.emp_no " +
                "JOIN dept_emp de ON e.emp_no = de.emp_no " +
                "WHERE s.to_date = '9999-01-01' " +
                "AND de.dept_no = ? " + // Parameter for department number
                "ORDER BY e.emp_no ASC";

        try (
                // Use the connection object 'db'
                PreparedStatement ps = db.prepareStatement(sql)
        ) {
            // Set the parameter (dept_no from the Department object)
            // Assumes dept_no field is public or accessible via a getter
            ps.setString(1, dept.dept_no);

            // Execute the query
            try (ResultSet rs = ps.executeQuery()) {

                // Process the results
                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.emp_no = rs.getInt("e.emp_no");
                    emp.first_name = rs.getString("e.first_name");
                    emp.last_name = rs.getString("e.last_name");
                    emp.salary = rs.getInt("s.salary");

                    employeesList.add(emp);
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL Error while fetching salaries by department: " + e.getMessage());
            e.printStackTrace();
        }

        return employeesList;
    }


    // ----------------------------------------------------------------------
    // METHOD: getDepartment  <-- UPDATED TO INCLUDE LOGIC
    // ----------------------------------------------------------------------

    /**
     * Gets a department's details based on its name.
     *
     * @param dept_name The name of the department (e.g., "Sales").
     * @return The populated Department object or null if not found/error occurred.
     */
    public Department getDepartment(String dept_name) {
        try {
            // Use PreparedStatement for safer querying with a string input
            String strSelect = "SELECT dept_no, dept_name FROM departments WHERE dept_name = ?";
            PreparedStatement ps = db.prepareStatement(strSelect);
            ps.setString(1, dept_name);

            ResultSet rset = ps.executeQuery();

            // Return new Department if valid
            if (rset.next()) {
                Department dept = new Department();
                // Assuming your Department class has public fields: dept_no and dept_name
                dept.dept_no = rset.getString("dept_no");
                dept.dept_name = rset.getString("dept_name");
                return dept;
            } else {
                System.out.println("Department not found with name: " + dept_name);
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get department details");
            return null;
        }
    }


    // ----------------------------------------------------------------------
    // METHOD: displayEmployee (kept for original functionality)
    // ----------------------------------------------------------------------

    /**
     * Prints employee details to the console.
     *
     * @param emp The Employee object to display.
     */
    public void displayEmployee(Employee emp) {
        if (emp != null) {
            // Note: Many fields (title, salary, dept_name, manager) will be null or 0
            // because your current SQL query in getEmployee() only retrieves emp_no,
            // first_name, and last_name.
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept_name + "\n"
                            + "Manager: " + emp.manager + "\n");
        } else {
            System.out.println("Employee not found or invalid data.");
        }
    }

    // ----------------------------------------------------------------------
    // METHOD: getEmployee (kept for original functionality)
    // ----------------------------------------------------------------------

    /**
     * Gets an employee's basic details based on their ID.
     *
     * @param ID The employee number to retrieve.
     * @return The populated Employee object or null if not found/error occurred.
     */
    public Employee getEmployee(int ID) {
        try {
            // Create an SQL statement
            Statement stmt = db.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT emp_no, first_name, last_name "
                            + "FROM employees "
                            + "WHERE emp_no = " + ID;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                return emp;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }


    // ----------------------------------------------------------------------
    // METHODS: connect and disconnect
    // ----------------------------------------------------------------------

    /**
     * Connect to the MySQL database.
     */
    public void connect() {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                db = DriverManager.getConnection("jdbc:mysql://localhost:33060/employees?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (db != null) {
            try {
                // Close connection
                db.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }
}
