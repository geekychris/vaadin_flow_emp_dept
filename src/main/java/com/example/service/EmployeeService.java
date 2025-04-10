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
package com.example.service;

import com.example.entity.Department;
import com.example.entity.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing employees
 */
public interface EmployeeService {
    
    /**
     * Get all employees
     * 
     * @return list of all employees
     */
    List<Employee> findAll();
    
    /**
     * Get an employee by ID
     * 
     * @param id the employee ID
     * @return the employee if found
     */
    Optional<Employee> findById(Long id);
    
    /**
     * Get an employee by email
     * 
     * @param email the employee email
     * @return the employee if found
     */
    Optional<Employee> findByEmail(String email);
    
    /**
     * Find employees by department
     * 
     * @param department the department
     * @return list of employees in the department
     */
    List<Employee> findByDepartment(Department department);
    
    /**
     * Find employees by department ID
     * 
     * @param departmentId the department ID
     * @return list of employees in the department
     */
    List<Employee> findByDepartmentId(Long departmentId);
    
    /**
     * Find employees by name (first name or last name)
     * 
     * @param name the name to search for
     * @return list of employees matching the name
     */
    List<Employee> findByName(String name);
    
    /**
     * Save an employee
     * 
     * @param employee the employee to save
     * @return the saved employee
     * @throws IllegalArgumentException if email already exists or department not found
     */
    Employee save(Employee employee);
    
    /**
     * Delete an employee by ID
     * 
     * @param id the employee ID
     * @throws IllegalArgumentException if employee not found
     */
    void deleteById(Long id);
    
    /**
     * Check if an employee exists by ID
     * 
     * @param id the employee ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long id);
    
    /**
     * Check if an employee with the given email exists
     * 
     * @param email the employee email
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Assign an employee to a department
     * 
     * @param employeeId the employee ID
     * @param departmentId the department ID
     * @return the updated employee
     * @throws IllegalArgumentException if employee or department not found
     */
    Employee assignToDepartment(Long employeeId, Long departmentId);
}
