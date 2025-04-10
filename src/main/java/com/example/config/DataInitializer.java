/*
 * MIT License
 *
 * Copyright (c) 2025 Chris Collins (chris@hitorro.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.config;

import com.example.entity.Department;
import com.example.entity.Employee;
import com.example.service.DepartmentService;
import com.example.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to initialize sample data
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    
    /**
     * Initialize sample data on application startup
     */
    @Bean
    public CommandLineRunner initData(DepartmentService departmentService, EmployeeService employeeService) {
        return args -> {
            // Only add data if no departments exist
            if (departmentService.findAll().isEmpty()) {
                log.info("Initializing sample data...");
                initializeSampleData(departmentService, employeeService);
            } else {
                log.info("Database already contains data, skipping initialization");
            }
        };
    }
    
    /**
     * Create sample departments and employees
     */
    private void initializeSampleData(DepartmentService departmentService, EmployeeService employeeService) {
        // Create departments
        Department engineering = departmentService.save(
                new Department("Engineering", "Software development and engineering teams"));
                
        Department marketing = departmentService.save(
                new Department("Marketing", "Product marketing and customer outreach"));
                
        Department hr = departmentService.save(
                new Department("Human Resources", "Employee management and hiring"));
                
        Department finance = departmentService.save(
                new Department("Finance", "Financial planning and accounting"));
                
        Department operations = departmentService.save(
                new Department("Operations", "Day-to-day business operations"));
        
        // Create employees in Engineering
        employeeService.save(new Employee("John", "Smith", "john.smith@example.com", engineering));
        employeeService.save(new Employee("Emily", "Johnson", "emily.johnson@example.com", engineering));
        employeeService.save(new Employee("Michael", "Williams", "michael.williams@example.com", engineering));
        employeeService.save(new Employee("David", "Brown", "david.brown@example.com", engineering));
        
        // Create employees in Marketing
        employeeService.save(new Employee("Sarah", "Davis", "sarah.davis@example.com", marketing));
        employeeService.save(new Employee("Jessica", "Miller", "jessica.miller@example.com", marketing));
        employeeService.save(new Employee("Daniel", "Wilson", "daniel.wilson@example.com", marketing));
        
        // Create employees in HR
        employeeService.save(new Employee("Lisa", "Moore", "lisa.moore@example.com", hr));
        employeeService.save(new Employee("Robert", "Taylor", "robert.taylor@example.com", hr));
        
        // Create employees in Finance
        employeeService.save(new Employee("Jennifer", "Anderson", "jennifer.anderson@example.com", finance));
        employeeService.save(new Employee("James", "Thomas", "james.thomas@example.com", finance));
        employeeService.save(new Employee("Patricia", "Jackson", "patricia.jackson@example.com", finance));
        
        // Create employees in Operations
        employeeService.save(new Employee("Richard", "White", "richard.white@example.com", operations));
        employeeService.save(new Employee("Elizabeth", "Harris", "elizabeth.harris@example.com", operations));
        
        log.info("Sample data initialized with {} departments and {} employees", 
                departmentService.findAll().size(), employeeService.findAll().size());
    }
}
