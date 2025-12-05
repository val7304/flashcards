# Flashcards CI/CD Pipeline Documentation

This folder contains the files and scripts related to the CI/CD pipelines for the **Flashcards** project.

## Flashcards CI/CD Status

#### Continuous Deployment  

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)

#### Docker Hub Registry 

[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards/tags)
[![Docker Pulls](https://img.shields.io/docker/pulls/valeriejeanne/flashcards)](https://hub.docker.com/r/valeriejeanne/flashcards)
[![Docker Image Size](https://img.shields.io/docker/image-size/valeriejeanne/flashcards/latest)](https://hub.docker.com/r/valeriejeanne/flashcards)

#### Continuous Integration (Branch) 
![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/develop.yml/badge.svg?branch=develop)
[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/staging.yml)

#### Actions Runs Badges
![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)
![Build](https://img.shields.io/badge/Build-success-brightgreen)


> These badges display the current CI/CD status, Docker image information, and code quality results.

---

## Overview

This document describes the **CI/CD pipelines** and validation processes of the Flashcards application.

#### The CD pipeline on `main` automates:
- Build & packaging (Maven)
- Static analysis (Checkstyle, SpotBugs)
- Unit & integration tests (JUnit 5)
- Coverage analysis (JaCoCo) 
- Security scanning (Trivy)
- Docker build & publishing on Docker Hub Registry
- Connect and send JaCoCo report to SonarQube Cloud
- Ready to Deploy 

---

## Project structure

```text
flashcards/
 ├─ .github/workflows/main.yml       # main CD pipeline
 ├─ src/test/java/                   # Unit & integration tests
 ├─ src/main/java/                   # Application source code
 ├─ src/main/resources/
 │   ├─ application.properties
 │   └─ application-prod.properties
 │   └─ data.sql
 ├─ config/checkstyle/                  
 │   ├─ checkstyle.xml
 │   └─ checkstyle-suppressions.xml
 ├─ Dockerfile
 ├─ pom.xml
 └─ sonar-project.properties
```

#### The pipeline uses the project's dedicated Checkstyle configuration
These rules are enforced automatically during the CI pipeline stages.

> Note: The file `sonar-project.properties` is only required on branches scanned by SonarCloud 

> Only `main` branch when using the free plan

--- 

### Branch profile

The project uses three Spring Boot profiles:

| Branch     | Profile   | Purpose                                 |
|------------|-----------|-----------------------------------------|
| develop    | `dev`     | Daily development + auto reset database |
| staging    | `staging` | Close-to-production integration tests   |
| main       | `prod`    | Production-like, persistent data        |

Each branch automatically loads the matching profile in CI/CD.

<!-- ### Environments & Profiles

| Profile   | Purpose                  | Database   | Reset Behavior   |
| --------- | ------------------------ | ---------- | ---------------- |
| `test`    | Unit + integration tests | H2         | Always clean     |
| `dev`     | Local development        | PostgreSQL | create / drop    |
| `staging` | CI / QA                  | PostgreSQL | update           |
| `prod`    | (future)                 | PostgreSQL | validated schema | -->


----

## CI Workflow

location: `.github/workflows/main.yml`

The following workflow is triggered on `push` targeting the `main` branch:

```text
.github/workflows/main.yml
├─ Checkout & Maven cache                : Clone repo / restore dependencies
├─ Static analysis                       : Checkstyle + SpotBugs
├─ Build & Tests                         : Unit + integration tests
├─ Coverage (JaCoCo)                     : Generate XML + HTML reports
├─ Security scan (Trivy filesystem)      : CVE detection on project directory
├─ Upload artifacts                      : Checkstyle, SpotBugs, logs, coverage
├─ Build Docker image                    : with version tag and Latest
├─ Security scan (Trivy Docker image)
├─ Push Docker image to Docker Hub (version + latest tags)
├─ SonarQube Cloud                       : Push the JaCoCo report on SonarQube Cloud
└─ Workspace cleanup                     : Final cleanup
```

Trivy is used to detect vulnerabilities both in:
- project dependencies (filesystem scan)
- the final Docker image (image scan)

It ensures that produced artifacts are secure before being deployed or published.

---

## SonarCloud Integration

This project uses SonarCloud to analyze code quality on the `main` branch.

The workflow:
1. Generates JaCoCo XML coverage report
2. Uploads it to the pipeline
3. Runs `SonarSource/sonarqube-scan-action`
4. SonarCloud reads `sonar-project.properties`
5. Displays results (Bugs, Code Smells, Coverage, Duplications)

SonarCloud is triggered only on the `main` branch because the free plan supports a single branch.

---

## Quality Gates Summary (all branches)

| Branch  | Build/Test | Checkstyle | SpotBugs | Coverage | Trivy | Newman | Docker | SonarCloud |
| ------- | ---------- | ---------- | -------- | -------- | ----- | ------ | ------ | ---------- |
| develop | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ❌     | ❌        |
| staging | ✔         | ✔          | ✔        | ✔       | ✔     | ✔     | ❌     | ❌        |
| main    | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ✔      | ✔         |

> **JaCoCo Coverage** reports are generated on all branches.

---

## CI/CD Platforms

### GitHub Actions: 
- **GitHub Actions**  — Automated build and Docker publishing  
- **Docker Hub**      — Stores ready-to-deploy images  
- **ci-scripts/**     — Standardized shell scripts reusable across CI platforms  

> Reusable in Jenkins or GitLab CI with minimal adaptations.

---

## Tests Instructions & Code Quality

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
./mvnw checkstyle:check
./mvnw spotbugs:check
./mvnw jacoco:report
```

---

## Run CI/CD on Github Actions

#### Reports Generated:

| Report           | Path                            |
| ---------------- | ------------------------------- |
| Checkstyle HTML  | `target/site/checkstyle.html`   |
| SpotBugs HTML    | `target/site/spotbugs.html`     |
| JaCoCo HTML      | `target/site/jacoco/`           |
| JaCoCo XML       | `target/site/jacoco/jacoco.xml` |
| Application logs | `spring.log` (CI artifact)      |

JaCoCo XML is uploaded for SonarCloud usage (on `main` only).
 
A **Trivy** report is available in the GitHub Actions logs under the job `Scan filesystem with Trivy` 
where you will see the:  `Library  │ Vulnerability  │ Severity │ Status │ Installed Version │ Fixed Version │ `  

> The report highlights vulnerable dependencies detected from your `pom.xml`

---

### Developer Notes

- **Dev profile** (develop branch): drops and recreates schema at each run
- **Staging profile** (staging branch): keeps schema and loads test data once
- **Prod profile** (main branch): persistent data

- **Code quality**: 	Checkstyle and SpotBugs must both pass with 0 issues before commit
- **Tests**:        	All unit/integration tests must succeed before merge
- **CI/CD-ready**:  	Compatible with CI/CD tools (GitHub Actions, Jenkins, GitLab CI)

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
