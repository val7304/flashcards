# Flashcards Application 

Flashcards is a Java Spring Boot application designed to manage flashcards and their categories.
It serves as a learning and demonstration project showcasing DevOps practices, clean code principles, and CI/CD automation.

This repository follows a three-branch strategy. Each branch activates a different runtime configuration aligned with its environment (`dev`, `staging`, `prod`)

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml)
[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards/tags)
[![Docker Pulls](https://img.shields.io/docker/pulls/valeriejeanne/flashcards)](https://hub.docker.com/r/valeriejeanne/flashcards)
[![Docker Image Size](https://img.shields.io/docker/image-size/valeriejeanne/flashcards/latest)](https://hub.docker.com/r/valeriejeanne/flashcards)

- `main` for production
[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=coverage)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)


- [develop](https://github.com/val7304/flashcards/blob/develop) is dedicated to development
- [staging](https://github.com/val7304/flashcards/tree/staging)  provides a safe environment close to production for integration, functional and API testing

  [![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml) 
  ![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/ci-develop.yml/badge.svg?branch=develop)
  ![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen) ![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)

> See also the [readme_CI](https://github.com/val7304/flashcards/blob/staging/readme_ci.md) for more information about CI.

---

## Overview

**Flashcards** is a Java Spring Boot application designed to manage flashcards and their categories.  
It serves as a learning and demonstration project to practice **DevOps**, **clean code**, and **automation** concepts.

### Features

- CRUD for categories and flashcards  
- Clean architecture with service + controller layers 
- PostgreSQL database 
- Automated CI for quality and integration validation

### Technologies 

| Layer    | Technology                                        |
| -------- | ------------------------------------------------- |
| Backend  | Java 17, Spring Boot 3                            |
| Build    | Maven Wrapper (`./mvnw`)                          |
| Database | PostgreSQL (runtime), H2 (tests)                  |
| Testing  | JUnit 5, Mockito, Postman (Newman CLI)            |
| Quality  | Checkstyle, SpotBugs, CodeQL, JaCoCo, SonarCloud  |
| Security | Trivy (filesystem scan + Docker image scan)       |
| CI       | GitHub Actions (staging pipeline)                 |


## Project structure

```text
FLASHCARDS/
├── src/
│	 ├─ main/                                   
│	 │   ├─ java/com/example/flashcards/             # application source code
│	 │   │   ├─ controller/                          # REST controllers
│	 │   │   ├─ dto/                                 # DTO for API exchanges
│	 │   │   ├─ entity/                              # JPA entities
│	 │   │   ├─ mapper/                              # Mapper
│	 │   │   ├─ repository/                          # JPA interfaces
│	 │   │   ├─ service/                             # Service
│	 │   │   └─ FlashcardsApplication.java                             
│	 │   ├─ resources/
│	 │   │   ├─ db/dev/                             # auto in dev
│	 │   │   │      └─ init-data.sql                # datas for dev
│	 │   │   ├─ application.properties              # common configuration
│	 │   │   └─ application-[PROFILE].properties    # configuration profiles
│	 │   ├─ static/
│	 │   │   └─ index.html, app.js, styles.css      # simple static API webpage
│	 ├─ db/
│	 │   └─ [PROFILE]/                   
│	 │     	 └─ init-data.sql                  # manual in staging/prod init
│	 └─ test/java/com/example/flashcards/
│		    ├─ controller/                     # Unit tests for controllers
│		    ├─ dto/                            # Unit tests for dto
│		    ├─ entity/                         # Unit tests for entities
│		    ├─ integration/                    # Integration tests
│		    ├─ mapper/                         # Unit tests for mapper
│		    ├─ service/                        # Unit tests for service
│		    └─ resources/                    
│		           └─ application-test.properties	  # test profile
└─── pom.xml
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
---

### Initialize the Database (Optional)

The `./init-db.sh` script checks for flashcardsdb, creating it if missing

> This script is intended for local developers running PostgreSQL manually.
> No manual initialization is required during CI

---

### Data Initialization (staging/main only)

Staging and production databases are NOT initialized automatically by Spring Boot.
In their environment, data is initialized manually using an idempotent SQL script

Script location: `db/staging/init-data.sql` 

```sh
#psql -h <host> -U postgres -d flashcardsdb -f db/staging/init-data.sql
psql -h localhost -U postgres -d flashcardsdb -f db/staging/init-data.sql
```
** password: `pswd`

> **Notes**
> - Executed manually or via CI/CD, never by Spring Boot
> - Safe to re-run if the script uses idempotent inserts (INSERT ... WHERE NOT EXISTS or ON CONFLICT DO NOTHING)
> - Does NOT truncate or overwrite production data
> - Fully compatible with PostgreSQL 16
---

### Run the application

Build: 
```sh
./mvnw clean install
```
and: 
```sh
./mvnw spring-boot:run -Dspring-boot.run.profiles=staging
```
or use:
```sh
export SPRING_PROFILES_ACTIVE=staging
./mvnw spring-boot:run
```

#### Runtime behavior by branch and profile

Each Git branch enforces a specific Spring profile through configuration and CI.
Runtime behavior (port, database lifecycle, actuator exposure) is therefore deterministic.

`main` **Spring profile:** prod
- Persistent data between runs (no automatic schema drop)
- Application port: 8080
- Designed for Docker and CI/CD usage
- Actuator exposure: health only

`staging` **Spring profile:** staging
- Persistent data between runs
- Application port: 8081
- Actuator exposure: health/info

`develop` **Spring profile:** dev
- Database recreated on each startup
- Application port: 8080
- Actuator exposure: health/info

---

### Access the Application

#### Web Interface

Once the application is running, open: http://localhost:8081

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
### Actuator endpoints

#### Health endpoint
`http://localhost:8081/actuator/health`  Returns the application health status.
Details are intentionally restricted (UP only) to avoid exposing sensitive information.

#### Info endpoint

`http://localhost:8081/actuator/info`   aggregates:
* build metadata generated by spring-boot-maven-plugin
* environment-specific properties prefixed with info.*

> Exposure is profile-dependent and controlled via:

> `management.info.*.enabled `
> `management.endpoints.web.exposure.include`

---

### Access to the API

```sh
http://localhost:8081/api/categories
http://localhost:8081/api/flashcards
```

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

`init-data.sql` inserts:
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
