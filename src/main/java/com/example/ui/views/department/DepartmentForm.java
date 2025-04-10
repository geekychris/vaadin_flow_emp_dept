package com.example.ui.views.department;

import com.example.entity.Department;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

/**
 * Form for creating and editing Department entities
 */
public class DepartmentForm extends FormLayout {

    private Department department;

    // Form fields
    private TextField name = new TextField("Name");
    private TextArea description = new TextArea("Description");
    
    // Binder for data binding
    private Binder<Department> binder = new BeanValidationBinder<>(Department.class);
    
    // Buttons
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button cancel = new Button("Cancel");

    /**
     * Create a new department form
     */
    public DepartmentForm() {
        addClassName("department-form");
        
        // Configure form fields
        name.setRequired(true);
        description.setMinHeight("100px");
        
        // Configure binder
        binder.bindInstanceFields(this);
        
        // Add components
        add(
            name,
            description,
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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, department)));
        cancel.addClickListener(event -> fireEvent(new CancelEvent(this)));
        
        return new HorizontalLayout(save, delete, cancel);
    }

    /**
     * Validate form and save if valid
     */
    private void validateAndSave() {
        try {
            binder.writeBean(department);
            fireEvent(new SaveEvent(this, department));
        } catch (ValidationException e) {
            // Validation errors are shown automatically by the binder
        }
    }

    /**
     * Set the department for the form
     * 
     * @param department the department to edit
     */
    public void setDepartment(Department department) {
        this.department = department;
        binder.readBean(department);
        
        // Show delete button only for existing departments
        delete.setVisible(department != null && department.getId() != null);
    }
    
    // Events
    public static abstract class DepartmentFormEvent extends ComponentEvent<DepartmentForm> {
        private Department department;

        protected DepartmentFormEvent(DepartmentForm source, Department department) {
            super(source, false);
            this.department = department;
        }

        public Department getDepartment() {
            return department;
        }
    }

    public static class SaveEvent extends DepartmentFormEvent {
        SaveEvent(DepartmentForm source, Department department) {
            super(source, department);
        }
    }

    public static class DeleteEvent extends DepartmentFormEvent {
        DeleteEvent(DepartmentForm source, Department department) {
            super(source, department);
        }
    }

    public static class CancelEvent extends DepartmentFormEvent {
        CancelEvent(DepartmentForm source) {
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
