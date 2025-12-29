# Flashcards CI/CD Pipeline Documentation

This folder contains the files and scripts related to the CI/CD pipelines for the **Flashcards** project.

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

#### Continuous Integration (Branch) 
![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/ci-develop.yml/badge.svg?branch=develop)
![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml/badge.svg?branch=staging)

> **Note:**  
> Advanced pipelines are only executed on the `main` branch
> `develop` only executes: Build, Tests, Checkstyle, SpotBugs, CodeQl, JaCoCo Coverage
> `staging` executes: everything as the `develop` branch + Postman/Newman

#### 'Actions Runs' Badges
![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)
![Build](https://img.shields.io/badge/Build-success-brightgreen)

> These badges display the current CI/CD status, Docker image information, and code quality results.

---

## Overview

The CI pipeline enforces strict quality gates:

- Build & packaging (Maven)
- Format code (Spotless)
- Static application security testing (CodeQL)
- Static code quality scanning (Checkstyle, SpotBugs)
- Unit & integration tests (JUnit 5)
- Coverage analysis (JaCoCo)
- Security scanning (Trivy)
- Artifact publishing (JaCoCo reports)
- SonarCloud Quality Gate (main only)

Any violation fails the pipeline immediately.
It serves as the **quality gate** before merging into `staging` or `main`.

---

## CI/CD Structure

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

--- 

## Branch, Profile & Environments

| Branch    | profile   | port  | Purpose                                 |
|-----------|-----------|-------|---------------------------------------- |
| develop   | dev       | 8080  | Continuous Integration (build + tests)  |
| staging   | staging   | 8081  | Integration, QA & API testing           |
| main      | prod      | 8080  | Production build, Docker & SonarCloud   |

Each branch automatically loads the matching profile in CI/CD

----

## CI Workflow

location: `.github/workflows/cd-prod.yml`

The following workflow is triggered on `push` targeting the `main` branch:

```text
.github/workflows/cd-prod.yml
├─ Checkout & Maven cache                : Clone repository / restore dependencies
├─ Spotless check                        : Validate formatting with Spotless
├─ Static analysis                       : Checkstyle + SpotBugs + CodeQL
├─ Build & Tests                         : Unit + integration tests (JUnit 5)
├─ Coverage (JaCoCo)                     : Generate XML report for CI
├─ Security scan (Trivy filesystem)      : CVE detection on project dependencies
├─ Build Docker image                    : Tagged with short SHA
├─ Security scan (Trivy Docker image)    : CVE detection on final container image
├─ Container smoke tests                 : Health check + API endpoint validation
├─ Release tagging                       : Git release tag (vX.Y.Z)
├─ Push Docker image to Docker Hub       : Version, short SHA and latest tags
├─ SonarCloud analysis                   : JaCoCo upload + Quality Gate
└─ Workspace cleanup                     : Final cleanup    *optional
```

Trivy is used to detect vulnerabilities both in:
- project dependencies (filesystem scan)
- the final Docker image (image scan)

---

## Quality Gates Summary (all branches)

| Branch  | Build/Test | Checkstyle | SpotBugs | CodeQL   | Coverage | Trivy | Newman | Docker | SonarCloud |
| ------- | ---------- | ---------- | ---------| -------- |--------- | ----- | ------ | ------ | ---------- |
| develop | ✔         | ✔          | ✔        | ✔       | ✔        | ✔    |  ❌    | ❌     | ❌        |
| staging | ✔         | ✔          | ✔        | ✔       | ✔        | ✔    |  ✔     | ❌     | ❌        |
| main    | ✔         | ✔          | ✔        | ✔       | ✔        | ✔    |  ❌    | ✔      | ✔         |

> **JaCoCo Coverage** reports are generated on all branches

---

## SonarCloud Integration

This project uses SonarCloud to analyze code quality 

The workflow:
1. Generates JaCoCo XML coverage report
2. Uploads it to the pipeline
3. Runs `SonarSource/sonarqube-scan-action`
4. SonarCloud reads `sonar-project.properties`
5. Displays results (Bugs, Code Smells, Coverage, Duplications)

SonarCloud is only triggered on the `main` branch because the free plan only supports this branch

[See the full analysis on SonarCloud](https://sonarcloud.io/project/overview?id=val7304_flashcards)

---

## CI/CD Platforms

### GitHub Actions: 
- **GitHub Actions**  — Automated build and Docker publishing  
- **Docker Hub**      — Stores ready-to-deploy images  
- **ci-scripts/**     — Standardized shell scripts reusable across CI platforms  
- **SonarQube Cloud** — Display Quality Gates and global view on QA  

> The project structure and CI scripts are designed to be easily transferable in order to perform other scenarios on others CI/CD platforms

### Secrets management
Sensitive runtime credentials (password, Docker, SonarCloud tokens and actuator admin password) are injected using GitHub Secrets.

---

## Tests Instructions & Code Quality

#### Run locally:

This project uses Spotless to enforce consistent code formatting.

The CI pipeline runs `./mvnw spotless:check`

Before committing, 
or if formatting issues are detected, apply fixes locally (before the clean verify cmd) to ensure formatting is correct:

```sh
./mvnw spotless:apply
```

Full pipeline equivalent:

```sh
./mvnw clean verify
```

#### Individual checks:

```sh
./mvnw spotless:apply
./mvnw test
./mvnw checkstyle:check
./mvnw spotbugs:check
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

CodeQL is executed on the all branches to detect potential security
vulnerabilities and unsafe coding patterns at source code level.

> Results are published in GitHub Security → Code scanning alerts

### Reports Generated: in CI: 

| Branch            | Report             | Location / Artifact                                                |
| ----------------- | ------------------ | ------------------------------------------------------------------ |
| develop           | JaCoCo XML         | `target/site/jacoco/` (XML uploaded as CI artifact)                |
| staging           | JaCoCo HTML + XML  | `target/site/jacoco/jacoco.xml` (HTML + XML uploaded as artifacts) |
|   ""              | Application logs   | `spring.log` (uploaded as CI artifact)                             |
| main              | JaCoCo XML only    | `target/site/jacoco/jacoco.xml` (for SonarCloud)                   |

> JaCoCo XML is uploaded for SonarCloud usage
> A **Trivy** report is available in the GitHub Actions logs under the job `Scan filesystem with Trivy` 

---

## Notes for CI

**`dev` profile**  execution (`develop` branch): 
- Recreates the schema on each startup (`create-drop`)
- Reloads demo data from `db/dev/init-data.sql`
- The application runs on port 8080

**`staging` profile**  execution (`staging` branch): 
- This branch is used for integration tests  
- Full API validation is performed using Newman
- All logs and reports are uploaded, even on failure
- The application runs on port 8081

### Test execution (`test` profile)
JUnit tests run using the dedicated `test` Spring profile
(`src/test/resources/application-test.properties`).

- Runs in all environments
- In-memory H2 database
- Fast and deterministic execution
- No dependency on PostgreSQL

> Checkstyle and SpotBugs must both pass with 0 issues before commit.
> Run tests: all unit/integration tests must succeed before merge
> Compatible with CI/CD tools (GitHub Actions, Jenkins, GitLab CI)

This ensures stable and reproducible CI test runs.

---

### Production execution (`main` branch/`prod` profile)
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
- CodeQL
- JaCoCo coverage (all branches)
- SonarCloud Quality Gate (**main only**)

Any violation fails the pipeline.

---

### Smoke tests (container validation)

Before publishing to Docker Hub:
- Container is started in CI
- Spring Boot health check (`/actuator/health`) is executed
- API endpoint (`/api/categories`) is validated

This ensures that only runnable, production-ready images are published.

---

### CI/CD readiness
- All unit and integration tests must pass before merge
- Fully compatible with other CI/CD platforms
- CI scripts are centralized and reusable (`ci-scripts/`)

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
