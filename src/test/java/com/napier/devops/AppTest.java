package com.napier.devops;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

// Note: TestInstance import isn't strictly needed here unless you change @BeforeAll
// import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        // Assuming App is located in src/main/java/com.napier.devops
        app = new App();
    }

    @Test
    void printSalariesTestNull()
    {
        app.printSalaries(null);
    }

    /**
     * Test case for printSalaries with an empty ArrayList.
     */
    @Test
    void printSalariesTestEmpty()
    {
        ArrayList<Employee> employees = new ArrayList<>();
        app.printSalaries(employees);
    }

    @Test
    void printSalariesTestContainsNull()
    {
        ArrayList<Employee> employess = new ArrayList<Employee>();
        employess.add(null);
        app.printSalaries(employess);
    }
    @Test
    void printSalaries()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        employees.add(emp);
        app.printSalaries(employees);
    }

}