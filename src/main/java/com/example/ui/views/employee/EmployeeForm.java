package com.example.ui.views.employee;

import com.example.entity.Department;
import com.example.entity.Employee;
import com.example.service.DepartmentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Form for creating and editing Employee entities
 */
public class EmployeeForm extends FormLayout {

    private Employee employee;

    // Form fields
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private EmailField email = new EmailField("Email");
    private ComboBox<Department> department = new ComboBox<>("Department");
    
    // Binder for data binding
    private Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);
    
    // Buttons
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button cancel = new Button("Cancel");

    /**
     * Create a new employee form
     * 
     * @param departmentService the department service for loading departments
     */
    public EmployeeForm(DepartmentService departmentService) {
        addClassName("employee-form");
        
        // Configure form fields
        firstName.setRequired(true);
        lastName.setRequired(true);
        email.setRequired(true);
        department.setRequired(true);
        
        // Configure department combo box
        department.setItems(departmentService.findAll());
        department.setItemLabelGenerator(Department::getName);
        
        // Configure binder
        binder.bindInstanceFields(this);
        
        // Add components
        add(
            firstName,
            lastName,
            email,
            department,
            createButtonsLayout()
        );
    }

    /**
     * Create the button layout for the form
     * 
     * @return the button layout
     */
    private Component createButtonsLayout() {
        // Style buttons
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        
        // Configure button events
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        
        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, employee)));
        cancel.addClickListener(event -> fireEvent(new CancelEvent(this)));
        
        return new HorizontalLayout(save, delete, cancel);
    }

    /**
     * Validate form and save if valid
     */
    private void validateAndSave() {
        try {
            binder.writeBean(employee);
            fireEvent(new SaveEvent(this, employee));
        } catch (ValidationException e) {
            // Validation errors are shown automatically by the binder
        }
    }

    /**
     * Set the employee for the form
     * 
     * @param employee the employee to edit
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
        binder.readBean(employee);
        
        // Show delete button only for existing employees
        delete.setVisible(employee != null && employee.getId() != null);
    }
    
    /**
     * Refresh the department list
     * 
     * @param departments the updated list of departments
     */
    public void setDepartments(List<Department> departments) {
        department.setItems(departments);
    }
    
    // Events
    public static abstract class EmployeeFormEvent extends ComponentEvent<EmployeeForm> {
        private Employee employee;

        protected EmployeeFormEvent(EmployeeForm source, Employee employee) {
            super(source, false);
            this.employee = employee;
        }

        public Employee getEmployee() {
            return employee;
        }
    }

    public static class SaveEvent extends EmployeeFormEvent {
        SaveEvent(EmployeeForm source, Employee employee) {
            super(source, employee);
        }
    }

    public static class DeleteEvent extends EmployeeFormEvent {
        DeleteEvent(EmployeeForm source, Employee employee) {
            super(source, employee);
        }
    }

    public static class CancelEvent extends EmployeeFormEvent {
        CancelEvent(EmployeeForm source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }
}
