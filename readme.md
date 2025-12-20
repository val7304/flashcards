# Flashcards Application


**Flashcards** is a Java Spring Boot application designed to manage flashcards and their categories.  
It serves as a learning and demonstration project showcasing **DevOps** practices, **clean code** principles, and **CI/CD automation**.

This repository follows a **three-branch strategy**, where each branch contains its own dedicated `application.properties` configuration.

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)
[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards/tags)
[![Docker Pulls](https://img.shields.io/docker/pulls/valeriejeanne/flashcards)](https://hub.docker.com/r/valeriejeanne/flashcards)
[![Docker Image Size](https://img.shields.io/docker/image-size/valeriejeanne/flashcards/latest)](https://hub.docker.com/r/valeriejeanne/flashcards)
- `main` for  production
- [See the full analysis on SonarCloud](https://sonarcloud.io/project/overview?id=val7304_flashcards)

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=coverage)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)

- [`develop`](https://github.com/val7304/flashcards/tree/develop) is dedicated to development.
- [`staging`](https://github.com/val7304/flashcards/tree/staging) provides a safe environment close to production for integration, functional and API testing.

![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/develop.yml/badge.svg?branch=develop)
[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/staging.yml)
![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)


> See also [readme_CI](https://github.com/val7304/flashcards/blob/main/readme_ci.md) for more explanations about CI.

---

### Features

- Full CRUD on Categories and Flashcards  
- Extensible REST API  
- Automatic sample data loading (`dev` branch only)
- Unit and integration tests using Spring Boot, JUnit 5, Mockito

---

### Technologies 

| Layer         | Technology                                         |
|---------------|----------------------------------------------------|
| Backend       | Java 17, Spring Boot 3                             |
| Build         | Maven Wrapper (`./mvnw`)                           |
| Database      | PostgreSQL (runtime), H2 (tests)                   |
| Testing       | JUnit 5, Mockito                                   |
| Code Quality  | Checkstyle, SpotBugs, CodeQl, JaCoCo, SonarCloud   |
| Security Scan | Trivy (filesystem scan + Docker image scan)        |
| Packaging     | Docker                                             |
| CI/CD         | GitHub Actions, Docker Hub                         |

---

## Branches & Data Management

This project follows a realistic database lifecycle strategy depending on the **active branch**.

| Branch     | Database type          | Schema strategy | Data initialization              |
|------------|------------------------|-----------------|----------------------------------|
| `develop`  | Local (ephemeral)      | `create-drop`   | `data.sql` executed automatically|
| `staging`  | Persistent (local VM)  | `update`        | No automatic data loading        |
| `main`     | Persistent (production)| `update`        | No automatic data loading        |

### Important
- In **production (`main`)**, the application **never modifies data automatically at startup**
- Initial production data must be inserted **manually or via CI/CD**

---

## Project structure

```
FLASHCARDS/
├── .github
│    └─ workflows
│	    └─ main.yaml  
├── ci-scripts
│    └─ build.sh
│    └─ docker-build.sh
│    └─ test.sh
├── config/checkstyle/
│   		└─ checkstyle-suppressions.xml
│   		└─ checkstyle.xml
├── src/
│	 ├─ main/
│	 │   ├─ java/com/example/flashcards/
│	 │   │   ├─ controller/     # REST controllers
│	 │   │   ├─ dto/            # DTO for API exchanges
│	 │   │   ├─ entity/         # JPA entities
│	 │   │   ├─ mapper/         # Mapper
│	 │   │   ├─ repository/     # JPA interfaces
│	 │   │   └─ service/        # Service
│	 │   └─ resources/
│	 │       └─ application.properties   # common configuration (no active profile)
│	 ├─ db/
│	 │   └─ prod/
│	 │     	 └─ init-data.sql            # manual / CI staging init
│	 └─ test/
│		 ├─ controller/  # Unit tests for controllers
│		 ├─ service/     # Unit tests for services
│		 ├─ integration/ # Integration tests
│		 └─ application-test.properties	  # test profile
│		 
├─── Dockerfile
├─── init-db.sh
├─── pom.xml
└─── sonar-project.properties
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

> The app connects automatically to a local PostgreSQL instance via environment variables.  

Default credentials are:
```
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:pswd}
```

To override:

```sh
export DB_USER=myuser
export DB_PASSWORD=mypassword
```

### Initialize the Database 

The `./init-db.sh` script checks for `flashcardsdb`, creating it if missing

> `init-db.sh` is required only for local development when PostgreSQL is not managed by Docker or CI.
> In CI/CD pipelines, PostgreSQL is provided using a GitHub Actions service container (postgres:16).  
> No manual initialization is required during CI.

## Production Data Initialization (main branch)

Production data is initialized manually using an idempotent SQL script

Script location: `db/prod/init-data.sql`

### Execution (one-time or controlled re-run)

```bash
psql -h <host> -U <user> -d flashcardsdb -f db/prod/init-data.sql
```

> **Notes**
> - Executed manually or via CI/CD, never by Spring Boot
> - Safe to re-run if the script uses idempotent inserts
  (INSERT ... WHERE NOT EXISTS or ON CONFLICT DO NOTHING)
> - Does NOT truncate or overwrite production data
> - Fully compatible with PostgreSQL 16

---

### Run the application

1. Build: 
```sh
./mvnw clean install
```
and
```sh
java -jar target/flashcards-0.0.1-SNAPSHOT.jar 
#or
./mvnw spring-boot:run
```

#### Behavior:
- Persistent data between runs (no automatic drop)
- Application runs on port: 8080
- Ideal for Docker and CI/CD environments

---

### Access the Application

Base URLs: `http://localhost:8080`   return →  "Flashcards API is running"
Healthpoint: `http://localhost:8080/actuator/health` return →  "status: "UP",groups: ["liveness","readiness"]}"

> **Note:**
> In the `develop` and `staging` branches, there is a minimal web user interface (HTML + JavaScript) for interacting with the API. 

### Access to the API

```sh
http://localhost:8080/api/categories
http://localhost:8080/api/flashcards
```

### Endpoints

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

- In the **develop branch**, `data.sql` automatically loads:
  - 5 categories
  - 25 flashcards

- In **staging and production**, data initialization is **disabled by design**
  Initial data must be inserted manually using the provided SQL script

---

### Usage Scenario (via cURL)

#### 1. Get all categories

```sh 
curl -s http://localhost:8080/api/categories
```

### Optional: JSON pretty-print

For a nicer output, you may install `jq` and run:

```bash
curl -s http://localhost:8080/api/categories | jq
```

#### 2. Create a new category
> returns: new ID (example: {"id":6,"name":"new category"})
```sh
curl -X POST http://localhost:8080/api/categories \
     -H "Content-Type: application/json" \
     -d '{
           "name": "new category"
         }'
```

#### 3. Create a flashcard inside this new category
> returns: new flashcard ID (example: {"id":25,"question":"My question","answer":"My answer","categoryId":6})
```sh
curl -X POST http://localhost:8080/api/flashcards \
  -H "Content-Type: application/json" \
  -d '{
    "question": "My question",
    "answer": "My answer",
    "categoryId": 6
  }'
```

#### 4. List all flashcards
```sh
curl -s http://localhost:8080/api/flashcards
```

#### 5. Update flashcard (example: {"id":25,"question":"My corrected question","answer":"My corrected answer","categoryId":4})
```sh
curl -X PUT http://localhost:8080/api/flashcards/25 \
  -H "Content-Type: application/json" \
  -d '{
    "question": "My corrected question",
    "answer": "My corrected answer",
    "categoryId": 4
  }'
```

#### 6. Delete flashcard 25
```sh
curl -X DELETE http://localhost:8080/api/flashcards/25
```

#### 7. Delete category 6
```sh
curl -X DELETE http://localhost:8080/api/categories/6
```

---

## Local Validation

Code formatting and quality checks are enforced using Spotless, Checkstyle, SpotBugs and JaCoCo.

Before pushing:
```sh
./mvnw spotless:apply
./mvnw clean verify
```
> If all commands pass successfully, the code is production-ready and can be safely committed and pushed.

All validations are also enforced in CI.
> See [readme_CI](https://github.com/val7304/flashcards/blob/main/readme_ci.md) for full CI details.
 
---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
