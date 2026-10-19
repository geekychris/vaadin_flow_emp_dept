<p align="center">
  <img src="assets/logo.png" alt="Vaadin Flow Emp Dept logo" width="180">
</p>

# Employee Department Management System

A Spring Boot application with Vaadin Flow UI that provides a complete management system for employees and departments. The application allows creating, updating, and deleting employees and departments via both a web interface and REST API.

## Features

- **Department Management**: Create, read, update, and delete departments
- **Employee Management**: Create, read, update, and delete employees with department assignments
- **Responsive Web UI**: Built with Vaadin Flow for a modern, responsive user interface
- **REST API**: Complete API for integrating with other systems
- **Validation**: Input validation for all operations
- **Search & Filter**: Search employees by name and filter by department
- **In-Memory Database**: H2 database for easy setup and testing

## Technology Stack

- **Backend**: 
  - Java 21
  - Spring Boot 3.2.3
  - Spring Data JPA
  - H2 Database
  
- **Frontend**:
  - Vaadin Flow 24.3.5
  - HTML5/CSS3

## Setup & Running

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Build and run the application:

```bash
mvn clean install
mvn spring-boot:run
```

The application will be available at: http://localhost:8080/employee-mgmt/

### H2 Database Console

H2 console is available at: http://localhost:8080/employee-mgmt/h2-console

Use these connection details:
- JDBC URL: `jdbc:h2:mem:employeedb`
- Username: `sa`
- Password: `password`

## Available Endpoints

### Vaadin UI Endpoints

- `/employee-mgmt` - Department management view (home)
- `/employee-mgmt/employees` - Employee management view

### REST API Endpoints

#### Departments

- `GET /employee-mgmt/api/departments` - List all departments
- `GET /employee-mgmt/api/departments/{id}` - Get a specific department
- `POST /employee-mgmt/api/departments` - Create a new department
- `PUT /employee-mgmt/api/departments/{id}` - Update a department
- `DELETE /employee-mgmt/api/departments/{id}` - Delete a department
- `GET /employee-mgmt/api/departments/search?name={name}` - Search departments by name

#### Employees

- `GET /employee-mgmt/api/employees` - List all employees
- `GET /employee-mgmt/api/employees/{id}` - Get a specific employee
- `POST /employee-mgmt/api/employees` - Create a new employee
- `PUT /employee-mgmt/api/employees/{id}` - Update an employee
- `DELETE /employee-mgmt/api/employees/{id}` - Delete an employee
- `GET /employee-mgmt/api/employees/search?name={name}` - Search employees by name
- `GET /employee-mgmt/api/employees/department/{departmentId}` - Get employees by department
- `PUT /employee-mgmt/api/employees/{id}/department/{departmentId}` - Assign employee to department

## Using the Application

### Vaadin UI Usage

1. Navigate to `/employee-mgmt` to manage departments:
   - View all departments in the grid
   - Click on a department to edit
   - Use the "Add Department" button to create a new department
   - Filter departments by name using the search field

2. Navigate to `/employee-mgmt/employees` to manage employees:
   - View all employees in the grid
   - Filter employees by name or department
   - Click on an employee to edit
   - Use the "Add Employee" button to create a new employee

### REST API Usage Examples

#### Creating a Department

```bash
curl -X POST http://localhost:8080/employee-mgmt/api/departments \
  -H "Content-Type: application/json" \
  -d '{"name":"Research","description":"Research and Development"}'
```

#### Creating an Employee

```bash
curl -X POST http://localhost:8080/employee-mgmt/api/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john.doe@example.com","department":{"id":1}}'
```

#### Getting All Employees in a Department

```bash
curl -X GET http://localhost:8080/employee-mgmt/api/employees/department/1
```

## Sample Data

The application comes with pre-loaded sample data including:
- 5 departments (Engineering, Marketing, Human Resources, Finance, Operations)
- 15 employees across these departments

This sample data is automatically loaded on first startup for testing purposes.

## Project Structure

```
src/main/java/com/example/
├── Application.java                  # Main application class
├── config/
│   └── DataInitializer.java          # Sample data initialization
├── controller/
│   ├── DepartmentController.java     # REST API for departments
│   └── EmployeeController.java       # REST API for employees
├── entity/
│   ├── Department.java               # Department entity
│   └── Employee.java                 # Employee entity
├── repository/
│   ├── DepartmentRepository.java     # Department data access
│   └── EmployeeRepository.java       # Employee data access
├── service/
│   ├── DepartmentService.java        # Department business logic interface
│   ├── EmployeeService.java          # Employee business logic interface
│   └── impl/
│       ├── DepartmentServiceImpl.java # Department service implementation
│       └── EmployeeServiceImpl.java   # Employee service implementation
└── ui/
    ├── MainLayout.java               # Main UI layout
    └── views/
        ├── department/
        │   ├── DepartmentForm.java   # Department edit form
        │   └── DepartmentView.java   # Department view
        └── employee/
            ├── EmployeeForm.java     # Employee edit form
            └── EmployeeView.java     # Employee view
```
