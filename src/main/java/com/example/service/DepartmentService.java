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
