# Flashcards CI/CD Pipeline Documentation

This folder contains the files and scripts related to the CI/CD pipelines for the Flashcards project

## Flashcards CI/CD Status

#### Continuous Integration (Branches) 

![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/ci-develop.yml/badge.svg?branch=develop)
[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml)

#### Production Build & Release Pipeline

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml)

#### Docker Hub Registry 
[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards/tags)

#### SonarCloud Quality gate

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=coverage)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=val7304_flashcards&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)


---

## Overview


The CI pipeline enforces strict quality gates:

- Build & packaging (Maven)
- Format code (Spotless)
- Static application security testing (CodeQL)
- Static code quality scanning (Checkstyle, SpotBugs)
- Unit & integration tests (JUnit 5)
- Coverage analysis (JaCoCo)
- Execute API test collections (Postman/Newman)
- Execute load test (k6)
- Security scanning (Trivy)
- Artifact publishing (JaCoCo reports)
- SonarCloud Quality Gate (main only)

Any violation fails the pipeline immediately. It serves as the quality gate before merging

---

## Project structure

This structure highlights CI/CD-relevant files and directories. The full application structure is documented in the main README.

 ```text
FLASHCARDS/
├── .github/workflows/
│   ├── cd-prod.yml          # CD (main)
│   └── ci-[branch].yml      # CI (develop / staging)
├── ci-scripts/              # main only
│   ├── build.sh
│   ├── docker-build.sh
│   └── test.sh
├── config/
│   └── checkstyle/
│       ├── checkstyle.xml
│       └── checkstyle-suppressions.xml
├── db/
│   └── [profile]/init-data.sql
├── src/
│   ├── main/
│   │   ├── java/.../flashcards/   # application code
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-[profile].properties
│   │       └── db/dev/init-data.sql
│   └── test/
│       ├── java/.../flashcards/
│       │   ├── config/            # security (CodeQL fix)
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── entity/
│       │   ├── integration/
│       │   ├── mapper/
│       │   └── service/
│       └── resources/
│           └── application-test.properties
├── load-test/               # k6 (staging)
│   └── flashcards.js
├── postman/                 # Newman (staging)
│   ├── flashcards.postman_collection.json
│   ├── flashcards_error_cases.postman_collection.json
│   └── local.postman_environment.json
├── Dockerfile
├── init-db.sh
├── pom.xml
└── sonar-project.properties
```

#### The pipeline uses the project's dedicated Checkstyle configuration
These rules are enforced automatically during the CI pipeline stages.

#### A **Postman collection** is included and can be used manually or within CI

---

### Branch, Profile & Environments

| Branch    | Profil      | port       | Purpose                                 |
|-----------|-------------|------------|---------------------------------------- |
| develop   | `dev`       | 8080       | Continuous Integration (build + tests)  |
| staging   | `staging`   | 8081       | Integration, QA & API testing           |
| main      | `prod`      | 8080       | Production build, Docker & SonarCloud   |

Each branch automatically loads the matching profile in CI/CD

--- 

## CI Workflow

These workflows are triggered during `push` and `pull requests` targeting their own branch

### ci-develop

Location on `develop` branch: `.github/workflows/ci-develop.yml`

```text
.github/workflows/ci-develop.yml
├─ Checkout & Maven cache           : Clone repository and restore Maven dependencies
├─ Spotless check                   : Validate formatting with Spotless
├─ Static analysis                  : Checkstyle + SpotBugs + CodeQL
├─ PostgreSQL service               : Real PostgreSQL instance for integration tests
├─ Build & Tests                    : Unit tests (H2) + integration tests (PostgreSQL)
├─ Coverage (JaCoCo)                : Generate XML coverage reports
├─ Security scan (Trivy filesystem) : Detect CVEs in project dependencies and filesystem
└─ Upload artifacts                 : Store coverage report
```

### ci-staging

Location on `staging` branch: `.github/workflows/ci-staging.yml`
> It contains all the steps used in `ci-develop` +  

```text
.github/workflows/ci-staging.yml
├─ Start Spring Boot         : Launch on port 8081 for live API testing
├─ API tests with Newman     : Execute Postman collection against running application
├─ Load tests with k6        : Execute load test in K6
└─ Upload artifacts          : Store Newman report, Springboot logs, and coverage reports
```

## CI/CD Workflow

### cd-prod

Location on `main` branch: `.github/workflows/cd-prod.yml`
> It contains all the steps used in `ci-develop` +  

```text
.github/workflows/cd-prod.yml
├─ Build Docker image                    : Tagged with short SHA
├─ Security scan (Trivy Docker image)    : CVE detection on final container image
├─ Container smoke tests                 : Health check + API endpoint validation
├─ Release tagging                       : Git release tag (vX.Y.Z)
├─ Push Docker image to Docker Hub       : Version, short SHA and latest tags
├─ SonarCloud analysis                   : JaCoCo upload + Quality Gate
└─ Workspace cleanup                     : Final cleanup
```

**Trivy** is used to detect vulnerabilities both in:
- project dependencies (filesystem scan)
- the final Docker image (image scan)

--- 

## Quality Gates Summary (all branches)

| Branch  | Build/Test | Checkstyle | SpotBugs | Coverage | Trivy | Newman | k6     | Docker | SonarCloud |
| ------- | ---------- | ---------- | -------- | -------- | ----- | ------ | ------ | ------ | ---------- |
| develop | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ❌     |❌     | ❌        |
| staging | ✔         | ✔          | ✔        | ✔       | ✔     | ✔     | ✔      |❌     | ❌        |
| main    | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ❌     |✔      | ✔         |

> **JaCoCo Coverage** reports are generated on all branches.
> **SonarCloud** is only triggered on the `main` branch because the free plan only supports this branch

--- 

## CI/CD Platforms

### GitHub Actions: Used for:
`Build & tests`  `Code quality` `Security scanning` `API testing` `Artifact generation` 

> The project structure and CI scripts are designed to be easily transferable in order to perform other scenarios on others CI/CD platforms

### Secrets management
Sensitive runtime credentials (password, Docker, SonarCloud tokens and actuator admin password) are injected using GitHub Secrets.

---

## API Testing with Newman (CI)

The `staging` CI pipeline executes Postman API tests automatically using Newman

### Execution details

- The application is started with the `staging` profile 
- PostgreSQL is provided via a GitHub Actions service container
- Newman executes all API collections against http://localhost:8081
- Tests include both functional scenarios and error cases

### Failure policy

- Any failing API test fails the pipeline
- API tests act as a functional quality gate
- All reports are uploaded as CI artifacts, even on failure

### These reports are compatible with:

- CI test visualization
- Future integration with test dashboards

---

## Load Testing with k6 (CI)

The `staging` pipeline includes automated load testing using k6

### Purpose

k6 tests ensure that:

- The application remains stable under concurrent load
- Performance regressions are detected early
- Error rates remain under defined thresholds

### Execution strategy

- Executed after successful build and API tests
- Runs against a real running Spring Boot application
- Uses the same PostgreSQL instance as API tests
- Uses the staging configuration profile

### Threshold enforcement

The pipeline fails if:
- `http_req_failed` ≥ 1%
- `http_req_duration p(95)` ≥ 500 ms

k6 returns a non-zero exit code when thresholds are exceeded, which:
- Immediately fails the CI job
- Prevents promotion to further stages

---

## Run CI Locally 

The CI pipeline runs `./mvnw spotless:check`, this section mirrors the developer pre-commit validation described in the main README, but reflects the exact CI execution order.

```bash
./mvnw spotless:apply
./mvnw clean verify  
```

**Individual checks**:

```bash
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

CodeQL is executed on all branches to detect potential security
vulnerabilities and unsafe coding patterns at source code level.

- Results are published in `GitHub Security` → `Code scanning alerts`

#### Reports Generated: in CI: 

| Branch            | Report             | Location / Artifact                       |
| ----------------- | ------------------ | ------------------------------------------------------------------ |
| develop           | JaCoCo HTML + XML  | `target/site/jacoco/` (XML uploaded as CI artifact)                |
| staging           | JaCoCo HTML + XML  | `target/site/jacoco/jacoco.xml` (HTML + XML uploaded as artifacts) |
| staging           | Newman-reports     | `newman-reports.zip` (uploaded as CI artifact)                             |
| staging           | Application logs   | `spring.log` (uploaded as CI artifact)                             |
| main              | JaCoCo XML only    | `target/site/jacoco/jacoco.xml` (for SonarCloud)                   |


- **JaCoCo** XML is uploaded for SonarCloud usage (on `main` only)
- A **Trivy** report is available in the `GitHub Actions` logs under the job `Scan filesystem` with Trivy
- A **newman-reports** report artifact is available in the `GitHub Actions`, it contains 2 xml file: newman-functional.xml and newman-error.xml

---

## Notes for CI  

**`dev` profile** execution (`develop` branch):
- Recreates the schema on each startup (create-drop)
- Reloads demo data from db/dev/init-data.sql
- The application runs on port 8080

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

> Spotless, Checkstyle and SpotBugs must both pass with 0 issues before commit

> **Run tests:** all unit/integration tests must succeed before merge Compatible with CI/CD tools 

---

## Production execution (`main` branch)
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

All tags reference the same image digest

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
