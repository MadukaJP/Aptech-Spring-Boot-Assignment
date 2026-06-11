# Employee Management Portal — Aptech Corp

A Spring Boot web application for HR to manage employee records (CRUD) using JPA, Thymeleaf, and PostgreSQL.

## Tech Stack

- **Java 17** with Spring Boot 4.0.6
- **Spring MVC** (Thymeleaf templates)
- **Spring Data JPA** / **Hibernate**
- **PostgreSQL**
- **Bean Validation** (jakarta.validation)
- **Maven**

## Features

- List all employees with a summary count
- Add new employees with server-side validation
- Edit existing employee details
- Delete employees who have left
- Red inline validation errors on every form field

## Architecture

```
com.aptech.employeedir
├── model/
│   └── Employee.java
├── repository/
│   └── EmployeeRepository.java
├── controller/
│   └── EmployeeController.java
└── EmployeeDirApplication.java
```

## Database

This project uses **PostgreSQL**.  
Database: `aptech_corp_db` — tables are auto-created by Hibernate (`ddl-auto=update`).

### Connection config (`application.properties`)

```
spring.datasource.url=jdbc:postgresql://localhost:5432/spring_aptech_db
spring.datasource.username=postgres
spring.datasource.password=dev1234$
spring.datasource.driver-class-name=org.postgresql.Driver
```

## Employee Fields

| Field      | Constraints                               |
|------------|-------------------------------------------|
| fullName   | Required, 3–100 characters                |
| email      | Required, valid email format              |
| department | Required (e.g. HR, IT, Finance)           |
| salary     | Must be greater than 0                    |
| joinDate   | Required (format: YYYY-MM-DD)             |

## Running

```bash
./mvnw spring-boot:run
```

The app starts at **http://localhost:5000/employees**.

