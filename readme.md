# Flashcards Application

![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/develop.yml/badge.svg?branch=develop)
[![CI - Main](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)

> **Note:**  
> Advanced pipelines are only executed on the `main` branch. 

> `develop` only executes: Build, Tests, Checkstyle, SpotBugs, Coverage.

![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)

> See also [readme_CI](https://github.com/val7304/flashcards/blob/develop/readme_ci.md)  for more explanations about CI.

---

## Overview

**Flashcards** is a Java Spring Boot application designed to manage flashcards and their categories.  
It serves as a learning and demonstration project to practice **DevOps**, **clean code**, and **automation** concepts.

The `develop` branch is dedicated to development.  

---

### Features

- Full CRUD on Categories and Flashcards  
- Extensible REST API  
- Automatic data loading via `data.sql`  
- Unit and integration tests using Spring Boot

---

### Technologies 

| Layer    | Technology                        |
| -------- | --------------------------------- |
| Backend  | Java 17, Spring Boot 3            |
| Build    | Maven Wrapper (`./mvnw`)          |
| Database | H2 / PostgreSQL                   |
| Testing  | JUnit 5, Mockito                  |
| Quality  | Checkstyle, SpotBugs, JaCoCo      |
| Security | Trivy FS scan (detects CVEs in dependencies and filesystem) |
| CI       | GitHub Actions (pipeline) |

---

## Project structure
```text
src/
 ├─ main/
 │   ├─ java/com/example/flashcards/
 │   │   ├─ controller/       # REST controllers
 │   │   ├─ service/          # Business services
 │   │   ├─ entity/           # Entities
 │   │   ├─ mapper/           # Mappers
 │   │   ├─ dto/              # DTO for API exchanges
 │   │   └─ repository/       # JPA interfaces
 │   └─ resources/
 │       └─ application.properties
 │       └─ application-dev.properties
 └─ test/java/com/example/flashcards/
     ├─ controller/           # Unit tests for controllers
     ├─ service/              # Unit tests for services
     ├─ integration/          # Integration tests
     └─ resources
        └─ application-test.properties
```

---

## Installation & Setup (develop)

## Requirements
- Java 17+
- Maven Wrapper (included)
- PostgreSQL 16 (optional for local `dev`)
- Git

### Clone the project

```sh
git clone -b develop https://github.com/val7304/flashcards.git
cd flashcards
```

### Database configuration: 
The application requires a running local PostgreSQL instance when using the `dev` profile.
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

### Initialize the Database (first time only)

* The script checks for `flashcardsdb`, creating it if missing.

```sh
./init-db.sh
```

> `init-db.sh` is required only for local development, when PostgreSQL run manually
> This script is not used by CI.


---

### Run the application

#### Default (Dev Profile)

```sh
./mvnw clean install
./mvnw spring-boot:run
```

#### Behavior:
- Schema recreated on startup  
- Create-drop → schema recreated and data reloaded from `data.sql`
- Port: 8080

---

### Access the Application
Base URLs:

```sh
http://localhost:8080/api/categories
http://localhost:8080/api/flashcards
```

### API Endpoints

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


### Data Initialization

`src/main/resources/data.sql` loads:
- 5 categories
- 25 flashcards (5 by categories)

---

### Usage Scenario (via cURL)

#### 1. Get all categories
```sh 
curl -s http://localhost:8080/api/categories | jq
```

#### 2. Create a new category
> returns: new ID (example: 6)
```sh
curl -X POST http://localhost:8080/api/categories \
     -H "Content-Type: application/json" \
     -d '{
           "name": "new category"
         }'
```

#### 3. Create a flashcard inside this new category
> returns: new flashcard ID (example: 26)
```sh
curl -X POST http://localhost:8080/api/flashcards \
     -H "Content-Type: application/json" \
     -d '{
           "question": "My question",
           "answer": "My answer",
           "category": { "id": 6 }
         }'
```

#### 4. List all flashcards
```sh
curl -s http://localhost:8080/api/flashcards | jq 
```

#### 5. Update flashcard 26
```sh
curl -X PUT http://localhost:8080/api/flashcards/26 \
     -H "Content-Type: application/json" \
     -d '{
           "question": "My corrected question",
           "answer": "My corrected answer",
           "category": { "id": 6 }
         }'
```

#### 6. Delete flashcard 26
```sh
curl -X DELETE http://localhost:8080/api/flashcards/26 
```

#### 7. Delete category 6
```sh
curl -X DELETE http://localhost:8080/api/categories/6 
```

---

### Developer Notes

**`test` profile**: 
- JUnit tests run on H2 in test profile (default during Maven test phase).
- The file is located at: `\src\test\resources\application-test.properties` and 
  uses the standard configuration for an H2 database.
- This file was not mandatory, but is used to isolate the tests from the PostgreSQL service.

**`dev` profile**: 
- `dev` profile is activated automatically, it is used only when running the application manually.
- The file is located at: `\src\main\resources\application.properties` and 
  use `spring.profiles.active=dev` as default profile

- Recreates the schema on each startup (`create-drop`)
- Reloads demo data from `data.sql`

- Checkstyle and SpotBugs must both pass with 0 issues before commit.
- Run tests: all unit/integration tests must succeed before merge
- Compatible with CI/CD tools (GitHub Actions, Jenkins, GitLab CI)

---
**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
