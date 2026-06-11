# SpringIntro - Student Management System

A Spring Boot web application for managing student records (CRUD operations) with JPA, Thymeleaf, and PostgreSQL.

## Tech Stack

- **Java 17** with Spring Boot 4.0.6
- **Spring MVC** (Thymeleaf templates)
- **Spring Data JPA** / **Hibernate**
- **PostgreSQL** (instead of MySQL)
- **Bean Validation** (jakarta.validation)
- **Maven**

## Features

- List all students with search by name
- Add, edit, view, and delete students
- Server-side validation with error messages
- Consistent styling across all pages

## Database

This project uses **PostgreSQL** (configured in `application.properties`).  
The `students` table is auto-created by Hibernate (`ddl-auto=update`).

### Connection config (example)

```
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
```

## Student Fields

| Field       | Constraints                           |
|-------------|---------------------------------------|
| Name        | 2–80 characters                       |
| Course      | Required                              |
| Email       | Valid email, unique                   |
| Phone       | 11 digits starting with `0`           |
| Grade       | One of A, B, C, D, F                  |
| RegNumber   | Pattern `APT-YYYY-NNNN`, unique       |

## Running

```bash
./mvnw spring-boot:run
```

The app starts at **http://localhost:5000**.
