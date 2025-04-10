package com.example.service;

import com.example.entity.Department;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing departments
 */
public interface DepartmentService {
    
    /**
     * Get all departments
     * 
     * @return list of all departments
     */
    List<Department> findAll();
    
    /**
     * Get a department by ID
     * 
     * @param id the department ID
     * @return the department if found
     */
    Optional<Department> findById(Long id);
    
    /**
     * Get a department by name
     * 
     * @param name the department name
     * @return the department if found
     */
    Optional<Department> findByName(String name);
    
    /**
     * Find departments with names containing the given string
     * 
     * @param name the name fragment to search for
     * @return list of matching departments
     */
    List<Department> findByNameContaining(String name);
    
    /**
     * Save a department
     * 
     * @param department the department to save
     * @return the saved department
     * @throws IllegalArgumentException if department name already exists
     */
    Department save(Department department);
    
    /**
     * Delete a department by ID
     * 
     * @param id the department ID
     * @throws IllegalArgumentException if department has employees
     */
    void deleteById(Long id);
    
    /**
     * Check if a department exists by ID
     * 
     * @param id the department ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long id);
    
    /**
     * Check if a department with the given name exists
     * 
     * @param name the department name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
}
