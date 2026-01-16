# Flashcards CI/CD Pipeline Documentation

This document describes the CI/CD pipelines and quality gates for the Flashcards project.

### Status

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/cd-prod.yml)
![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/ci-develop.yml/badge.svg?branch=develop)
[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/ci-staging.yml)
[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=val7304_flashcards)

---

### Branch, Profile & Environments

| Branch  | Profile | Port | Purpose                                |
| ------- | ------- | ---- | -------------------------------------- |
| develop | dev     | 8080 | Continuous Integration (build + tests) |
| staging | staging | 8081 | Integration, QA & API testing          |
| main    | prod    | 8080 | Production build, Docker & SonarCloud  |

Each CI pipeline explicitly activates the appropriate Spring profile depending on the execution context (tests vs runtime)

--- 

## CI/CD Project Files

This structure highlights CI/CD-relevant files and directories. 
For the full application structure, see the main [ReadMe](./readme.md)


 ```text
FLASHCARDS/
├── .github/workflows/
│   ├── cd-prod.yml           # CI/CD (main)
│   ├── ci-develop.yml        # CI (develop)
│   └── ci-staging.yml        # CI (staging)
├── ci-scripts/               # main only
│   ├── build.sh
│   ├── docker-build.sh
│   └── test.sh
├── config/checkstyle/
│   ├── checkstyle.xml
│   └── checkstyle-suppressions.xml
├── load-test/                # k6 (staging)
│   └── flashcards.js
├── postman/                  # Newman (staging)
│   ├── flashcards.postman_collection.json
│   ├── flashcards_error_cases.postman_collection.json
│   └── local.postman_environment.json
├── Dockerfile
├── init-db.sh
├── sonar-project.properties
└── pom.xml
```

---

## CI Workflows

These workflows are triggered during `push` and `pull requests` targeting their own branch

### ci-develop

Location on `develop` branch, path: `.github/workflows/ci-develop.yml`

```text
.github/workflows/ci-develop.yml
├─ Checkout & Maven cache 
├─ Spotless check  
├─ Static analysis: Checkstyle, SpotBugs, CodeQL
├─ Tests (H2, unit + integration)
├─ Coverage (JaCoCo XML)
├─ Trivy filesystem scan
└─ Upload coverage artifacts
```

### ci-staging

Location on `staging` branch, path: `.github/workflows/ci-staging.yml`

> Includes all `ci-develop` step plus:  

```text
.github/workflows/ci-staging.yml
├─ Start Spring Boot on port 8081 (profile staging)
├─ API tests with Newman     
├─ Load tests with Grafana k6 (local)        
└─ Upload Newman reports, logs, coverage          
```
PostgreSQL service:
- Provided as a GitHub Actions service container
- Injected via environment variables
- Overrides H2 configuration from application-it.properties

### cd-prod

Location on `main` branch, path: `.github/workflows/cd-prod.yml`

> Includes all `ci-develop` step plus:  

```text
.github/workflows/cd-prod.yml
├─ PostgreSQL service
├─ Build Docker image (tagged with short SHA, semantic version, latest)
├─ Security scan (Trivy image scan) 
├─ Container Smoke tests (Spring Boot health check executed + API endpoint validated)
├─ Release tagging (e.g. v1.0.0)
├─ Push image to Docker Hub
├─ SonarCloud analysis  (JaCoCo XML)
└─ Workspace cleanup    (Optional)
```

--- 

### Testing Strategy (CI Focus)

This project applies a clear separation between unit tests and integration tests

#### Unit tests
- Scope: controller, service, mapper, DTO, entity
- Profile: test, DB: H2
- Execution: local + CI (all branches)

#### Integration tests
- Location: `src/test/java/.../integration`
- Execution local + develop:
    - Profile: `it`
    - Database: H2 (in-memory, defined in `application-it.properties`)
- Execution staging / main:
    - Profile: `it`
    - Database: PostgreSQL 16 (provided by CI service container and environment variables)

---

## Quality Gates Summary (all branches)

The CI pipeline enforces strict quality gates:

`Build & packaging` `Code formatting` `Static analysis` `Unit & integration tests` 

`Coverage analysis` `API tests` `Load tests` `Security scanning ` `SonarCloud Quality Gate` 

| Branch  | Build/Test | Checkstyle | SpotBugs | Coverage | Trivy | Newman | k6     | Docker | SonarCloud |
| ------- | ---------- | ---------- | -------- | -------- | ----- | ------ | ------ | ------ | ---------- |
| develop | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ❌     |❌     | ❌         |
| staging | ✔         | ✔          | ✔        | ✔       | ✔     | ✔     | ✔      |❌     | ❌         |
| main    | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ❌     |✔      | ✔          |


> **JaCoCo Coverage** reports are generated on all branches

> **SonarCloud** is only triggered on the `main` branch (free plan)

--- 

### API Testing with Newman (CI)

The `staging` CI pipeline executes Postman API tests automatically using Newman

- The application is started with the `staging` profile 
- PostgreSQL is provided via a GitHub Actions service container
- Newman executes all API collections against `http://localhost:8081`
- Tests include both functional scenarios and error cases
- All reports are uploaded as CI artifacts, even on failure. These reports are compatible with:
    - CI test visualization
    - Future integration with test dashboards

---

### Load Testing with k6 (Grafana)

The `staging` pipeline includes automated load testing using **k6** to
validate application stability and performance under concurrent load.

- Load tests are executed **locally via k6 OSS**
- Results are generated in JSON format
- Grafana Cloud is not used yet (no public endpoint available)
- The setup is fully compatible with future Grafana Cloud integration

#### Test scenario

- Target environment: `staging` - Application profile: `staging`
- Database: PostgreSQL 16 
- Virtual users: 50  - Duration: 2 minutes
- Test coverage: 
  - Create category, flashcard
  - List categories, flashcards

#### Performance thresholds

The pipeline enforces strict performance thresholds:

| Metric                | Threshold        |
|----------------------|------------------ |
| http_req_duration    | p(95) < 500 ms    |
| http_req_failed      | < 1%              |

The pipeline fails immediately if any threshold is exceeded

#### Sample results (local execution)

| p(95) http_req_duration   |     21.01 ms  | 
| http_req_failed           |       0.00 %  | 
| requests                  |   ~183 req/s  | 
| iterations                |   5,529       | 
| checks success rate       |   100%        | 

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
| staging           | Newman-reports     | `newman-reports.zip` (uploaded as CI artifact)                     |
| staging           | Application logs   | `spring.log` (uploaded as CI artifact)                             |
| main              | JaCoCo XML only    | `target/site/jacoco/jacoco.xml` (for SonarCloud)                   |

- **JaCoCo** XML is uploaded for SonarCloud usage (on `main` only)

- A **Trivy** report is available in the `GitHub Actions` logs under the job `Scan filesystem` with Trivy

- A **newman-reports** report artifact is available in the `GitHub Actions`, it contains 2 xml file: `newman-functional.xml` and `newman-error.xml`


### CI/CD readiness
- All unit and integration tests must pass before merge
- Fully compatible with other CICD platforms
- CI scripts are centralized and reusable (`ci-scripts/`)

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
