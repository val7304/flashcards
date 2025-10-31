# Flashcards Application

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)
[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards/tags)
![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)

---

## Overview

**Flashcards** is a Java Spring Boot application designed to manage flashcards and their categories.  
It serves as a learning and demonstration project to practice **DevOps**, **clean code**, and **automation** concepts.

### Features

- Full CRUD on Categories and Flashcards  
- Extensible REST API  
- Automatic data loading via `data.sql`  
- Unit and integration tests using Spring Boo

### Technologies 

| Layer         | Technology                       |
|---------------|----------------------------------|
| Backend       | Java 17, Spring Boot 3           |
| Build         | Maven Wrapper (`./mvnw`)         |
| Database      | PostgreSQL                       |
| Testing       | JUnit 5, Mockito                 |
| Code Quality  | Checkstyle, SpotBugs             |
| Packaging     | Docker                           |
| CI/CD         | GitHub Actions, Docker Hub       |

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
---

## Installation & Setup

###  1. Clone the project

```sh
git clone https://github.com/val7304/flashcards.git
cd flashcards
```

### 2. Database configuration: 
The app connects automatically to a local PostgreSQL instance via environments variables.  

Default credentials are:
```sh
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:pswd}
```

To override:
```sh
export DB_USER=myuser
export DB_PASSWORD=mypassword
```

### 3. Initialize the Database (first time only)
This script checks for `flashcardsdb`, creating it if missing.

```sh
./init-db.sh
```

---

If you are interested to run some tests about Code Quality & Static Analysis, go to https://github.com/val7304/flashcards/blob/main/readme_ci.md for more explanations.
 
- Build and Run Unit Tests 
- Checkstyle
- SpotBugs

---

### 4. Run the application

#### 4.1 Default (Development Profile)

```sh
./mvnw clean install
./mvnw spring-boot:run
```
The dev profile is activated automatically (see application.properties):

```properties
spring.profiles.active=dev
```

#### Behavior:
- Schema recreated on startup  
- Data reloaded from `data.sql`

---
#### 4.2 Production profile

```sh
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

#### Behavior:
- Persistent data between runs (no automatic drop)
- Ideal for Docker and CI/CD environments

---

### 5. Access the Application
Base URLs:

```sh
http://localhost:8080/api/categories
http://localhost:8080/api/flashcards

```

### 6. API Endpoints

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


---

### 7. Data Initialization

src/main/resources/data.sql loads initial demo content automatically:
- 5 categories
- 25 flashcards (5 each)

---

### 8. Example Usage (via cURL)

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
---

### 9. Developer Notes

- **Dev profile**: resets the DB each run (for isolated tests)
- **Prod profile**: persistent data 
- **Code quality**: Checkstyle and SpotBugs must both pass with 0 issues before commit
- **Tests**: All tests must succeed before merge or release  
- **CI/CD-ready**: Compatible with CI/CD tools (GitHub Actions, Jenkins, GitLab CI)

---

**Build:** SUCCESS  
**Code Quality:** 0 violations  0 Checkstyle violations, 0 SpotBugs warnings 
**Validation:** All tests passed  

---
**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
