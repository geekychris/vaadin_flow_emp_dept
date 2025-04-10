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
package com.example.service.impl;

import com.example.entity.Department;
import com.example.repository.DepartmentRepository;
import com.example.repository.EmployeeRepository;
import com.example.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the DepartmentService interface
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> findByNameContaining(String name) {
        return departmentRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Department save(Department department) {
        // Check if department with same name already exists (for a different ID)
        if (department.getId() == null) {
            if (departmentRepository.existsByName(department.getName())) {
                throw new IllegalArgumentException("Department with name '" + department.getName() + "' already exists");
            }
        } else {
            Optional<Department> existingDept = departmentRepository.findByName(department.getName());
            if (existingDept.isPresent() && !existingDept.get().getId().equals(department.getId())) {
                throw new IllegalArgumentException("Department with name '" + department.getName() + "' already exists");
            }
        }
        
        return departmentRepository.save(department);
    }

    @Override
    public void deleteById(Long id) {
        // Check if department has employees
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id " + id));
                
        if (!department.getEmployees().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete department that has employees assigned to it");
        }
        
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return departmentRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }
}
