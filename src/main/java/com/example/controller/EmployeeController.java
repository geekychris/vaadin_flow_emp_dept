package com.example.controller;

import com.example.entity.Employee;
import com.example.service.EmployeeService;
import com.example.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST Controller for Employee entity
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    /**
     * Get all employees
     * 
     * @return list of all employees
     */
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    /**
     * Get an employee by ID
     * 
     * @param id the employee ID
     * @return the employee
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id " + id));
    }

    /**
     * Create a new employee
     * 
     * @param employee the employee to create
     * @return the created employee
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        // Ensure a new employee is created, not an update
        if (employee.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new employee cannot have an ID");
        }
        
        try {
            return employeeService.save(employee);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Update an existing employee
     * 
     * @param id the employee ID
     * @param employeeDetails the updated employee details
     * @return the updated employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employeeDetails) {
        if (!employeeService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id " + id);
        }
        
        employeeDetails.setId(id); // Ensure the ID is set correctly
        
        try {
            Employee updatedEmployee = employeeService.save(employeeDetails);
            return ResponseEntity.ok(updatedEmployee);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Delete an employee
     * 
     * @param id the employee ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (!employeeService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id " + id);
        }
        
        try {
            employeeService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Search employees by name (first name or last name)
     * 
     * @param name the name to search for
     * @return list of matching employees
     */
    @GetMapping("/search")
    public List<Employee> searchEmployees(@RequestParam String name) {
        return employeeService.findByName(name);
    }

    /**
     * Get employees by department ID
     * 
     * @param departmentId the department ID
     * @return list of employees in the department
     */
    @GetMapping("/department/{departmentId}")
    public List<Employee> getEmployeesByDepartment(@PathVariable Long departmentId) {
        if (!departmentService.existsById(departmentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id " + departmentId);
        }
        
        return employeeService.findByDepartmentId(departmentId);
    }

    /**
     * Assign an employee to a department
     * 
     * @param id the employee ID
     * @param departmentId the department ID
     * @return the updated employee
     */
    @PutMapping("/{id}/department/{departmentId}")
    public ResponseEntity<Employee> assignEmployeeToDepartment(@PathVariable Long id, @PathVariable Long departmentId) {
        if (!employeeService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id " + id);
        }
        
        if (!departmentService.existsById(departmentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id " + departmentId);
        }
        
        try {
            Employee updatedEmployee = employeeService.assignToDepartment(id, departmentId);
            return ResponseEntity.ok(updatedEmployee);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
