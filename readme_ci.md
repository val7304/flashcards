# Flashcards CI/CD Pipeline Documentation

This folder contains the files and scripts related to the CI/CD pipelines for the Flashcards project

## Flashcards CI/CD Status

#### Production Build & Release Pipeline

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml)

#### Docker Hub Registry 

[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards/tags)
[![Docker Pulls](https://img.shields.io/docker/pulls/valeriejeanne/flashcards)](https://hub.docker.com/r/valeriejeanne/flashcards)
[![Docker Image Size](https://img.shields.io/docker/image-size/valeriejeanne/flashcards/latest)](https://hub.docker.com/r/valeriejeanne/flashcards)

#### SonarQube Cloud Quality gate

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=coverage)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)

#### Continuous Integration (Branches) 
![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/ci-develop.yml/badge.svg?branch=develop)
[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml)

#### 'Actions Runs' Badges
![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)
![Build](https://img.shields.io/badge/Build-success-brightgreen)


> These badges display the current CI/CD status, Docker image information, and code quality results.

---

## Overview

The CI pipeline on `staging` automates:
- Build & packaging (Maven)
- Format code (Spotless)
- Static application security testing (CodeQL)
- Static code quality scanning (Checkstyle, SpotBugs)
- Unit & integration tests (JUnit 5)
- Coverage analysis (JaCoCo)
- Execute API test collections (Postman/Newman)
- Security scanning (Trivy)
- Artifact publishing (JaCoCo reports)

---

## Project structure

 ```text
FLASHCARDS/
 ├── .github/workflows/
 │             ├─ cd-prod.yml        # CD pipeline (main)
 │             └─ ci-[branch].yml    # CI pipeline (develop/staging)
 ├── ci-scripts
 │      ├─ build.sh
 │      ├─ docker-build.sh
 │      └─ test.sh
 ├── config/checkstyle/
 │   	       ├─ checkstyle-suppressions.xml
 │   		   └─ checkstyle.xml 
 ├── db/[profile]/                              # manual / CI staging & prod init
 │		  └─ init-data.sql                      
 ├── src/main/java/com/example/flashcards       # Application source code
 │		        ├─ static/                
 │		        └─ resources/                
 │		             ├─ db/dev/init-data.sql    # datas for dev                
 │		             ├─ static/                 # simple web API                
 │		             └─ application.properties, application-[profile].properties                 
 ├── src/test/java/com/example/flashcards/      # Unit & integration tests
 │		            ├─ config/                  # security config
 │		            │    └─ SecurityConfig.java # fix CodeQL issue Actuator(CI)
 │		            ├─ controller/                
 │		            ├─ dto/                
 │		            ├─ entity/                
 │		            ├─ integration/                   
 │		            ├─ mapper/               
 │		            ├─ service/               
 │		            └─ resources/                    
 │		                └─ application-test.properties
 ├── postman/
 │     └─ flashcards.postman_collection.json  
 ├── Dockerfile
 ├── pom.xml
 └── sonar-project.properties
```

#### The pipeline uses the project's dedicated Checkstyle configuration
These rules are enforced automatically during the CI pipeline stages.

#### A **Postman collection** is included and can be used manually or within CI

---

### Branch, Profile & Environments

| Branch    | Profil    | port       | Purpose                                 |
|-----------|-----------|------------|---------------------------------------- |
| develop   | dev       | 8080       | Continuous Integration (build + tests)  |
| staging   | staging   | 8081       | Integration, QA & API testing           |
| main      | prod      | 8080       | Production build, Docker & SonarCloud   |

Each branch automatically loads the matching profile in CI/CD

--- 

## CI Workflow

location: `.github/workflows/ci-staging.yml`

The following workflow is triggered on `push` and `pull requests` targeting the `staging` branch:

```text
.github/workflows/staging.yml
├─ Checkout & Maven cache           : Clone repository and restore Maven dependencies
├─ Static analysis                  : Checkstyle + SpotBugs + CodeQL
├─ PostgreSQL service               : Real PostgreSQL instance for integration tests
├─ Build & Tests                    : Unit tests (H2) + integration tests (PostgreSQL service)
├─ Coverage (JaCoCo)                : Generate XML and HTML coverage reports
├─ Security scan (Trivy filesystem) : Detect CVEs in project dependencies and filesystem
├─ Start Spring Boot (staging profile) : Launch on port 8081 for live API testing
├─ API tests with Newman           : Execute Postman collection against running application
├─ Upload artifacts                : Store Checkstyle, SpotBugs, logs, and coverage reports
└─ Workspace cleanup               : Final cleanup of temporary files
```
**Trivy** is used to detect vulnerabilities both in:
- project dependencies (filesystem scan)
- the final Docker image (image scan)

--- 

## Quality Gates Summary (all branches)

| Branch  | Build/Test | Checkstyle | SpotBugs | Coverage | Trivy | Newman | Docker | SonarCloud |
| ------- | ---------- | ---------- | -------- | -------- | ----- | ------ | ------ | ---------- |
| develop | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ❌     | ❌        |
| staging | ✔         | ✔          | ✔        | ✔       | ✔     | ✔     | ❌     | ❌        |
| main    | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ✔      | ✔         |

> **JaCoCo Coverage** reports are generated on all branches.

---

## SonarCloud Integration (only on `main`)
This project uses SonarCloud to analyze code quality

The ci/cd workflow:

- Generates JaCoCo XML coverage report
- Uploads it to the pipeline
- Runs SonarSource/sonarqube-scan-action
- SonarCloud reads sonar-project.properties
- Displays results (Bugs, Code Smells, Coverage, Duplications)
- SonarCloud is only triggered on the `main` branch because the free plan only supports this branch

--- 

## CI/CD Platforms

### GitHub Actions: Used for:
`Build & tests`  `Code quality` `Security scanning` `API testing` `Artifact generation` 

> The project structure and CI scripts are designed to be easily transferable in order to perform other scenarios on others CI/CD platforms

### Secrets management
Sensitive runtime credentials (password, Docker, SonarCloud tokens and actuator admin password) are injected using GitHub Secrets.

---

## Tests Instructions & Code Quality

### Integration tests run with:

- @SpringBootTest
- Real PostgreSQL service
- Web environment on port 8081 (CI)

---

## Run CI Locally (staging equivalent)

This project uses **Spotless** to enforce consistent code formatting. 

The CI pipeline runs `./mvnw spotless:check`

Before committing, or if formatting issues are detected, apply fixes locally (before the `clean verify` cmd) to ensure formatting is correct:

```sh
./mvnw spotless:apply
```

Full pipeline equivalent:
```sh
./mvnw clean verify  
```

Individual checks:
```sh
./mvnw spotless:apply
./mvnw checkstyle:check 
./mvnw spotbugs:check
./mvnw test
./mvnw jacoco:report   
```

#### Reports Generated: local(developer): 

| Report             | Location / Artifact               |
| ------------------ | --------------------------------- |
| Checkstyle         | `target/site/checkstyle.html`     |
| SpotBugs           | `target/site/spotbugs.html`       |
| Jacoco HTML + XML  | `target/site/jacoco/`             |
| Surefire           | `target/surefire-reports/`        |

---

## Run CI on Github Actions

### Static Application Security (CodeQL)

CodeQL is executed on the `staging` branch to detect potential security
vulnerabilities and unsafe coding patterns at source code level.

- Results are published in `GitHub Security` → `Code scanning alerts`

#### Reports Generated: in CI: 

| Branch            | Report             | Location / Artifact                       |
| ----------------- | ------------------ | ------------------------------------------------------------------ |
| develop           | JaCoCo HTML + XML  | `target/site/jacoco/` (XML uploaded as CI artifact)                |
| staging           | JaCoCo HTML + XML  | `target/site/jacoco/jacoco.xml` (HTML + XML uploaded as artifacts) |
| staging           | Application logs   | `spring.log` (uploaded as CI artifact)                             |
| main              | JaCoCo XML only    | `target/site/jacoco/jacoco.xml` (for SonarCloud)                   |

- **JaCoCo** XML is uploaded for SonarCloud usage (on `main` only)
- A **Trivy** report is available in the `GitHub Actions` logs under the job `Scan filesystem` with Trivy

---

## Notes for Staging CI  (Staging)

The `staging` branch runs **the complete QA pipeline**: **No Docker image and no SonarCloud scan are executed on this branch.**

**`staging` profile** execution (`staging` branch):
- Full API validation is performed using Newman
- All logs and reports are uploaded, even on failure
- The application runs on port 8081

### Test execution (`test` profile)
JUnit tests run using the dedicated `test` Spring profile (`src/test/resources/application-test.properties`)
- Runs in all environments
- In-memory H2 database
- Fast and deterministic execution
- No dependency on PostgreSQL

> Checkstyle and SpotBugs must both pass with 0 issues before commit

> **Run tests:** all unit/integration tests must succeed before merge Compatible with CI/CD tools (GitHub Actions, Jenkins, GitLab CI)

This ensures stable and reproducible CI test runs

---

### Production execution (`main` branch)
The `main` branch configuration is automatically used when running inside Docker.
It is never used during CI test phases (which use H2 database).

Designed for:
- Persistent data
- Docker-based deployments
- Production parity

#### Database initialization
- `spring.sql.init.mode=never`
- No schema or data mutation at startup
- Production data initialized explicitly via SQL scripts (`db/prod/`)

This guarantees safe redeployments and prevents accidental data loss.

---

### Image versioning & traceability
Each successful CI run produces three Docker tags:
- **Release tag** (e.g. `v0.87.0`) — semantic version
- **Short Git SHA** — immutable audit & rollback reference
- **latest** — most recent production build

All tags reference the same image digest.

---

### Quality gates
CI enforces strict validation:
- Spotless (formatting)
- Checkstyle
- SpotBugs
- JaCoCo coverage (all branches)
- SonarCloud Quality Gate (**main only**)

Any violation fails the pipeline.

---

### Smoke tests (container validation)
Before publishing to Docker Hub:
- Container is started in CI
- Spring Boot health check is executed
- API endpoint (`/api/categories`) is validated

Only runnable, production-ready images are published.

---

### CI/CD readiness
- All unit and integration tests must pass before merge
- Fully compatible with other CICD platforms
- CI scripts are centralized and reusable (`ci-scripts/`)

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
