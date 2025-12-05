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
 ├─ src/test/java/                      # Unit & integration tests
 ├─ src/test/resources/                 # test profile
 │   └─ application-test.properties     # running test on H2
 ├─ src/main/java/                      # Application source code
 ├─ src/main/resources/
 │   ├─ application.properties
 │   └─ application-dev.properties
 │   └─ data.sql
 ├─ config/checkstyle/                  
 │   ├─ checkstyle.xml
 │   └─ checkstyle-suppressions.xml
 ├─ Dockerfile
 └─ pom.xml
```
#### The pipeline uses the project's dedicated Checkstyle configuration
These rules are enforced automatically during the CI pipeline stages.

---

## CI Workflow

location: `.github/workflows/develop.yml`

The following workflow is triggered on `push` targeting the `develop` branch:


```text
.github/workflows/develop.yml
├─ Checkout & Maven cache                : Clone repo / restore dependencies
├─ Static analysis                       : Checkstyle + SpotBugs
├─ Build & Tests                         : Unit + integration tests
├─ Coverage (JaCoCo)                     : Generate XML + HTML reports
├─ Security scan (Trivy filesystem)      : CVE detection on project directory
├─ Upload artifacts                      : Checkstyle, SpotBugs, logs, coverage
└─ Workspace cleanup                     : Final cleanup
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

The project structure and CI scripts are designed to be easily portable to:
- Jenkins
- GitLab CI 
- Azure DevOps

No deployment or publishing occurs on this branch.

- **SonarCloud (Free Plan)** 

SonarCloud can analyze this project, but:

⚠  The free plan only provides full branch analysis for main.

⚠  develop and other branches receive limited or no Quality Gate.

⚠  PR decoration works, but coverage and issues may be incomplete.

> This is normal and expected for free-tier SonarCloud usage.

--- 

## Quality Gates Summary (Develop vs Staging)

| Branch  | Build/Test | Checkstyle | SpotBugs | Coverage | Trivy | Newman | Docker | SonarCloud |
| ------- | ---------- | ---------- | -------- | -------- | ----- | ------ | ------ | ---------- |
| develop | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ❌    | ❌         |
| staging | ✔         | ✔          | ✔        | ✔       | ✔     | ✔     | ❌    | ❌         |


> **JaCoCo Coverage** reports are generated on both branches.

---

### Run CI Locally (Develop equivalent)

#### Full pipeline equivalent:

```sh
./mvnw clean verify
```

#### Individual checks:

```sh
./mvnw test
./mvnw checkstyle:check
./mvnw spotbugs:check
./mvnw jacoco:report
```

---

## Run CI on Github Actions

#### Reports Generated:

| Report           | Path                            |
| ---------------- | ------------------------------- |
| Checkstyle HTML  | `target/site/checkstyle.html`   |
| SpotBugs HTML    | `target/site/spotbugs.html`     |
| JaCoCo HTML      | `target/site/jacoco/`           |
| JaCoCo XML       | `target/site/jacoco/jacoco.xml` |
| Application logs | `spring.log` (CI artifact)      |

JaCoCo XML is uploaded for SonarCloud usage (on `main` only).

A **Trivy** report checks is included on `actions/runs`  job name: `Scan filesystem with Trivy` 

where you will see the:  `Library  │ Vulnerability  │ Severity │ Status │ Installed Version │ Fixed Version │ `  

> The report highlights vulnerable dependencies detected from your `pom.xml`

---

### Notes for Develop CI 

**`test` profile**: 
- JUnit tests run on H2 in test profile (default during Maven test phase).
- The file is located at: `\src\test\resources\application-test.properties` and 
  uses the standard configuration for an H2 database.
- This file was not mandatory, but is used to isolate the tests from the PostgreSQL service.

**`dev` profile**: 
- `dev` profile is activated automatically, it is used only when running the application manually.
- The file is located at: `\src\main\resources\application.properties` and 
  use `spring.profiles.active=dev` as default profile

- Recreates the schema on each startup (`create-drop`)
- Reloads demo data from `data.sql`

- Checkstyle and SpotBugs must both pass with 0 issues before commit.
- Run tests: all unit/integration tests must succeed before merge
- Compatible with CI/CD tools (GitHub Actions, Jenkins, GitLab CI)

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
