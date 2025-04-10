package com.example.controller;

import com.example.entity.Department;
import com.example.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST Controller for Department entity
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Get all departments
     * 
     * @return list of all departments
     */
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.findAll();
    }

    /**
     * Get a department by ID
     * 
     * @param id the department ID
     * @return the department
     */
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id " + id));
    }

    /**
     * Create a new department
     * 
     * @param department the department to create
     * @return the created department
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Department createDepartment(@Valid @RequestBody Department department) {
        // Ensure a new department is created, not an update
        if (department.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new department cannot have an ID");
        }
        
        try {
            return departmentService.save(department);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Update an existing department
     * 
     * @param id the department ID
     * @param departmentDetails the updated department details
     * @return the updated department
     */
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @Valid @RequestBody Department departmentDetails) {
        if (!departmentService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id " + id);
        }
        
        departmentDetails.setId(id); // Ensure the ID is set correctly
        
        try {
            Department updatedDepartment = departmentService.save(departmentDetails);
            return ResponseEntity.ok(updatedDepartment);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Delete a department
     * 
     * @param id the department ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (!departmentService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found with id " + id);
        }
        
        try {
            departmentService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Search departments by name
     * 
     * @param name the name to search for
     * @return list of matching departments
     */
    @GetMapping("/search")
    public List<Department> searchDepartments(@RequestParam String name) {
        return departmentService.findByNameContaining(name);
    }
}
