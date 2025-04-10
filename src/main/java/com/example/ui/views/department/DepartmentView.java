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
package com.example.ui.views.department;

import com.example.entity.Department;
import com.example.service.DepartmentService;
import com.example.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
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

/**
 * View for managing departments
 */
//@org.springframework.stereotype.Component
@Scope("prototype")
@Route(value = "departments", layout = MainLayout.class)
@PageTitle("Departments | Employee Management")
public class DepartmentView extends VerticalLayout {

    private final DepartmentService departmentService;
    
    // UI components
    private Grid<Department> grid = new Grid<>(Department.class);
    private TextField filterText = new TextField();
    private DepartmentForm form;

    /**
     * Create a new department view
     * 
     * @param departmentService the department service
     */
    public DepartmentView(DepartmentService departmentService) {
        this.departmentService = departmentService;
        
        addClassName("department-view");
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
        grid.addClassName("department-grid");
        grid.setSizeFull();
        
        // Configure columns
        grid.setColumns("name");
        grid.addColumn(Department::getDescription).setHeader("Description");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        
        // Configure selection
        grid.asSingleSelect().addValueChangeListener(e -> editDepartment(e.getValue()));
    }

    /**
     * Configure the form
     */
    private void configureForm() {
        form = new DepartmentForm();
        form.setWidth("25em");
        
        // Listen to form events
        form.addSaveListener(this::saveDepartment);
        form.addDeleteListener(this::deleteDepartment);
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
        
        Button addButton = new Button("Add Department");
        addButton.addClickListener(e -> addDepartment());
        
        return new HorizontalLayout(filterText, addButton);
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
     * Update the department list
     */
    private void updateList() {
        if (filterText.getValue().isEmpty()) {
            grid.setItems(departmentService.findAll());
        } else {
            grid.setItems(departmentService.findByNameContaining(filterText.getValue()));
        }
    }

    /**
     * Add a new department
     */
    private void addDepartment() {
        grid.asSingleSelect().clear();
        editDepartment(new Department());
    }

    /**
     * Edit a department
     * 
     * @param department the department to edit
     */
    private void editDepartment(Department department) {
        if (department == null) {
            closeEditor();
        } else {
            form.setDepartment(department);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    /**
     * Close the editor
     */
    private void closeEditor() {
        form.setDepartment(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    /**
     * Save a department
     * 
     * @param event the save event
     */
    private void saveDepartment(DepartmentForm.SaveEvent event) {
        try {
            departmentService.save(event.getDepartment());
            updateList();
            closeEditor();
            showNotification("Department saved successfully", false);
        } catch (Exception e) {
            showNotification("Error saving department: " + e.getMessage(), true);
        }
    }

    /**
     * Delete a department
     * 
     * @param event the delete event
     */
    private void deleteDepartment(DepartmentForm.DeleteEvent event) {
        try {
            departmentService.deleteById(event.getDepartment().getId());
            updateList();
            closeEditor();
            showNotification("Department deleted", false);
        } catch (Exception e) {
            showNotification("Error deleting department: " + e.getMessage(), true);
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
