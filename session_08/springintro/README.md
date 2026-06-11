# SpringIntro — Student Management System

A full-stack Spring Boot web application for managing student records with role-based security, PostgreSQL, and Docker support.

**Live demo:** [https://aptech-spring-boot-assignment-production.up.railway.app/](https://aptech-spring-boot-assignment-production.up.railway.app/)

---

## Tech Stack

| Layer          | Technology                                      |
|----------------|-------------------------------------------------|
| Language       | Java 17                                         |
| Framework      | Spring Boot 4.0.6                               |
| Web            | Spring MVC — Thymeleaf templates                |
| Security       | Spring Security (`@EnableMethodSecurity`, BCrypt, Remember-Me, CSRF) |
| Database       | PostgreSQL with Spring Data JPA / Hibernate     |
| Validation     | Jakarta Bean Validation (`@Valid` + `BindingResult`) |
| Monitoring     | Spring Boot Actuator (`/actuator/health`, `/actuator/info`) |
| Build          | Maven (wrapped — `mvnw` / `mvnw.cmd`)           |
| Container      | Docker (multi-stage, Eclipse Temurin 17 JRE)    |

---

## Features

### Student CRUD (role-gated)

| Action         | URL                          | Roles Required       |
|----------------|------------------------------|----------------------|
| List / Search  | `GET /students`              | Any authenticated user |
| Add            | `GET/POST /students/add`     | `INSTRUCTOR` or `ADMIN` |
| Edit           | `GET/POST /students/edit/{id}` | `INSTRUCTOR` or `ADMIN` |
| View Details   | `GET /students/{id}`         | Any authenticated user |
| Delete         | `GET /students/delete/{id}`  | `ADMIN` only         |

### Security & User Features

- **In-memory authentication** with 3 test accounts:

  | Username | Password  | Role          | Permissions              |
  |----------|-----------|---------------|--------------------------|
  | `alice`  | `alice123`  | `STUDENT`     | View only                |
  | `bob`    | `bob456`    | `INSTRUCTOR`  | View, Add, Edit          |
  | `admin`  | `admin789`  | `ADMIN`       | Full access (incl. Delete) |

- Custom login page with remember-me (7-day token)
- CSRF protection (auto-injected in Thymeleaf forms)
- Role-aware UI (buttons shown/hidden per role)
- `/admin/dashboard` — Admin-only dashboard with student count
- `/my-profile`, `/my-details`, `/context-demo` — security demo endpoints

### Validation

| Field       | Constraint                         |
|-------------|------------------------------------|
| Name        | 2–80 characters, required          |
| Course      | Required                           |
| Email       | Valid email format, unique in DB   |
| Phone       | 11 digits starting with `0`        |
| Grade       | One of `A`, `B`, `C`, `D`, `F`     |
| RegNumber   | Pattern `APT-YYYY-NNNN`, unique    |

### Other

- Search students by name (case-insensitive)
- Filter by grade and active status
- Custom error page (404)
- Health and info endpoints exposed via Actuator

---

## Database

The `students` table is auto-created by Hibernate (`ddl-auto=update`).  
Connection is configured via environment variables with local defaults:

```properties
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/spring_aptech_db}
spring.datasource.username=${DATABASE_USERNAME:postgres}
spring.datasource.password=${DATABASE_PASSWORD:dev1234$}
```

---

## Running Locally

```bash
mvnw.cmd spring-boot:run
```

The app starts at **http://localhost:5000**.

### Docker

```bash
docker build -t springintro .
docker run -p 5000:5000 -e DATABASE_URL=... springintro
```

---

## Project Structure

```
src/main/java/com/aptech/springintro/
├── config/
│   └── SecurityConfig.java
├── controller/
│   └── StudentController.java
├── model/
│   └── Student.java
├── repository/
│   └── StudentRepository.java
└── SpringintroApplication.java

src/main/resources/
├── templates/
│   ├── admin/dashboard.html
│   ├── add-student.html
│   ├── confirmation.html
│   ├── edit-student.html
│   ├── error.html
│   ├── login.html
│   ├── student-detail.html
│   └── students.html
└── application.properties
```

---

## Author

**Maduka Johnpeter**
