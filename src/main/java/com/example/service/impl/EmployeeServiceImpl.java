package com.example.service.impl;

import com.example.entity.Department;
import com.example.entity.Employee;
import com.example.repository.DepartmentRepository;
import com.example.repository.EmployeeRepository;
import com.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the EmployeeService interface
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findByDepartment(Department department) {
        return employeeRepository.findByDepartment(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findByName(String name) {
        return employeeRepository.findByNameContaining(name);
    }

    @Override
    public Employee save(Employee employee) {
        validateEmployee(employee);
        
        // Make sure department exists before saving the employee
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            Department department = departmentRepository.findById(employee.getDepartment().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found with id " + employee.getDepartment().getId()));
            employee.setDepartment(department);
        }
        
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteById(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Employee not found with id " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return employeeRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public Employee assignToDepartment(Long employeeId, Long departmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with id " + employeeId));
        
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id " + departmentId));
        
        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }
    
    /**
     * Validate employee data before saving
     * 
     * @param employee the employee to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateEmployee(Employee employee) {
        // Check for email uniqueness
        if (employee.getId() == null) {
            // New employee
            if (employeeRepository.existsByEmail(employee.getEmail())) {
                throw new IllegalArgumentException("Email already in use: " + employee.getEmail());
            }
        } else {
            // Existing employee
            Optional<Employee> existingEmp = employeeRepository.findByEmail(employee.getEmail());
            if (existingEmp.isPresent() && !existingEmp.get().getId().equals(employee.getId())) {
                throw new IllegalArgumentException("Email already in use: " + employee.getEmail());
            }
        }
        
        // Department is required
        if (employee.getDepartment() == null || employee.getDepartment().getId() == null) {
            throw new IllegalArgumentException("Department is required");
        }
    }
}
