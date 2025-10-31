# Flashcards Application

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)
[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards)

## Description

Flashcards is a Java Spring Boot application for managing flashcards and their categories based on DevOps technologies and one-liner scripts.
It was designed as a demonstration project to practise and illustrate DevOps concepts:

## Features:

* Full CRUD on Categories and Flashcards
* Simple, extensible and testable REST API
* Automatic data injection via data.sql
* Unit and integration tests

## Technologies used
* Java 17
* Spring Boot 3
* Maven
* PostgreSQL 
* Tests: JUnit 5, Mockito, Spring Test

## Project structure
```text
src/
 ├─ main/
 │   ├─ java/com/example/flashcards/
 │   │   ├─ controller/       # REST controllers
 │   │   ├─ service/          # Business services
 │   │   ├─ model/            # Entities
 │   │   ├─ dto/              # DTO for API exchanges
 │   │   └─ repository/       # JPA interfaces
 │   └─ resources/
 │       └─ application.properties
 └─ test/
     ├─ controller/           # Unit tests for controllers
     ├─ service/              # Unit tests for services
     └─ integration/          # Integration tests
```

## Installation
#### 1. Clone the project
```sh
git clone https://gitlab.com/val7304/flashcards.git
cd flashcards
```

#### 2. Database configuration: 
- The app connects automatically to a local PostgreSQL instance via environment variables, or defaults to user postgres and password pswd.
- No manual edition of application.properties is required.
```sh
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:pswd}
```

If you prefer different credentials, export them before launching:
```sh
export DB_USER=myuser
export DB_PASSWORD=mypassword
```

#### 3. Create the database (first time only)
This script checks if flashcardsdb exists; if not, it creates it automatically.
```sh
./init-db.sh
```

#### 3. Build & Tests
```sh
./mvnw clean package
./mvnw test
```
#### 3.1 Run Checkstyle
```bash
./mvnw checkstyle:check
```
Goal: maintain clean, standardized, and CI/CD–ready Java code.
Checkstyle ensures:
- No unused imports
- Correct spacing and indentation
- Proper Javadoc comments
- Consistent naming and formatting

### Tests
- Unit services and controllers: ./mvn test or ./mvnw test
- REST API integration: tests with MockMvc and Spring Boot Test

### Example of a common error:
integration test:
Expected status 204 for a deletion but code 200 returned → adjust the controller or tests.

#### Objective:
- Facilitate integration into CI/CD (Jenkins, GitLab CI, etc.)
- Ensure code quality in future developments.

#### 4. Launch the application
```sh
./mvnw clean install
```
##### Default (Dev Profile)
```sh
./mvnw spring-boot:run
```
The dev profile is activated automatically (see application.properties):
```properties
spring.profiles.active=dev
```
#### ➡ On each run:
- The schema is recreated (spring.jpa.hibernate.ddl-auto=create-drop)
- data.sql reloads initial categories and flashcards

##### Production profile
```sh
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```
Data is preserved between runs.

### Access the application via browser or Postman or curl - your choice
```sh
http://localhost:8080/api/categories
http://localhost:8080/api/flashcards
```

### endpoints available:

| Type   | Endpoint                                 | Description                  |
| ------ | ---------------------------------------- | ---------------------------- |
| GET    | `/api/categories`                        | List all categories          |
| GET    | `/api/categories/search?name=Bash`       | Search category by name      |
| GET    | `/api/categories/{id}`                   | Get category by ID           |
| POST   | `/api/categories`                        | Add new category             |
| PUT    | `/api/categories/{id}`                   | Update category              |
| DELETE | `/api/categories/{id}`                   | Delete category              |
| GET    | `/api/flashcards`                        | List all flashcards          |
| GET    | `/api/flashcards/search?question=branch` | Search flashcards by keyword |
| GET    | `/api/flashcards/{id}`                   | Get flashcard by ID          |
| POST   | `/api/flashcards`                        | Add new flashcard            |
| PUT    | `/api/flashcards/{id}`                   | Update flashcard             |
| DELETE | `/api/flashcards/{id}`                   | Delete flashcard             |


### Data initialization
src/main/resources/data.sql contains:
- 5 categories
- 25 flashcards (5 per category)

```sh
INSERT INTO category (name) VALUES ('Bash one-liner');
INSERT INTO flashcard (question, answer, category_id)
VALUES ('Version of redhat used', 'cat /etc/redhat-release', 1);
```

### Example usage (via curl)

```sh
# Get all categories
curl -s http://localhost:8080/api/categories | jq

# Add a new flashcard
curl -X POST http://localhost:8080/api/flashcards \
     -H "Content-Type: application/json" \
     -d '{
           "question": "test ADD flashcard",
           "answer": "ADD test flashcard",
           "category": { "id": 1 }
         }'
```

#### Notes for Developers

* Dev profile: resets DB at each run, for isolated tests.
* Prod profile: keeps DB persistent (used for CI/CD or Docker).
* Checkstyle: all code is compliant; run it locally before commit.
* Tests: ./mvnw test must pass without error before merge.

### Access:
http://localhost:8080/api/categories

http://localhost:8080/api/flashcards