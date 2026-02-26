# Taskflow API

A task management REST API built with Spring Boot 3 and PostgreSQL. This project demonstrates modern backend development practices including authentication, DTOs, validation, layered architecture, and database integration.

## Tech Stack

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Security**
- **JSON Web Tokens (jjwt)**
- **PostgreSQL 14**
- **Spring Data JPA / Hibernate**
- **Lombok** (reduces boilerplate)
- **Jakarta Validation** (request validation)
- **Maven** (dependency management)

## Features

- Full CRUD operations for task management
- RESTful API design with proper HTTP methods
- DTO pattern (separation of API and database layers)
- Request validation with custom error messages
- PostgreSQL database integration
- Layered architecture (Controller → Service → Repository)
- Auto-timestamping (createdOn, updatedOn)
- Environment-based configuration
- JWT Authentication
- Role-based endpoint protection
- BCrypt password hashing
- Global Exception Handling

## Architecture

```
com.taskflow/
├── auth/
│   ├── controller/
│   │    └── AuthController.java
│   ├── dto/
│   │    ├── AuthResponse.java
│   │    ├── LoginRequest.java
│   │    └── RegisterRequest.java
│   └── service/
│        └── AuthService.java
├── common/
│   ├── exception/
│   │    ├── ErrorResponse.java
│   │    ├── GlobalExceptionHandler.java
│   │    └── TaskNotFoundException.java
│   └── util/
│       └── JwtUtil.java # JWT class
├── security/
│   ├── JwtAuthenticationFilter.java
│   └── SecurityConfig.java
├── task/
│   ├── web/
│   │   ├── TaskController.java      # REST endpoints
│   │   └── dto/                      # Data Transfer Objects
│   │       ├── CreateTaskRequest.java
│   │       ├── UpdateTaskRequest.java
│   │       └── TaskResponse.java
│   ├── service/
│   │   └── TaskService.java         # Business logic
│   ├── persistence/
│   │   ├── TaskEntity.java          # JPA entity
│   │   └── TaskRepository.java      # Data access
│   └── mapper/
│       └── TaskMapper.java          # DTO ↔ Entity conversion
├── user/
│   ├── persistence/
│   │   ├── UserEntity.java
│   │   └── UserRepository.java
│   └── security/
│      └── UserDetailsServiceImpl.java
└── TaskflowApiApplication.java      # Application entry point

```

## Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL 14 or higher
- Maven 3.6+

### Database Setup

1. Install and start PostgreSQL:
```bash
brew install postgresql@14
brew services start postgresql@14
```

2. Create database and user:
```bash
psql -U postgres

CREATE DATABASE taskflow;
CREATE USER taskflow_user WITH PASSWORD 'taskflow_password';
GRANT ALL PRIVILEGES ON DATABASE taskflow TO taskflow_user;
\q
```

### Application Setup

1. Clone the repository:
```bash
git clone <your-repo-url>
cd taskflow-api
```

2. Configure environment (optional):
```bash
cp .env.example .env
# Edit .env with your database credentials
```

3. Build and run:
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## API Endpoints

### Authentication

| Method | Endpoint             | Description       |
|--------|----------------------|-------------------|
| POST   | `/api/auth/register` | Register new user |
| POST   | `/api/auth/login`    | Login as user     |

### Task Management

**All Task Management endpoints require a bearer token**

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/tasks` | Get all tasks |
| GET    | `/api/tasks/{id}` | Get task by ID |
| POST   | `/api/tasks` | Create new task |
| PUT    | `/api/tasks/{id}` | Replace existing task |
| PATCH  | `/api/tasks/{id}` | Partially update task |
| DELETE | `/api/tasks/{id}` | Delete task |

### Example Requests

**Create Task:**
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "title": "Complete project",
    "description": "Finish the Spring Boot API",
    "status": "In Progress",
    "priority": 1,
    "assignee": "Jamie"
  }'
```

**Get All Tasks:**
```bash
curl http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>"
```

**Update Task Status (PATCH):**
```bash
curl -X PATCH http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"status": "Completed"}'
```

**Register new user:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "username",
    "password": "password",
    "email": "username@gmail.com"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "username",
    "password": "password"
  }'
```

### Response Format

```json
{
  "id": 1,
  "title": "Complete project",
  "description": "Finish the Spring Boot API",
  "status": "In Progress",
  "assignee": "Jamie",
  "deadline": null,
  "priority": 1,
  "createdOn": "2025-11-05T13:17:01.710206",
  "updatedOn": null
}
```

## Validation Rules

- `title`: Required, max 1000 characters
- `description`: Optional, max 1000 characters
- `status`: Optional, max 50 characters
- `assignee`: Optional, max 100 characters
- `priority`: Optional, minimum value 1

## Testing

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report
```

## Roadmap

### Completed
- [x] PostgreSQL integration
- [x] DTO pattern implementation
- [x] JWT authentication & authorization
- [x] User entity and registration
- [x] Input validation
- [x] Exception handling with @ControllerAdvice

### Upcoming
- [ ] Unit tests (60% coverage target)
- [ ] Docker containerization
- [ ] AWS deployment (EC2/RDS)
- [ ] Apache Kafka integration (event-driven architecture)
- [ ] Observability (logging, metrics, tracing)
- [ ] API documentation (Swagger/OpenAPI)
- [ ] Final polish & optimization

## Environment Variables

| Variable     | Description | Default                                     |
|--------------|-------------|---------------------------------------------|
| `DB_URL`     | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/taskflow` |
| `DB_USERNAME` | Database username | `taskflow_user`                             |
| `DB_PASSWORD` | Database password | `taskflow_password`                         |
| `JWT_SECRET`  | Secret key for signing JWTs | (none - must be set)                        |

## Author

**Jamie Marini-Loebe**
- Portfolio: [github.com/JamieMariniLoebe](https://github.com/JamieMariniLoebe)
- LinkedIn: [linkedin.com/in/jamiemariniloebe](https://www.linkedin.com/in/jamiemariniloebe/)
- Email: jamie.loebe2@gmail.com

---