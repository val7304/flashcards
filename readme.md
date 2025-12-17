# Flashcards Application 


[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/staging.yml) 
[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)

> **Note :**  
> Advanced pipelines are only executed on the `main` branch. 

> `staging` only executes: Build, Tests, Checkstyle, SpotBugs, Coverage, Postman/Newman.

![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen) ![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)

> See also the [readme_CI](https://github.com/val7304/flashcards/blob/staging/readme_ci.md) for more information about CI.

---

## Overview

**Flashcards** is a Java Spring Boot application designed to manage flashcards and their categories.  
It serves as a learning and demonstration project to practice **DevOps**, **clean code**, and **automation** concepts.

The `staging` branch provides a safe environment close to production for integration, functional and API testing.


### Features

- CRUD for categories and flashcards  
- Clean architecture with service + controller layers 
- PostgreSQL database 
- Automated CI for quality and integration validation

### Technologies 

| Layer    | Technology                        |
| -------- | --------------------------------- |
| Backend  | Java 17, Spring Boot 3            |
| Build    | Maven Wrapper (`./mvnw`)          |
| Database | PostgreSQL                        |
| Testing  | JUnit 5, Mockito, Postman (Newman CLI) |
| Quality  | Checkstyle, SpotBugs, JaCoCo      |
| Security | Trivy FS scan (detects CVEs in dependencies and filesystem) |
| CI       | GitHub Actions (staging pipeline) |


## Project structure

```text
src/
 ├─ main/
 │   ├─ java/com/example/flashcards/
 │   │   ├─ controller/       # REST controllers
 │   │   ├─ dto/              # DTO for API exchanges
 │   │   ├─ entity/           # JPA entities
 │   │   ├─ mapper/           # Mapper
 │   │   ├─ entity/           # JPA entities
 │   │   ├─ service/          # Service
 │   │   └─ repository/       # JPA interfaces
 │   └─ resources/
 │       └─ application.properties  # common configuration (no active profile)
 ├─ db/
 │   └─ staging/
 │       └─ init-data.sql    # manual / CI staging init
 └─ test/
     ├─ controller/           # Unit tests for controllers
     ├─ service/              # Unit tests for services
     ├─ integration/          # Integration tests
     └─ resources/
         └─ application-test.properties  # test profile
```

---

## Installation & Setup (Staging)

## Requirements
- Java 17+
- Maven Wrapper (included)
- PostgreSQL 16 
- Git

###  Clone the project

```sh
git clone -b staging https://github.com/val7304/flashcards.git
cd flashcards
```
---

### Database configuration: 

> The application requires a running local PostgreSQL instance when using the `staging` profile. 

> The application connects automatically to a local PostgreSQL instance via environments variables.

Default credentials (Spring configuration):

```
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:pswd}
```

To override:
```sh
export DB_USER=myuser
export DB_PASSWORD=mypassword
```

### Initialize the Database (Optional)

```sh
./init-db.sh
```

> This script is intended for local developers running PostgreSQL manually.

> It is not used in CI and never executed in production environments.

### Database initialization (staging / prod)

Staging and production databases are NOT initialized automatically by Spring Boot.

Initial data must be loaded manually using SQL scripts:

```sh
psql -h <host> -U postgres -d flashcardsdb -f db/staging/init-data.sql
```

---

### Run the application

#### Default (Staging Profile)

```sh
./mvnw clean install
./mvnw spring-boot:run -Dspring-boot.run.profiles=staging
```

or use:

```sh
./mvnw clean install

export SPRING_PROFILES_ACTIVE=staging
./mvnw spring-boot:run
```

#### Behavior:
- Database schema is updated, never dropped 
- No automatic data initialization. Initial data must be loaded manually or via CI scripts
- Tests run using the "test profile" with an embedded H2 database
- Application runs on port 8081
- Suitable for integration & functional API testing

---

### Access the Application

#### Web Interface

Once the application is running, open:

http://localhost:8081

This page provides: A simple UI to list, search, create, update and delete:
- Categories
- Flashcards

> Click-to-toggle or hide Flashcard answers
> Visibility of category ID for each Flashcard

> This UI is intentionally simple and framework-free (no React/Angular)

as the project focuses on backend, CI/CD, and DevOps practices.

**Notes on Frontend vs Backend responsibilities**

- The **backend** remains API-first
- The **frontend**:
    * Is served from `src/main/resources/static`
    * Uses fetch() to call REST endpoints

> Exists only to improve developer experience and project discoverability
> In a real-world scenario, this frontend could be: Replaced by a dedicated frontend application Or deployed separately (e.g., React + API gateway)

---

#### Base URLs:

```sh
http://localhost:8081/api/categories
http://localhost:8081/api/flashcards
```
---

### API Endpoints

| Method | Endpoint                                 | Description              |
| ------ | ---------------------------------------- | ------------------------ |
| GET    | `/api/categories`                        | List all categories      |
| GET    | `/api/categories/search?name=Bash`       | Search category by name  |
| GET    | `/api/categories/{id}`                   | Retrieve category by ID  |
| POST   | `/api/categories`                        | Create a new category    |
| PUT    | `/api/categories/{id}`                   | Update a category        |
| DELETE | `/api/categories/{id}`                   | Delete a category        |
| GET    | `/api/flashcards`                        | List flashcards          |
| GET    | `/api/flashcards/search?question=branch` | Search flashcards        |
| GET    | `/api/flashcards/{id}`                   | Retrieve flashcard by ID |
| POST   | `/api/flashcards`                        | Create flashcard         |
| PUT    | `/api/flashcards/{id}`                   | Update flashcard         |
| DELETE | `/api/flashcards/{id}`                   | Delete flashcard         |

---

### Data Initialization

`init-data.sql` loads:
- 5 categories
- 25 flashcards (5 per categories)

---

### Usage Scenario (via cURL)

#### 1. Get all categories
```sh 
curl -s http://localhost:8081/api/categories
```

#### 2. Create a new category
> returns: new ID (example: 6)
```sh
curl -X POST http://localhost:8081/api/categories \
     -H "Content-Type: application/json" \
     -d '{
           "name": "new category"
         }'
```

#### 3. Create a flashcard inside this new category
> returns: new flashcard ID (example: 25)
```sh
curl -X POST http://localhost:8081/api/flashcards \
     -H "Content-Type: application/json" \
     -d '{
           "question": "My question",
           "answer": "My answer",
           "categoryId": 6 }
         }'
```

#### 4. List all flashcards
```sh
curl -s http://localhost:8081/api/flashcards | jq 
```

#### 5. Update flashcard 25
```sh
curl -X PUT http://localhost:8081/api/flashcards/25 \
     -H "Content-Type: application/json" \
     -d '{
           "question": "My corrected question",
           "answer": "My corrected answer",
           "categoryId": 6
         }'
```

#### 6. Delete flashcard 25
```sh
curl -X DELETE http://localhost:8081/api/flashcards/25
```

#### 7. Delete category 6
```sh
curl -X DELETE http://localhost:8081/api/categories/6 
```

---

## Local Pre-Commit Validation

This project uses Spotless to enforce consistent code formatting.
Before committing, or if formatting issues are detected, apply fixes locally to ensure formatting is correct:

```sh
### Code formatting
./mvnw spotless:apply
```

> Spotless fails the build if formatting rules are violated.

> Run `spotless:apply` before any `clean test` or `clean verify` to avoid failures.

Before pushing:

```sh
./mvnw clean verify   # full validation
```
Ensure:

✔ All tests pass  
✔ Checkstyle = 0 errors   
✔ SpotBugs = 0 issues   
✔ Coverage OK   

> Ensures that the `staging` branch remains reliable for integration and API testing.

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
