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
package com.example.ui.views.employee;

import com.example.entity.Department;
import com.example.entity.Employee;
import com.example.service.DepartmentService;
import com.example.service.EmployeeService;
import com.example.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * View for managing employees
 */
//@org.springframework.stereotype.Component
@Scope("prototype")
@Route(value = "employees", layout = MainLayout.class)
@PageTitle("Employees | Employee Management")
public class EmployeeView extends VerticalLayout {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    
    // UI components
    private Grid<Employee> grid = new Grid<>(Employee.class);
    private TextField filterText = new TextField();
    private ComboBox<Department> departmentFilter = new ComboBox<>();
    private EmployeeForm form;

    /**
     * Create a new employee view
     * 
     * @param employeeService the employee service
     * @param departmentService the department service
     */
    public EmployeeView(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        
        addClassName("employee-view");
        setSizeFull();
        
        configureGrid();
        configureForm();
        
        add(
            getToolbar(),
            getContent()
        );
        
        updateList();
        closeEditor();
    }

    /**
     * Configure the grid
     */
    private void configureGrid() {
        grid.addClassName("employee-grid");
        grid.setSizeFull();
        
        // Configure columns
        grid.setColumns(); // Clear default columns
        grid.addColumn(Employee::getFirstName).setHeader("First Name").setSortable(true);
        grid.addColumn(Employee::getLastName).setHeader("Last Name").setSortable(true);
        grid.addColumn(Employee::getEmail).setHeader("Email");
        grid.addColumn(employee -> employee.getDepartment().getName())
            .setHeader("Department").setSortable(true);
            
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        
        // Configure selection
        grid.asSingleSelect().addValueChangeListener(e -> editEmployee(e.getValue()));
    }

    /**
     * Configure the form
     */
    private void configureForm() {
        form = new EmployeeForm(departmentService);
        form.setWidth("25em");
        
        // Listen to form events
        form.addSaveListener(this::saveEmployee);
        form.addDeleteListener(this::deleteEmployee);
        form.addCancelListener(e -> closeEditor());
    }

    /**
     * Create the toolbar
     * 
     * @return the toolbar component
     */
    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        
        departmentFilter.setPlaceholder("Filter by department");
        departmentFilter.setClearButtonVisible(true);
        departmentFilter.setItems(departmentService.findAll());
        departmentFilter.setItemLabelGenerator(Department::getName);
        departmentFilter.addValueChangeListener(e -> updateList());
        
        Button addButton = new Button("Add Employee");
        addButton.addClickListener(e -> addEmployee());
        
        return new HorizontalLayout(filterText, departmentFilter, addButton);
    }

    /**
     * Create the main content layout
     * 
     * @return the content component
     */
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    /**
     * Update the employee list based on filters
     */
    private void updateList() {
        Department selectedDepartment = departmentFilter.getValue();
        String searchTerm = filterText.getValue().trim();
        
        if (selectedDepartment != null && !searchTerm.isEmpty()) {
            // Filter by both department and name
            grid.setItems(employeeService.findByDepartment(selectedDepartment).stream()
                    .filter(employee -> 
                            employee.getFirstName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                            employee.getLastName().toLowerCase().contains(searchTerm.toLowerCase()))
                    .toList());
        } else if (selectedDepartment != null) {
            // Filter by department only
            grid.setItems(employeeService.findByDepartment(selectedDepartment));
        } else if (!searchTerm.isEmpty()) {
            // Filter by name only
            grid.setItems(employeeService.findByName(searchTerm));
        } else {
            // No filters, show all
            grid.setItems(employeeService.findAll());
        }
    }

    /**
     * Add a new employee
     */
    private void addEmployee() {
        grid.asSingleSelect().clear();
        Employee employee = new Employee();
        
        // If a department filter is active, pre-select that department
        if (departmentFilter.getValue() != null) {
            employee.setDepartment(departmentFilter.getValue());
        } else if (!departmentService.findAll().isEmpty()) {
            // If no filter but departments exist, pre-select the first one
            employee.setDepartment(departmentService.findAll().get(0));
        }
        
        editEmployee(employee);
    }

    /**
     * Edit an employee
     * 
     * @param employee the employee to edit
     */
    private void editEmployee(Employee employee) {
        if (employee == null) {
            closeEditor();
        } else {
            form.setEmployee(employee);
            form.setDepartments(departmentService.findAll());
            form.setVisible(true);
            addClassName("editing");
        }
    }

    /**
     * Close the editor
     */
    private void closeEditor() {
        form.setEmployee(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    /**
     * Save an employee
     * 
     * @param event the save event
     */
    private void saveEmployee(EmployeeForm.SaveEvent event) {
        try {
            employeeService.save(event.getEmployee());
            updateList();
            closeEditor();
            showNotification("Employee saved successfully", false);
        } catch (Exception e) {
            showNotification("Error saving employee: " + e.getMessage(), true);
        }
    }

    /**
     * Delete an employee
     * 
     * @param event the delete event
     */
    private void deleteEmployee(EmployeeForm.DeleteEvent event) {
        try {
            employeeService.deleteById(event.getEmployee().getId());
            updateList();
            closeEditor();
            showNotification("Employee deleted", false);
        } catch (Exception e) {
            showNotification("Error deleting employee: " + e.getMessage(), true);
        }
    }

    /**
     * Show a notification
     * 
     * @param message the message
     * @param isError true if error notification
     */
    private void showNotification(String message, boolean isError) {
        Notification notification = Notification.show(message, 3000, Notification.Position.BOTTOM_CENTER);
        if (isError) {
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
    }
}
