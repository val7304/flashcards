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
 │   │   ├─ service/          # Business services
 │   │   ├─ model/            # Entities
 │   │   ├─ dto/              # DTO for API exchanges
 │   │   └─ repository/       # JPA interfaces
 │   └─ resources/
 │       └─ application.properties
 │       └─ application-staging.properties
 └─ test/
     ├─ controller/           # Unit tests for controllers
     ├─ service/              # Unit tests for services
     └─ integration/          # Integration tests
```

At the root, you will find also: 
- A postman collection: 
```text
postman/
 └─ flashcards.postman_collection.json   # Postman collection used for CI (Newman tests)
```
- The Checkstyle config :
```text
config/checkstyle/
 └─ checkstyle-suppressions.xml
 └─ checkstyle.xml
```
---

## Installation & Setup (Staging)
###  1. Clone the project

```sh
git clone -b staging https://github.com/val7304/flashcards.git
cd flashcards
```
---

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
---

### 3. Initialize the Database (Optional)

```sh
./init-db.sh
```

---

### 4. Run the application

#### 4.1 Default (Staging Profile)

```sh
./mvnw clean install
./mvnw spring-boot:run
```

The active profile is defined in `application.properties`:

```application.properties
spring.profiles.active=staging
```

#### Behavior in staging profile:
- Database schema is updated, never dropped 
- `data.sql` executed only on first startup
- Application runs on port 8081
- Suitable for integration & functional API testing

---

### 5. Access the API (staging)

```sh
http://localhost:8081/api/categories
http://localhost:8081/api/flashcards
```
---

### 6. API Endpoints

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

### 7. Data Initialization

`data.sql` loads:
- 5 categories
- 25 flashcards (5 per category)

---

### 8. Example Usage (via cURL)

```sh
# Get all categories
curl -s http://localhost:8081/api/categories | jq

# Add a new flashcard
curl -X POST http://localhost:8081/api/flashcards \
  -H "Content-Type: application/json" \
  -d '{"question": "test to add a new card", "answer": "added or not", "category": { "id": 1 }}'
```
---

### 10. Developer Notes (Staging)

> This branch is used for integration tests  
> It is not intended for production

The `staging` branch runs **the complete QA pipeline**:

| Phase                        | Status |
| ---------------------------- | ------ |
| Build                        | ✔     |
| Checkstyle                   | ✔     |
| SpotBugs                     | ✔     |
| Integration tests PostgreSQL | ✔     |
| Unit tests                   | ✔     |
| JaCoCo coverage              | ✔     |
| Trivy filesystem scan        | ✔     |
| Newman API tests             | ✔     |
| Clean workspace              | ✔     |

**No Docker image and no SonarCloud scan are executed on this branch.**

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
