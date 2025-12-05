# Flashcards Application


**Flashcards** is a Java Spring Boot application designed to manage flashcards and their categories.  
It serves as a learning and demonstration project to practice **DevOps**, **clean code**, and **automation** concepts.

On GitHub, this project has three branches and each branch loads its own default profile. 

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)
[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards/tags)

- main for  production
- [develop](https://github.com/val7304/flashcards/tree/develop) is dedicated to development.
- [staging](https://github.com/val7304/flashcards/tree/staging) provides a safe environment close to production for integration, functional and API testing.

![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/develop.yml/badge.svg?branch=develop)
[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/staging.yml)

![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)


> See also [readme_CI.md](readme_CI.md) for more explanations about CI.

---

### Features

- Full CRUD on Categories and Flashcards  
- Extensible REST API  
- Automatic data loading via `data.sql`  
- Unit and integration tests using Spring Boot, JUnit 5, Mockito

---

### Technologies 

| Layer         | Technology                                         |
|---------------|----------------------------------------------------|
| Backend       | Java 17, Spring Boot 3                             |
| Build         | Maven Wrapper (`./mvnw`)                           |
| Database      | PostgreSQL                                         |
| Testing       | JUnit 5, Mockito                                   |
| Code Quality  | Checkstyle, SpotBugs, JaCoCo, SonarCloud           |
| Security Scan | Trivy (filesystem scan + Docker image scan)        |
| Packaging     | Docker                                             |
| CI/CD         | GitHub Actions, Docker Hub                         |

---

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
 │       └─ application-prod.properties
 │       └─ data.sql
 └─ test/
     ├─ controller/           # Unit tests for controllers
     ├─ service/              # Unit tests for services
     └─ integration/          # Integration tests
```
---

## Installation & Setup

### Clone the project

```sh
git clone https://github.com/val7304/flashcards.git
cd flashcards
```

--- 

### Database configuration: 

> The app connects automatically to a local PostgreSQL instance via environments variables.  

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

--- 

### Initialize the Database (first time only)
This script checks for `flashcardsdb`, creating it if missing.

```sh
./init-db.sh
```
> `init-db.sh` is required only for local development, when PostgreSQL run manually.

> In CI/CD pipelines, PostgreSQL is provided using a GitHub Actions service container (postgres:16).  
> No manual initialization is required during CI.

---

### Run the application

#### Production profile

> Used for live deployments. 

> Data and schema preserved across restarts.

```sh
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

#### Behavior:
- Persistent data between runs (no automatic drop)
- Application runs on port: 8080
- Ideal for Docker and CI/CD environments

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


---

### Data Initialization

the `data.sql` file loads:
- 5 categories
- 25 flashcards (5 per categories)

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
## Local Pre-Commit Validation
To ensure the same quality gates as CI/CD, before pushing changes:

```sh
./mvnw clean verify   # full validation
```

This validates that:

✔ All tests pass
✔ Checkstyle = 0 errors
✔ SpotBugs = 0 issues
✔ Coverage OK

> If all commands pass successfully, your code is production-ready and can be safely committed and pushed.

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
