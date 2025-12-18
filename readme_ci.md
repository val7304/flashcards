# Flashcards CI Pipeline (Branch: `develop`)

This document describes the **CI pipeline dedicated to the `develop` branch**.  
It focuses entirely on **code quality**, **tests**, **coverage**, **static analysis**, and **security scanning**.  

**No Docker image is built or published on this branch.**  
**This pipeline mirrors what is commonly used in enterprise environments for feature/integration development.**

![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/develop.yml/badge.svg?branch=develop)
[![CI - Main](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)

> **Note**  
> Docker build/publish and SonarCloud analysis are executed **only on the `main` branch**.

![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)

---

## Overview

The CI pipeline on `develop` automates:
- Build & packaging (Maven)
- Format code (Spotless)
- Static application security testing (CodeQL)
- Static code quality scanning (Checkstyle, SpotBugs)
- Unit & integration tests (JUnit 5)
- Coverage analysis (JaCoCo)
- Security scanning (Trivy)
- Artifact publishing (JaCoCo reports)

It serves as the **quality gate** before merging into `staging` or `main`.

---
## Structure

```text
flashcards/
 ├─ .github/workflows/develop.yml       # develop CI pipeline
 ├─ src/main/java/                      # Application source code
 ├─ src/main/resources/
 │           ├─ application.properties  # common configuration (no active profile)
 │           ├─ data.sql
 │           └─ static/                 # minimal frontend (index.html, app.js)
 ├─ src/test/java/                      # Unit & integration tests
 ├─ src/test/resources/                 
 │   └─ application-test.properties     # profile running test on H2
 ├─ config/checkstyle/                  
 │   ├─ checkstyle.xml
 │   └─ checkstyle-suppressions.xml
 ├─ init_db.sh
 ├─ Dockerfile
 └─ pom.xml
```
#### The pipeline uses the project's dedicated Checkstyle configuration
These rules are enforced automatically during the CI pipeline stages

---

## CI Workflow

location: `.github/workflows/develop.yml`

The following workflow is triggered on `push` targeting the `develop` branch:

```text
.github/workflows/develop.yml
├─ Checkout & Maven cache                : Clone repo / restore dependencies
├─ Spotless check                        : Validate formatting with Spotless
├─ Static analysis                       : Checkstyle + SpotBugs + CodeQL
├─ Build & Tests                         : Unit tests (H2) + integration tests (PostgreSQL service)
├─ Coverage (JaCoCo)                     : Generate XML + HTML reports
├─ Security scan (Trivy filesystem)      : CVE detection on project directory
└─ Upload artifacts                      : Checkstyle, SpotBugs, logs, coverage
```

Tests run on H2 automatically using the `test` profile

---

## CI Platform

### GitHub Actions: 
The `develop` branch uses:
- Build & tests 
- Code quality
- Security scanning
- Artifact generation

The project structure and CI scripts are designed to be easily transferable in order to perform other scenarios on others CI/CD platforms

> No deployment or publishing occurs on this branch.

--- 

## Quality Gates Summary (Develop)

| Branch    | Build/Test | Checkstyle | SpotBugs | Coverage | Trivy | Newman | Docker | SonarCloud |
| --------- | ---------- | ---------- | -------- | -------- | ----- | ------ | ------ | ---------- |
| `develop` | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ❌    | ❌         |

> **JaCoCo Coverage** reports are generated on all branches.

⚠  **SonarCloud (free plan)** only provides a complete analysis for the `main` branch

---

### Run CI Locally (Develop equivalent)

This project uses Spotless to enforce consistent code formatting.

The CI pipeline runs: `./mvnw spotless:check`

Before committing, or 
- if formatting issues are detected, apply fixes locally (before the `clean verify` cmd) 
to ensure formatting is correct:

```sh
./mvnw spotless:apply
```

#### Full pipeline equivalent:

```sh
./mvnw clean verify 
```

#### Individual checks:

```sh
./mvnw spotless:apply
./mvnw test     
./mvnw clean   
./mvnw checkstyle:check
./mvnw spotbugs:check
./mvnw jacoco:report
```

#### Reports Generated: local(developer): 

| Report             | Location / Artifact               |
| ------------------ | --------------------------------- |
| Checkstyle         | `target/site/checkstyle.html/xml` |
| SpotBugs HTML      | `target/site/spotbugs.html`       |
| Jacoco HTML + XML  | `target/site/jacoco/`             |
| Surefire           | `target/surefire-reports/`        |

---

## Run CI on Github Actions

### Static Application Security (CodeQL)

CodeQL is executed on the `develop` branch to detect potential security
vulnerabilities and unsafe coding patterns at source code level.

> Results are published in GitHub : `Security → Code scanning alerts`

### Reports Generated: in CI: 

| Branch            | Report             | Location / Artifact                                                |
| ----------------- | ------------------ | ------------------------------------------------------------------ |
| `develop`         | JaCoCo HTML + XML  | `target/site/jacoco/` (XML uploaded as CI artifact)                |
| `staging`         | JaCoCo HTML + XML  | `target/site/jacoco/jacoco.xml` (HTML + XML uploaded as artifacts) |
| `staging`         | Application logs   | `spring.log` (uploaded as CI artifact)                             |
| `main`            | JaCoCo XML only    | `target/site/jacoco/jacoco.xml` (for SonarCloud)                   |

> JaCoCo XML is uploaded for SonarCloud usage (on `main` only).

A **Trivy** report is included on `actions/runs`  job name: `Scan filesystem with Trivy` 

where you will see the: `Library │ Vulnerability │ Severity │ Status │ Installed Version │ Fixed Version │`  

> The report highlights vulnerable dependencies detected from your `pom.xml` and filesystem

---

### Notes for Develop CI 

**`test` profile**: 
- JUnit tests run on H2 in test profile (default during Maven test phase).
- The file is located at: `\src\test\resources\application-test.properties` and 
  uses the standard configuration for an H2 database.
- This file was not mandatory, but is used to isolate the tests from the PostgreSQL service.

**`dev` profile**: 
- Recreates the schema on each startup (`create-drop`)
- Reloads demo data from `data.sql`

- Checkstyle and SpotBugs must both pass with 0 issues before commit.
- Run tests: all unit/integration tests must succeed before merge
- Compatible with CI/CD tools (GitHub Actions, Jenkins, GitLab CI)

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
