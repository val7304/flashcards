# Flashcards CI/CD Pipeline Documentation

This document describes **all CI/CD pipelines**, quality gates, execution rules and branch-specific behaviors for the Flashcards project. It is aligned with the actual GitHub Actions workflows and Maven lifecycle configuration.

### Status

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml)
![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/ci-develop.yml/badge.svg?branch=develop)
[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml)
[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)

> Dependencies of Actions are continuously monitored by  ![Dependabot](https://img.shields.io/badge/dependabot-active-025E8C?logo=dependabot)

---

### Branch, Profile & Environments

| Branch  | Spring Profile | Port | Objective                                                  |
|---------|----------------|------|----------------------------------------------------------- |
| develop | dev / test     | 8080 | Fast CI feedback (formatting, static analysis, unit tests) |
| staging | staging / it   | 8081 | Integration, API validation, load testing                  |
| main    | prod           | 8080 | Production build, security, Docker release                 |

Each pipeline explicitly controls the active Spring profile and database configuration through environment variables.

--- 

## CI/CD Project Files

This structure highlights CI/CD-relevant files and directories. 
For the full application structure, see the main [ReadMe](./readme.md)

```text
FLASHCARDS/
├── .github/
│   ├── dependabot.yml           
│   └── workflows/
│       ├── cd-prod.yml                         # CI/CD (main)
│       ├── ci-develop.yml                      # CI (develop)
│       └── ci-staging.yml                      # CI (staging)
├── ci-scripts/                  
│       ├── build.sh
│       ├── docker-build.sh
│       └── test.sh
├── config/checkstyle/
│       ├── checkstyle.xml
│       └── checkstyle-suppressions.xml
├── load-test/                
│       └── flashcards.js
├── postman/                  
│       ├── flashcards.postman_collection.json
│       ├── flashcards_error_cases.postman_collection.json
│       └── local.postman_environment.json
├── src/
│   ├─ main/java/com/example/flashcards/        # source code app
│   └─ test/java/com/example/flashcards/        # ut/it tests
├── Dockerfile
└── pom.xml
``` 
---

## CI Workflows Overview

This project uses the `GitLab Flow` strategy:

- Features are developed in the `develop` branch
- Gradually promoted to `staging`, which acts as a stress test and continuous pre-production environment
- Once testing is complete, it is promoted to `main`

All workflows are triggered on **push** and **pull requests** targeting their respective branch

## ci-develop (Continuous Integration)

**Branch:** `develop`

### Purpose
Fast developer feedback with full static analysis and unit test validation.

### Execution

```text
- Checkout + Maven cache
- Spotless formatting check
- CodeQL (init + analyze)
- Maven clean verify
  ├─ Unit tests (H2)
  ├─ Checkstyle
  ├─ SpotBugs
  └─ JaCoCo coverage
- Trivy filesystem scan
- Upload JaCoCo reports
```

### Key Notes
- Checkstyle and SpotBugs are executed **via the Maven `verify` lifecycle**
- Database: **H2 in-memory**
- No Docker, no API, no load tests

---

## ci-staging (Integration & Validation)

**Branch:** `staging`

### Purpose
Validate real integrations and runtime behavior before production.

### Pipeline Stages

#### 1. Build, Static Analysis & Tests

```text
- PostgreSQL 16 service container
- Spotless formatting check
- CodeQL
- Unit tests (profile: test)
- Integration tests (profile: it, PostgreSQL Docker)
- Checkstyle & SpotBugs (via Maven verify)
- JaCoCo HTML + XML
- Trivy filesystem scan
```

#### 1a. Docker Runtime Smoke Validation (Staging Only)

```text
The application is packaged and started in a containerized environment during staging.
Docker image is built (flashcards:staging)
Container is started
Smoke test on /actuator/health
```

> This step only validates container startup and health endpoints.
Functional API testing is performed later in the pipeline (Stage 2)

#### 2. Functional API Tests

```text
- Start Spring Boot (profile: staging, port 8081)
- Newman API tests
  ├─ Functional scenarios
  └─ Error cases
- Upload JUnit reports and application logs
```

#### 3. Load Testing

```text
- Start Spring Boot (staging)
- k6 local execution
- Enforced performance thresholds
- Upload logs
```

### Load Test Configuration

| Parameter     | Value                    |
|---------------|--------------------------|
| Virtual users | 50                       |
| Duration      | 2 minutes                |
| Target        | /categories, /flashcards |

### Performance Gates

| Metric               | Threshold         |
|----------------------|------------------ |
| http_req_duration    | p(95) < 500 ms    |
| http_req_failed      | < 1%              |

The pipeline fails immediately if any threshold is exceeded

#### Sample results (local execution)

- p(95) http_req_duration :   21.01 ms   
- http_req_failed         :     0.00 %  
- requests                : ~183 req/s  
- iterations              :      5,529       
- checks success rate     :       100%        

---

## cd-prod (Production & Release)

**Branch:** `main`

### Purpose
Build, validate and publish production-ready Docker images.

### Trigger Rules

| Event        | Behavior                            |
|--------------|-------------------------------------|
| Push on main | Build + tests + security scans      |
| Tag vX.Y.Z   | Full release pipeline + Docker push |

---

### Pipeline Execution

```text
- PostgreSQL 16 service
- Spotless formatting check
- Unit tests + JaCoCo
- Integration tests (ONLY on release tags)
- SonarCloud analysis (push on main only)
- CodeQL
- Trivy filesystem scan
- Docker image build
- Trivy image scan (blocking)
- Container smoke tests
- Docker Hub publication (release tags only)
```

### Docker Image Strategy

The Docker image is rebuilt in the production pipeline (main branch)
> No Docker image produced in staging is reused in production

Rationale:

* Guarantees deterministic production builds
* Ensures security scans are executed on the final artifact
* Prevents relying on artifacts built in another branch

Even though staging validates container runtime behavior,
the production pipeline performs a fresh Docker build,
image scan (Trivy), smoke tests, and only then publishes the image.

This ensures full production traceability.

### Static Analysis Behavior on main

| Scenario     | Checkstyle | SpotBugs |
|--------------|------------|----------|
| Push on main | ❌         | ❌      |
| Tag vX.Y.Z   | ✔️         | ✔️      |

Static analysis is enforced as a **final production gate on release tags**.

---

## Smoke Tests (Docker Image Validation)

Before pushing images to Docker Hub:

- Container is started in CI
- Health check: `/actuator/health` (retry up to ~60s)
- API check: `/api/categories`

Only runnable images are published.

---

## Quality Gates Summary (all branches)

The CI pipeline enforces strict quality gates:

`Build & packaging` `Code formatting` `Static analysis` `Unit & integration tests` 

`Coverage analysis` `API tests` `Load tests` `Security scanning ` `SonarCloud Quality Gate` 

| Branch       | Formatting | Checkstyle | SpotBugs | Tests | Trivy | Newman | k6 | Docker | Sonar |
|--------------|------------|------------|----------|-------|-------|--------|----|--------|-------|
| develop      | ✔️        | ✔️         | ✔️       | ✔️   | ✔️   | ❌     | ❌ | ❌    | ❌    |
| staging      | ✔️        | ✔️         | ✔️       | ✔️   | ✔️   | ✔️     | ✔️ | ✔️    | ❌    |
| main (push)  | ✔️        | ❌         | ❌       | ✔️   | ✔️   | ❌     | ❌ | ✔️    | ✔️    |
| main (tag)   | ✔️        | ✔️         | ✔️       | ✔️   | ✔️   | ❌     | ❌ | ✔️    | ❌    |

> **JaCoCo Coverage** reports are generated on all branches

> **SonarCloud** is only triggered on the `main` branch (free plan)

--- 

## Required GitHub Secrets

| Secret | Usage |
|-------------------|---------------------------|
| POSTGRES_PASSWORD | PostgreSQL service        |
| SONAR_TOKEN       | SonarCloud authentication |
| SONAR_PROJECT_KEY | SonarCloud project        |
| DOCKERHUB_USERNAME| Docker registry           |
| DOCKERHUB_TOKEN   | Docker registry           |

---

## Dependabot Strategy

### Enabled features
- Security alerts
- Automatic security updates
- Grouped security pull requests
- Version updates for GitHub Actions only

### Scope
Dependabot is configured to:
- Update GitHub Actions workflows
- Group updates into a single weekly PR

### Validation workflow
<<<<<<< HEAD

Dependabot opens a PR (develop) → CI pipeline runs → merge → Smoke tests validate the Docker image (staging) → If green: merge allowed
=======
1. Dependabot opens a PR
2. CI pipeline runs
3. Smoke tests validate the Docker image (staging)
4. If green → merge allowed
>>>>>>> main

---

## Local CI-like Execution

The CI pipeline runs `./mvnw spotless:check`, this section mirrors the developer pre-commit validation described in the main README, 
but reflects the exact CI execution order

```bash
./mvnw spotless:apply
./mvnw clean verify  
```

**Individual checks:**

```bash
./mvnw -Dspring.profiles.active=it test 
./mvnw checkstyle:check 
./mvnw spotbugs:check
./mvnw jacoco:report   
```

#### Note for JaCoCo coverage 

- In dev, the coverage threshold is set at 0%
- In `staging` and `main` branches, the coverage threshold is set in workflows

To Simulate the coverage locally, use:  `./mvnw clean verify` and: 

|Branch          | CI command                       | using profile       |
|----------------|----------------------------------|-------------------- |
|`staging`       |`-Djacoco.minimum.coverage=0.70`  |`-Pcoverage-staging` |
|`main`          |`-Djacoco.minimum.coverage=0.80`  |`-Pcoverage-main`    |

#### Reports: local(developer): 

| Report             | Location / Artifact               |
| ------------------ | --------------------------------- |
| Checkstyle         | `target/site/checkstyle.html`     |
| SpotBugs           | `target/site/spotbugs.html`       |
| Jacoco HTML + XML  | `target/site/jacoco/`             |
| Surefire           | `target/surefire-reports/`        |

---

## Run CI on Github Actions

### Static Application Security (CodeQL)

CodeQL is executed on all branches to detect potential security
vulnerabilities and unsafe coding patterns at source code level.

- Results are published in `GitHub Security` → `Code scanning alerts`

### Reports Generated in CI: 

| Branch            | Report             | Location / Artifact                       |
| ----------------- | ------------------ | ------------------------------------------------------------------ |
| develop           | JaCoCo HTML + XML  | `target/site/jacoco/` (XML uploaded as CI artifact)                |
| staging           | JaCoCo HTML + XML  | `target/site/jacoco/jacoco.xml` (HTML + XML uploaded as artifacts) |
| ""                | Newman-reports     | `newman-reports.zip` (uploaded as CI artifact)                     |
| ""                | Application logs   | `spring.log` (uploaded as CI artifact)                             |
| ""                | app.jar            | `app.jar` uploaded as CI artifact and reused in smoke test         |
| main              | JaCoCo XML only    | `target/site/jacoco/jacoco.xml` (for SonarCloud)                   |

- **JaCoCo** XML is uploaded for SonarCloud usage (on `main` only)

- A **Trivy** report is available in the `GitHub Actions` logs under the job `Scan filesystem` with Trivy

- A **newman-reports** report artifact is available in the `GitHub Actions`, it contains 2 xml files: `newman-functional.xml` and `newman-error.xml`

### CI/CD readiness
- Clear separation between CI and CD
- Strong quality gates
- Docker images validated before publication
- CI scripts are centralized and reusable (`ci-scripts/`)

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
