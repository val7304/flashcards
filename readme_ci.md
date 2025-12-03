# Flashcards CI Pipeline (Branch: develop)

This document describes the **CI pipeline dedicated to the `develop` branch**.  
It focuses entirely on **code quality**, **tests**, **coverage**, **static analysis**, and **security scanning**.  

**No Docker image is built or published on this branch.**  
**This pipeline mirrors what is commonly used in enterprise environments for feature/integration development.**

---
# Flashcards CI Documentation

![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/develop.yml/badge.svg?branch=develop)
[![CI - Main](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)


> **Note :**  
> The `develop` branch runs a limited CI pipeline: Build, Tests, Checkstyle, SpotBugs, and Jacoco generation.  
> Full pipelines including Docker build/push and SonarCloud are only run on `main`.

![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)

---
## CI/CD Differences: develop vs main

| Branch   | Build/Test | Checkstyle | SpotBugs | Coverage | Docker build/push | SonarCloud |
|----------|------------|------------|----------|----------|--------------------|------------|
| develop  | ✔          | ✔          | ✔        | ✔ Jacoco | ❌                | ❌         |
| main     | ✔          | ✔          | ✔        | ✔ Jacoco | ✔                | ✔         |

---
## Overview
The CI pipeline on `develop` automates:
- Build & packaging (Maven)
- Unit & integration tests (JUnit 5)
- Coverage analysis (JaCoCo)
- Static code quality scanning (Checkstyle, SpotBugs)
- Security scanning (Trivy)
- Artifact publishing (JaCoCo reports)

It serves as the **quality gate** before merging into *staging* or *main*.

---
## Structure
```text
flashcards/
├── src/                  # Application source code
├── pom.xml               # Maven configuration and dependencies
├── Dockerfile            # Docker image build instructions (used only on main)
├── ci-scripts/           # Helper scripts for CI/CD (used only in main)
├── .github/workflows/    # GitHub Actions workflows
│     ├─ main.yml         # Production pipeline (Docker + publish)
│     └─ develop.yml      # Developer CI pipeline (this pipeline)
├── readme.md             # Main project documentation
└── readme_ci.md          # CI documentation (this file)
```
---
## CI Platform

### GitHub Actions: 
The develop branch uses:
- **GitHub Actions Runners** 
- **PostgreSQL service container**  
- **Artifacts storage for JaCoCo coverage** 

No deployment or publishing occurs on this branch.

- **SonarCloud (Free Plan)** 

SonarCloud can analyze this project, but:
⚠  The free plan only provides full branch analysis for main.
⚠  develop and other branches receive limited or no Quality Gate.
⚠  PR decoration works, but coverage and issues may be incomplete.
This is normal and expected for free-tier SonarCloud usage.

--- 

## Build and Quality Stages
| Stage                | Tool / Action                     | Description |
|----------------------|-----------------------------------|-------------|
| **Build**            | `./mvnw clean package`            | Compiles and packages the Spring Boot application |
| **Tests**            | `./mvnw test`                     | Runs unit and integration tests using JUnit and Mockito |
| **Coverage**         | `./mvnw jacoco:report`            | Generates code coverage reports (XML + HTML) |
| **Checkstyle**       | `./mvnw checkstyle:check`         | Enforces Java code style and formatting rules |
| **SpotBugs**         | `./mvnw spotbugs:check`           | Performs static bytecode analysis to detect potential bugs |
| **Security Scan**    | `trivy fs .`                      | Scans dependencies and source for vulnerabilities |
| **Cleanup**          | `./mvnw clean`                    | Cleanup Maven workspace |
| **Reports Upload**   | GitHub Artifacts                  | Stores coverage results for download |


✔  This pipeline ensures that develop code remains clean before staging integration.

---
### Tests & Quality Instructions
#### Run everything locally:

```sh
./mvnw clean verify
```
Equivalent to CI:
- Build
- Tests
- Coverage
- Checkstyle
- SpotBugs

#### Run individually:

```sh
./mvnw test
./mvnw jacoco:report
./mvnw checkstyle:check
./mvnw spotbugs:check
```

---
#### Reports generated:

JaCoCo Coverage
- HTML report → `target/site/jacoco/`
- XML report → `target/site/jacoco/jacoco.xml`

XML is uploaded to artifacts for potential use by SonarCloud (on main only).

Checkstyle
- HTML → `target/site/checkstyle.html`
- XML → `target/checkstyle-result.xml`

**Latest status**: No Checkstyle violations (0 errors)

SpotBugs
- HTML → `target/site/spotbugs.html`
- XML → `target/spotbugsXml.xml`

All reports are created locally inside the runner; coverage is uploaded as an artifact.

---

## Local Pre-Commit Validation
Before pushing:

```sh
./mvnw clean verify   # full validation
```
Ensure:

✔ All tests pass  
✔ Checkstyle = 0 errors   
✔ SpotBugs = 0 issues   
✔ Coverage OK   

This guarantees that develop remains stable.

---
## Summary of Branch Roles

| Branch   | Purpose            | Pipeline                                   | Produces Docker Image |
|----------|--------------------|---------------------------------------------|--------------------------|
| **develop** | Dev integration     | Full CI (tests + quality + security)        | ❌ No                    |
| **staging** | Pre-production      | Same + integration tests                    | ⚠ Optional               |
| **main**    | Production          | Build + push Docker                         | ✔ Yes                   |

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
