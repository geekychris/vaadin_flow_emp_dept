package com.example.repository;

import com.example.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Department entity
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    /**
     * Find a department by its name
     * 
     * @param name the department name
     * @return the department if found
     */
    Optional<Department> findByName(String name);
    
    /**
     * Find departments where name contains the given string (case insensitive)
     * 
     * @param name the name fragment to search for
     * @return list of matching departments
     */
    List<Department> findByNameContainingIgnoreCase(String name);
    
    /**
     * Check if a department with the given name exists
     * 
     * @param name the department name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
}
