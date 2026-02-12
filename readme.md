# Flashcards Application 

Flashcards is a Java Spring Boot application designed to manage flashcards and their categories.
It serves as a learning and demonstration project showcasing **DevOps practices**, **clean code** principles, and end-to-end **CI/CD automation**

This repository follows a three-branch strategy. 
Each branch represents a fully isolated environment with its own Spring profile, database lifecycle, and CI behavior

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml)
[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards/tags)
[![Docker Pulls](https://img.shields.io/docker/pulls/valeriejeanne/flashcards)](https://hub.docker.com/r/valeriejeanne/flashcards)
[![Docker Image Size](https://img.shields.io/docker/image-size/valeriejeanne/flashcards/latest)](https://hub.docker.com/r/valeriejeanne/flashcards)

- `main` for production
- [See the full analysis on SonarCloud](https://sonarcloud.io/project/overview?id=val7304_flashcards) 

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=coverage)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)


- [`develop`](https://github.com/val7304/flashcards/blob/develop) is dedicated to development
- [`staging`](https://github.com/val7304/flashcards/tree/staging)  provides a safe environment close to production for integration, functional and API testing

  [![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml) 
  ![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/ci-develop.yml/badge.svg?branch=develop)
  ![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen) ![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)


> Dependencies are continuously monitored by  ![Dependabot](https://img.shields.io/badge/dependabot-active-025E8C?logo=dependabot)

> See [readme_ci.md](./readme_ci.md) for detailed CI/CD pipeline documentation.

---

## Features

- Full CRUD on Categories and Flashcards
- Extensible REST API
- Automatic sample data loading (`develop` branch only)
- Unit and integration tests with Spring Boot, JUnit 5, Mockito
    - Unit tests use `test` profile
    - Integration tests use `it` profile

---

## Technologies

| Layer    | Technology                                                             |
|----------|------------------------------------------------------------------------|
| Backend  | Java 17, Spring Boot 3                                                 |
| Build    | Maven Wrapper (`./mvnw`)                                               |
| Database | PostgreSQL 16 (app, integration tests), H2 (unit tests)                |
| Testing  | JUnit 5, Mockito, Postman (Newman CLI), Grafana k6 (load tests)        |
| Quality  | Checkstyle, SpotBugs, CodeQL, JaCoCo, SonarCloud                       |
| Security | Trivy (filesystem scan + Docker image scan)                            |
| CI       | GitHub Actions (develop/staging pipelines)                             |
| CI/CD    | GitHub Actions + Docker Hub (production pipeline)                      |

---

## Branches, Profiles & Data

| Branch    | Profile  | Database type            | Schema strategy | Data initialization                    |
|-----------|----------|--------------------------|-----------------|--------------------------------------- |
| `develop` | `dev`    | Local (non-persistent)   | create-drop     | `init-data.sql` executed automatically |
| `staging` | `staging`| Persistent (local VM)    | update          | No automatic data loading              |
| `main`    | `prod`   | Persistent (production)  | update          | No automatic data loading              |

- In `staging` and `main`, the application never modifies data automatically at startup.
- Initial production data must be inserted manually or via CI/CD.
- See the [data-initialization](#data-initialization-develop-branch)  section

### Testing Overview

The project distinguishes clearly between unit tests and integration tests.
- Detailed CI execution is documented in [readme_CI](./readme_ci.md#testing-strategy)

---

## Project Structure (App)

```text
FLASHCARDS/
├── src/
│   ├─ main/java/com/example/flashcards/
│   │   ├─ config/          # security config
│   │   ├─ controller/      # REST controllers
│   │   ├─ dto/             # DTOs for API exchanges
│   │   ├─ entity/          # JPA entities
│   │   ├─ mapper/          # mappers
│   │   ├─ repository/      # JPA repositories
│   │   ├─ service/         # services
│   │   └─ FlashcardsApplication.java
│   ├─ resources/
│   │   ├─ db/dev/          # auto-loaded dev data
│   │   │   └─ init-data.sql
│   │   ├─ application.properties
│   │   └─ application-[profile].properties
│   ├─ static/
│   │   └─ index.html, app.js, styles.css
│   ├─ db/
│   │   └─ [profile]/init-data.sql  # manual data for staging/prod
│   └─ test/java/com/example/flashcards/
│       ├─ controller/
│       ├─ dto/
│       ├─ entity/
│       ├─ integration/
│       ├─ mapper/
│       ├─ service/
│       └─ resources/
│           ├─ application-it.properties    # it profile
│           └─ application-test.properties  # test profile 
└── pom.xml
```
**profile:** `dev`/`staging`/`prod`

Tests reach [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=coverage)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards) 

---

## Installation & Setup

### Requirements
- Java 17+
- Maven Wrapper (included)
- PostgreSQL 16 
- Git

### Clone the project

```bash
git clone https://github.com/val7304/flashcards.git
cd flashcards
```
---

### Database configuration: 

> The application connects to a local PostgreSQL instance via environment variables:

Default credentials (Spring configuration):

```text
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:pswd}
```

Override if needed:
```bash
export DB_USER=myuser
export DB_PASSWORD=mypassword
```
---

## Initialize the Database

The `./init-db.sh` script checks for `flashcardsdb`, creating it if missing

> Required only for local development when PostgreSQL is not managed by Docker or CI

> In CI/CD, PostgreSQL is provided via a GitHub Actions service container (staging, main). 
> - Unit tests use H2 in-memory
> - Integration tests use PostgreSQL in CI (staging)

## Data Initialization

- **develop**: Location: `src/main/resources/db/dev/init-data.sql` (5 categories, 25 flashcards) auto-loaded
- **staging**: Location: `db/staging/init-data.sql` (manual or CI/CD)
- **main**   : Location: `db/prod/init-data.sql` (manual or CI/CD)

### Run scripts on staging / prod:

```bash
psql -h [host] -U postgres -d flashcardsdb -f db/[profile]/init-data.sql
```
> Scripts are idempotent-safe and do not truncate production data

---

## Run the application

Build: 
```bash
./mvnw clean install
```

& Run (default: dev)
```bash
./mvnw spring-boot:run
```

Or, run with a specific profile:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=[profile]
# or
java -jar target/flashcards-1.0.0.jar --spring.profiles.active=[profile]
```

If you wish to test restricted access to the actuator, refer to the [actuator-security](#actuator-security) section

---

## Actuator Security

Spring Boot Actuator endpoints are secured to prevent unauthorized access to sensitive runtime information

### Exposure rules
- `/actuator/health` exposed on all branches, public
- `/actuator/info`   exposed on all branches, public
- All other `/actuator/**` endpoints 
    * exposed only on `develop` - `staging`, require ADMIN via HTTP Basic auth
    
### Authentication
Admin password is provided via SPRING_SECURITY_PSWD (never committed)
Example:

```bash
SPRING_SECURITY_PSWD=your-secret ./mvnw spring-boot:run
```

---

## Access the Application

| Profile | Port |
| ------- | ---- |
| dev     | 8080 |
| staging | 8081 |
| prod    | 8080 |

### Web Interface

Simple UI to list, search, create, update, and delete categories and flashcards: http://localhost:8080

> use the default port for the active profile

> This UI is intentionally framework-free (no React/Angular).

### API Base Urls

```bash
http://localhost:8080/api/categories
http://localhost:8080/api/flashcards
```

### Main endpoints:

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

## Example Usage (Scenario cURL)

> Use the default port for the active profile

### 1. Get all categories
```bash 
curl -s http://localhost:8080/api/categories
```

### 2. Create a new category
> returns: new ID (example: 6)
```bash
curl -X POST http://localhost:8080/api/categories \
     -H "Content-Type: application/json" \
     -d '{
           "name": "new category"
         }'
```

### 3. Create a flashcard inside this new category
> returns: new flashcard ID (example: 25)
```bash
curl -X POST http://localhost:8080/api/flashcards \
     -H "Content-Type: application/json" \
     -d '{
           "question": "My question",
           "answer": "My answer",
           "categoryId": 6
         }'
```

### 4. List all flashcards
```bash
curl -s http://localhost:8080/api/flashcards
```

### 5. Update flashcard 25
```bash
curl -X PUT http://localhost:8080/api/flashcards/25 \
     -H "Content-Type: application/json" \
     -d '{
           "question": "My corrected question",
           "answer": "My corrected answer",
           "categoryId": 6
         }'
```

### 6. Delete flashcard 25
```bash
curl -X DELETE http://localhost:8080/api/flashcards/25
```

### 7. Delete category 6
```bash
curl -X DELETE http://localhost:8080/api/categories/6 
```

---

## Local Pre-Commit Validation

This project uses **Spotless** to enforce consistent code formatting.
Before committing, or if formatting issues are detected, apply fixes locally to ensure formatting is correct:

```bash
./mvnw spotless:apply
./mvnw clean verify  
```

> If all commands pass successfully, the code is production-ready and can be safely committed and pushed

All validations are also enforced in CI

---

#### Dependency Management

This project uses GitHub Dependabot to:

- Monitor vulnerabilities (Dependabot Alerts)
- Automatically propose security fixes
- Automatically update GitHub Actions dependencies (grouped weekly)

All updates are validated through CI and staging smoke tests before production.

See also the [readme_CI](./readme_ci.md) for full CI details

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)

