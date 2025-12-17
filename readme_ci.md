# Flashcards CI Pipeline Documentation (Branch: `staging`)

The `staging` CI pipeline is dedicated to **code quality**, **testing**, **coverage**, **static analysis**, and **security scanning**.  
It replicates a realistic enterprise-level integration pipeline.

[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/staging.yml)
[![CI/CD - Main](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)

> **Note**  
> Docker build/publish and SonarCloud analysis are executed, **only on the `main` branch**.

![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)

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
flashcards/
 ├─ .github/workflows/staging.yml      # Staging CI pipeline
 ├─ src/main/java/                     # Application source code
 ├─ src/test/java/                     # Unit & integration tests
 ├─ src/test/resources/
 │   └─ application-test.properties    # profile used to run tests
 ├─ src/main/resources/
 │   └─ application.properties         # common configuration (no active profile)
 ├─ config/checkstyle/                  
 │   ├─ checkstyle.xml
 │   └─ checkstyle-suppressions.xml
 ├─ postman/
 │   └─ flashcards.postman_collection.json
 ├─ db/
 │   └─ staging/
 │       └─ init-data.sql              # manual / CI staging init
 ├─ Dockerfile
 └─ pom.xml
```
#### The pipeline uses the project's dedicated Checkstyle configuration
These rules are enforced automatically during the CI pipeline stages.

A **Postman collection** is included and can be used manually or within CI:
```sh
newman run postman/flashcards.postman_collection.json
```

---

## CI Workflow

location: `.github/workflows/staging.yml`

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
--- 

## CI/CD Platforms

### GitHub Actions: 
Used for:
- Build & tests 
- Code quality
- Security scanning
- API testing 
- Artifact generation

The project structure and CI scripts are designed to be easily portable to:
- Jenkins
- GitLab CI 
- Azure DevOps

--- 

## Quality Gates Summary (Staging)

| Branch  | Build/Test | Checkstyle | SpotBugs | Coverage | Trivy | Newman | Docker | SonarCloud |
| ------- | ---------- | ---------- | -------- | -------- | ----- | ------ | ------ | ---------- |
| staging | ✔         | ✔          | ✔        | ✔       | ✔     | ✔     | ❌    | ❌         |


> **JaCoCo Coverage** reports are generated on all branches.

---

### Integration tests run with:

- @SpringBootTest
- Real PostgreSQL service
- Web environment on port 8081 (CI)
- Application started with profile: staging

---

## Run CI Locally (staging equivalent)

This project uses Spotless to enforce consistent code formatting. 

The CI pipeline runs:

```sh
./mvnw spotless:check
```

Before committing, or if formatting issues are detected, apply fixes locally (before the ```clean verify``` cmd) to ensure formatting is correct:

```sh
./mvnw spotless:apply
```

Full pipeline equivalent:
```sh
./mvnw clean verify     # includes unit + integration tests, checkstyle, spotbugs and JaCoCo
```

Individual checks:
```sh
./mvnw spotless:apply
./mvnw checkstyle:check 
./mvnw spotbugs:check
./mvnw test
./mvnw clean   # clean target folder
./mvnw jacoco:report   
```
---

## Run CI on Github Actions

### Static Application Security (CodeQL)

CodeQL is executed on the `staging` branch to detect potential security
vulnerabilities and unsafe coding patterns at source code level.

> Results are published in GitHub Security → Code scanning alerts.

#### Reports Generated: local(developer): 

| Report             | Location / Artifact               |
| ------------------ | --------------------------------- |
| Checkstyle HTML    | `target/site/checkstyle.html`     |
| SpotBugs HTML      | `target/site/spotbugs.html`       |
| Jacoco HTML + XML  | `target/site/jacoco/`             |
| Surefire           | `target/surefire-reports/`        |

#### Reports Generated: in CI: 

| Branch            | Report             | Location / Artifact                       |
| ----------------- | ------------------ | ------------------------------------------------------------------ |
| develop           | JaCoCo HTML + XML  | `target/site/jacoco/` (XML uploaded as CI artifact)                |
| staging           | JaCoCo HTML + XML  | `target/site/jacoco/jacoco.xml` (HTML + XML uploaded as artifacts) |
| staging           | Application logs   | `spring.log` (uploaded as CI artifact)                             |
| main              | JaCoCo XML only    | `target/site/jacoco/jacoco.xml` (for SonarCloud)                   |

> JaCoCo XML is uploaded for SonarCloud usage (on `main` only).

A **Trivy** report is included on `actions/runs`  job name: `Scan filesystem with Trivy` 

where you will see the: `Library │ Vulnerability │ Severity │ Status │ Installed Version │ Fixed Version │ `  

> The report highlights vulnerable dependencies detected from your `pom.xml` and filesystem

---

### Notes for Staging CI  (Staging)

> This branch is used for integration tests  
> It is not intended for production

The `staging` branch runs **the complete QA pipeline**:

| Phase                        | Status |
| ---------------------------- | ------ |
| Build                        | ✔     |
| Spotless formatting check    | ✔     |
| Checkstyle                   | ✔     |
| SpotBugs                     | ✔     |
| CodeQL                       | ✔     |
| Integration tests PostgreSQL | ✔     |
| Unit tests                   | ✔     |
| JaCoCo coverage              | ✔     |
| Trivy filesystem scan        | ✔     |
| Newman API tests             | ✔     |
| Clean workspace              | ✔     |


**No Docker image and no SonarCloud scan are executed on this branch.**

- No Docker image is built
- No SonarCloud analysis (only on `main`)
- The application runs on port 8081
- Full API validation is performed using Newman
- All logs and reports are uploaded, even on failure

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
