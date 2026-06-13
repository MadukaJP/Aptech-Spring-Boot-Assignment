# Course Service

A Spring Boot microservice for managing courses and verifying student enrollment via the Student Service.

## Tech Stack

- Java 17, Spring Boot 4.0.7, Spring Cloud 2025.1.2
- MySQL, JPA / Hibernate, HikariCP
- OpenFeign (declarative REST client)
- Maven

## Prerequisites

- Java 17+
- Maven
- MySQL (or use the default remote DB configured in `application.properties`)

## Configuration

All settings are in `src/main/resources/application.properties` and overridable via environment variables:

| Variable                   | Default                            | Description                    |
|----------------------------|-------------------------------------|--------------------------------|
| `PORT`                     | `5001`                              | Server port                    |
| `DATABASE_URL`             | `jdbc:mysql://<host>/<database>`    | JDBC URL                       |
| `DATABASE_USERNAME`        | `<db-username>`                     | DB username                    |
| `DATABASE_PASSWORD`        | `<db-password>`                     | DB password                    |
| `STUDENT_SERVICE_URL`      | `http://localhost:5000`             | Student Service base URL       |
| `STUDENT_SERVICE_API_KEY`  | `placeholder-api-key`               | API key for Student Service    |

## Running

```bash
./mvnw spring-boot:run
```

The service starts on `http://localhost:5001`.

A live instance is available at: `https://course-service-production-c6eb.up.railway.app`

## API Endpoints

### Courses

| Method | Path      | Description            |
|--------|-----------|------------------------|
| POST   | `/course` | Create a new course    |
| GET    | `/course` | List all courses       |

**POST /course** expects a JSON body:
```json
{
  "title": "Java Spring Boot",
  "instructor": "John Doe",
  "durationMonths": 4
}
```

### Enrollment Verification

| Method | Path                            | Description                        |
|--------|---------------------------------|------------------------------------|
| GET    | `/course/verify-student/{id}`   | Verify student via Student Service |

This endpoint calls the Student Service at `localhost:5000`. Handles 404 (student not found) and connection failures gracefully.

## Architecture

This service is one piece of a microservice ecosystem. It manages its own course database via JPA and communicates with a separate Student Service through OpenFeign for enrollment verification. The Student Service URL is configured via the `STUDENT_SERVICE_URL` environment variable.
