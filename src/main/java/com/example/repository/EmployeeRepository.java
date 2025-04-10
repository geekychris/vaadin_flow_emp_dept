package com.example.repository;

import com.example.entity.Employee;
import com.example.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Employee entity
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    /**
     * Find an employee by email
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
     * Find employees where first name or last name contains the search string (case insensitive)
     * 
     * @param searchTerm the search term
     * @return list of matching employees
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Employee> findByNameContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Check if an employee with the given email exists
     * 
     * @param email the email to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
}
