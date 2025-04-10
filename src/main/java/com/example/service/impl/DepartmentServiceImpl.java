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
