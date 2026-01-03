# Flashcards Application 

Flashcards is a Java Spring Boot application designed to manage flashcards and their categories.
It serves as a learning and demonstration project showcasing **DevOps practices**, **clean code** principles, and **CI/CD automation**

This repository follows a three-branch strategy. Each branch activates a different runtime configuration aligned with its environment (`dev`, `staging`, `prod`)

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

> See also the [readme_CI](https://github.com/val7304/flashcards/blob/staging/readme_ci.md) for more information about CI.

---

## Features

- Full CRUD on Categories and Flashcards
- Extensible REST API
- Automatic sample data loading (`dev` branch only)
- Unit and integration tests using Spring Boot, JUnit 5, Mockito

---

## Technologies 

| Layer    | Technology                                        |  
| -------- | ------------------------------------------------- |
| Backend  | Java 17, Spring Boot 3                            |
| Build    | Maven Wrapper (`./mvnw`)                          |
| Database | PostgreSQL (runtime), H2 (tests)                  |
| Testing  | JUnit 5, Mockito, Postman (Newman CLI)            |
| Quality  | Checkstyle, SpotBugs, CodeQL, JaCoCo, SonarCloud  |
| Security | Trivy (filesystem scan + Docker image scan)       |
| CI       | GitHub Actions (develop/staging pipeline)         |
| CI/CD    | GitHub Actions, Docker Hub(cd-prod pipeline)      |

---

## Branches, Profiles & Data Management

This project follows a realistic database lifecycle strategy depending on the active branch.

| Branch    |  Profile	| Database type              | Schema strategy	| Data initialization                    |
|-----------|-----------| -------------------------- | ----------------	| ---------------------------------------|
|`develop`  |`dev`      | Local (non-persistent)     | create-drop   	| `init-data.sql` executed automatically |
|`staging`  |`staging`  | Persistent (local VM)      | update   	    | No automatic data loading              |
|`main`     |`prod`     | Persistent (production)    | update   	    | No automatic data loading              |

### Important 

- In `staging` and `production` (main), the application never modifies data automatically at startup
- Initial production data must be inserted manually or via CI/CD
- See the [data-initialization](#data-initialization-develop-branch)  section

---

## Project structure

```text
FLASHCARDS/
├── src/
│	 ├─ main/java/com/example/flashcards/            # application source code
│	 │   │      ├─ config/                           # security config
│	 │   │      ├─ controller/                       # REST controllers
│	 │   │      ├─ dto/                              # DTO for API exchanges
│	 │   │      ├─ entity/                           # JPA entities
│	 │   │      ├─ mapper/                           # Mapper
│	 │   │      ├─ repository/                       # JPA interfaces
│	 │   │      ├─ service/                          # Service
│	 │   │      └─ FlashcardsApplication.java                             
│	 │   ├─ resources/
│	 │   │   ├─ db/dev/                             # auto-loads of datas (dev)
│	 │   │   │      └─ init-data.sql                # datas for dev
│	 │   │   ├─ application.properties              # common configuration
│	 │   │   └─ application-[profile].properties    # configuration profile (3)
│	 │   ├─ static/
│	 │   │   └─ index.html, app.js, styles.css      # simple static API webpage
│	 ├─ db/
│	 │   └─ [profile]/                   
│	 │     	 └─ init-data.sql                  # manual data entry (staging/prod) 
│	 ├─ load-test/                             # k6 script (load testing - only staging)                   
│	 ├─ postman/                               # Execute API test collections (only staging)                   
│	 └─ test/java/com/example/flashcards/
│		    ├─ controller/                     # Unit tests for controllers
│		    ├─ dto/                            # Unit tests for dto
│		    ├─ entity/                         # Unit tests for entities
│		    ├─ integration/                    # Integration tests
│		    ├─ mapper/                         # Unit tests for mapper
│		    ├─ service/                        # Unit tests for mapper
│		    └─ resources/                    
│		           └─ application-test.properties	  # test profile
└─── pom.xml
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

> The application connects automatically to a local PostgreSQL instance via environments variables

Default credentials (Spring configuration):

```
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:pswd}
```

To override:
```bash
export DB_USER=myuser
export DB_PASSWORD=mypassword
```
---

## Initialize the Database

The `./init-db.sh` script checks for `flashcardsdb`, creating it if missing

> `init-db.sh` is required only for local development when PostgreSQL is not managed by Docker or CI

> In CI/CD pipelines, PostgreSQL is provided using a GitHub Actions service container (postgres:16)

> No manual initialization is required during CI

---

## Data Initialization (`develop` branch)
In the develop branch, the sql script automatically loads: 5 categories and 25 flashcards (5 per categories)

Script location: `src/main/resources/db/dev/init-data.sql`

This allows:
- Immediate API testing
- Immediate usage of the Web UI after startup

---

## Data Initialization (`staging` & `main` branches)

In their environment, data is initialized manually using an idempotent SQL script

- `staging` script location: `db/staging/init-data.sql`
- `main` script location: `db/prod/init-data.sql`

### Execution (one-time or controlled re-run)

```bash
psql -h [host] -U postgres -d flashcardsdb -f db/[PROFILE]/init-data.sql
```

> **Notes**
> - Executed manually or via CI/CD, never by Spring Boot
> - Safe to re-run if the script uses idempotent inserts (INSERT ... WHERE NOT EXISTS or ON CONFLICT DO NOTHING)
> - Does NOT truncate or overwrite production data
> - Fully compatible with PostgreSQL 16
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
```
Or, run the packaged JAR:
```bash
java -jar target/flashcards-0.0.1-SNAPSHOT.jar  --spring.profiles.active=[profile]
```

If you wish to test restricted access to the actuator, refer to the [actuator-security](#actuator-security) section

---

## Access the Application

| Profile | Port |
| ------- | ---- |
| dev     | 8080 |
| staging | 8081 |
| prod    | 8080 |

> use the default port for the active profile

### Web Interface

Once the application is running, open: http://localhost:8080

This page provides: A simple UI to list, search, create, update and delete:
- Categories
- Flashcards

Click-to-toggle or hide Flashcard answers

Visibility of category ID for each Flashcard

> This UI is intentionally simple and framework-free (no React/Angular) as the project focuses on backend, CI/CD, and DevOps practices.

---

**Notes on Frontend vs Backend responsibilities**

- The **backend** remains API-first
- The **frontend**:
    * Is served from `src/main/resources/static`
    * Uses fetch() to call REST endpoints
    * Exists only to improve developer experience and project discoverability

In a real-world scenario, this frontend could be: 

Replaced by a dedicated frontend application Or deployed separately (e.g., React + API gateway)

---
## Actuator Security

Spring Boot Actuator endpoints are secured to prevent unauthorized access to sensitive runtime information

### Exposure rules
- `/actuator/health` 
    * Available in all branches
    * Public
    * Used for health checks and container readiness
- `/actuator/info` 
    * available in: `develop` - `staging`
    * Public
    * Exposes build and application metadata
- All other `/actuator/**` endpoints 
    * available in: `develop` - `staging`
    * Protected
    * Require authentication with the `ADMIN` role

### Authentication
Actuator endpoints are protected using HTTP Basic authentication

The administrator credentials are provided via environment variables

| Variable               | 	Description                                           |
|------------------------|--------------------------------------------------------|
|`SPRING_SECURITY_PSWD`  | Admin password for Actuator access                     |
	
A non-sensitive default password is used only for local development convenience

### Local development example

```bash
SPRING_SECURITY_PSWD=your-secret ./mvnw spring-boot:run
```

> The actual password value must never be committed or documented

> In CI and production environments, the variable is injected securely

---

## Access to the API

> use the default port for the active profile

### API Base Urls

```bash
http://localhost:8080/api/categories
http://localhost:8080/api/flashcards
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

## Usage Scenario (via cURL)

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
           "categoryId": 6 }
         }'
```

### 4. List all flashcards
```bash
curl -s http://localhost:8080/api/flashcards | jq 
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
See also the [readme_CI](https://github.com/val7304/flashcards/blob/staging/readme_ci.md) for full CI details

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)

