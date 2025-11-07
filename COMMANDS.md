# Taskflow API - Command Reference

Quick reference for daily development and interview prep.

---

## Database Commands

### Connect to Database
```bash
# Connect as taskflow_user
psql -U taskflow_user -d taskflow

# Connect with password prompt
PGPASSWORD=taskflow_password psql -U taskflow_user -d taskflow
```

### Database Operations (Inside psql)
```sql
-- List all databases
\l

-- List all tables
\dt

-- Describe task_entity table structure
\d task_entity

-- View all tasks
SELECT * FROM task_entity;

-- Count tasks
SELECT COUNT(*) FROM task_entity;

-- View tasks with specific status
SELECT * FROM task_entity WHERE status = 'COMPLETED';

-- Delete all tasks (careful!)
DELETE FROM task_entity;

-- Exit psql
\q
```

### Database Administration
```bash
# Check if PostgreSQL is running
brew services list | grep postgresql

# Start PostgreSQL
brew services start postgresql@14

# Stop PostgreSQL
brew services stop postgresql@14

# Restart PostgreSQL
brew services restart postgresql@14
```

---

## Maven & Build Commands

### Build Project
```bash
# Clean and build (runs tests)
mvn clean install

# Build without tests (faster)
mvn clean install -DskipTests

# Just compile
mvn compile

# Just run tests
mvn test

# Clean only
mvn clean
```

### Check Dependencies
```bash
# List all dependencies
mvn dependency:tree

# Update dependencies
mvn versions:display-dependency-updates
```

---

## Running the Application

### Start Application
```bash
# Run with Maven
mvn spring-boot:run

# Run in background
mvn spring-boot:run &

# Run with specific port
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

# Run the JAR directly (after mvn install)
java -jar target/taskflow-api-0.0.1-SNAPSHOT.jar
```

### Stop Application
```bash
# If running in foreground: Ctrl+C

# If running in background, find process
ps aux | grep taskflow

# Kill by process ID
kill <PID>

# Or kill all Java processes (careful!)
pkill -f taskflow
```

---

## API Testing with curl

### Task Endpoints

#### Get All Tasks
```bash
curl http://localhost:8080/api/tasks
```

#### Get Task by ID
```bash
curl http://localhost:8080/api/tasks/1
```

#### Create New Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project",
    "description": "Finish the Spring Boot API",
    "status": "IN_PROGRESS",
    "priority": 1,
    "assignee": "Jamie",
    "deadline": "2025-12-31T23:59:59"
  }'
```

#### Update Task (PUT - Full Replacement)
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated title",
    "description": "Updated description",
    "status": "COMPLETED",
    "priority": 2,
    "assignee": "Jamie",
    "deadline": "2025-12-31T23:59:59"
  }'
```

#### Partial Update (PATCH)
```bash
curl -X PATCH http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "COMPLETED"
  }'
```

#### Delete Task
```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

### Pretty Print JSON Response
```bash
# Install jq for pretty JSON
brew install jq

# Use with curl
curl http://localhost:8080/api/tasks | jq
```

---

## Git Commands (Version Control)

### Initial Setup
```bash
# Initialize repository
git init

# Add all files
git add .

# First commit
git commit -m "Initial commit"

# Add remote repository
git remote add origin <your-repo-url>

# Push to remote
git push -u origin main
```

### Daily Workflow
```bash
# Check status
git status

# View changes
git diff

# Add specific file
git add src/main/java/com/taskflow/task/service/TaskService.java

# Commit with message
git commit -m "Add task service implementation"

# Push to remote
git push

# Pull latest changes
git pull

# View commit history
git log --oneline

# Create new branch
git checkout -b feature/add-authentication

# Switch branches
git checkout main

# Merge branch
git merge feature/add-authentication
```

---

## Docker Commands (Coming Soon)

### Build and Run
```bash
# Build Docker image
docker build -t taskflow-api .

# Run container
docker run -p 8080:8080 taskflow-api

# Run with Docker Compose
docker-compose up

# Run in background
docker-compose up -d

# Stop containers
docker-compose down

# View running containers
docker ps

# View logs
docker logs <container-id>

# Execute command in container
docker exec -it <container-id> bash
```

---

## Testing Commands

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TaskServiceTest

# Run specific test method
mvn test -Dtest=TaskServiceTest#testFindById

# Run with coverage report
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

---

## Troubleshooting

### Check Application Health
```bash
# Check if app is running
curl http://localhost:8080/actuator/health

# Check what's using port 8080
lsof -i :8080

# Kill process on port 8080
kill -9 $(lsof -t -i:8080)
```

### Database Issues
```bash
# Test database connection
psql -U taskflow_user -d taskflow -c "SELECT version();"

# Check PostgreSQL logs
tail -f /opt/homebrew/var/log/postgresql@14.log

# Recreate database (careful - deletes all data!)
psql -U jamiemariniloebe -d postgres -c "DROP DATABASE taskflow;"
psql -U jamiemariniloebe -d postgres -c "CREATE DATABASE taskflow OWNER taskflow_user;"
```

### Clean Build Issues
```bash
# Full clean rebuild
mvn clean install -U

# Clear Maven cache
rm -rf ~/.m2/repository

# Clear target directory manually
rm -rf target/
```

---

## Nov 4, 2025 - Verified Working Commands (DTOs + PostgreSQL)

### PostgreSQL Verification
```bash
# Quick connection test
psql -U taskflow_user -d taskflow -c "SELECT 1;"

# Query tasks with specific columns
psql -U taskflow_user -d taskflow -c "SELECT id, title, status, priority, created_on FROM task_entity;"
```

### API Testing with DTOs
```bash
# Create task with validation (returns TaskResponse DTO)
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"First Task","description":"Testing DTOs with PostgreSQL","status":"Pending","priority":1}' \
  -w "\nHTTP Status: %{http_code}\n"

# Get all tasks (returns array of TaskResponse DTOs)
curl http://localhost:8080/api/tasks -w "\nHTTP Status: %{http_code}\n"

# Test validation - POST without required title (should return 400)
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"description":"No title provided","priority":2}' \
  -w "\nHTTP Status: %{http_code}\n"
```

### Expected Responses
```json
// POST success (200 OK):
{
  "id": 1,
  "title": "First Task",
  "description": "Testing DTOs with PostgreSQL",
  "status": "Pending",
  "assignee": null,
  "deadline": null,
  "priority": 1,
  "createdOn": "2025-11-05T13:17:01.710206",
  "updatedOn": null
}

// GET all (200 OK):
[{
  "id": 1,
  "title": "First Task",
  "description": "Testing DTOs with PostgreSQL",
  "status": "Pending",
  "assignee": null,
  "deadline": null,
  "priority": 1,
  "createdOn": "2025-11-05T13:17:01.710206",
  "updatedOn": null
}]

// Validation failure (400 Bad Request):
{
  "timestamp": "2025-11-05T19:19:26.071+00:00",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/tasks"
}
```

---

## Useful One-Liners

### Quick Database Reset
```bash
psql -U taskflow_user -d taskflow -c "TRUNCATE TABLE task_entity RESTART IDENTITY CASCADE;"
```

### Quick Test API
```bash
# Create and immediately fetch task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Test","description":"Quick test","status":"PENDING","priority":1}' \
  && curl http://localhost:8080/api/tasks
```

### Watch Logs in Real-Time
```bash
# Start app and watch logs
mvn spring-boot:run | grep -E 'ERROR|WARN|Started'
```

---

## Quick Demo Commands

### "Show me how you run your application"
```bash
mvn spring-boot:run
```

### "How do you test your endpoints?"
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Interview Demo","description":"Testing API","status":"PENDING","priority":1}'

curl http://localhost:8080/api/tasks | jq
```

### "How do you check the database?"
```bash
psql -U taskflow_user -d taskflow -c "SELECT * FROM task_entity;"
```

### "How do you run tests?"
```bash
mvn test
```

### "How do you build for deployment?"
```bash
mvn clean package -DskipTests
java -jar target/taskflow-api-0.0.1-SNAPSHOT.jar
```

---

## Environment Variables (Production)

```bash
# Set database credentials via environment
export DB_URL=jdbc:postgresql://localhost:5432/taskflow
export DB_USERNAME=taskflow_user
export DB_PASSWORD=taskflow_password

# Run with environment variables
mvn spring-boot:run
```

---

## Quick Reference URLs

- Application: http://localhost:8080
- H2 Console (if enabled): http://localhost:8080/h2-console
- Actuator Health: http://localhost:8080/actuator/health
- API Docs (Swagger - when added): http://localhost:8080/swagger-ui.html

---

**Last Updated:** November 5, 2025
**Project:** Taskflow API - Portfolio Project
**Tech Stack:** Spring Boot 3.5.6, PostgreSQL 14, Java 17
**Status:** DTOs + Validation + PostgreSQL integration complete ✓
