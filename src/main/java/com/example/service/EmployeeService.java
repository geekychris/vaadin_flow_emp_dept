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
