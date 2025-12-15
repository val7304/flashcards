# Flashcards CI/CD Pipeline Documentation

This folder contains the files and scripts related to the CI/CD pipelines for the **Flashcards** project.

## Flashcards CI/CD Status

#### Production Build & Release Pipeline

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)

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
![CI - Develop](https://github.com/val7304/flashcards/actions/workflows/develop.yml/badge.svg?branch=develop)
[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/staging.yml)

#### 'Actions Runs' Badges
![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)
![Build](https://img.shields.io/badge/Build-success-brightgreen)


> These badges display the current CI/CD status, Docker image information, and code quality results.

---

## Overview

This document describes the **CI/CD pipelines** and validation processes of the Flashcards application.

#### The CD pipeline on `main` automates:
- Build & packaging (Maven)
- Code formatting validation (Spotless)
- Static analysis (Checkstyle, SpotBugs)
- Unit & integration tests (JUnit 5, Spring Boot) 
- Coverage analysis (JaCoCo – XML generated for CI)
- Security scanning (filesystem) using Trivy
- Docker image build (non-root runtime user for container hardening)
- Security scanning (Docker image) using Trivy
- Container smoke tests:
    * Spring Boot health check
    * API endpoint validation
- Versioning & traceability
    * Git release tag (vX.Y.Z)
    * Docker tags: release version, short Git SHA, latest
- Docker image publishing to Docker Hub
- SonarCloud analysis: 
    * JaCoCo report upload
    * Quality Gate enforcement
- Production-ready artifact validation


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

--- 

### Branch profile

The project uses three Spring Boot profiles:

| Branch     | Profile   | Purpose                                 |
|------------|-----------|-----------------------------------------|
| develop    | `dev`     | Daily development + auto reset database |
| staging    | `staging` | Close-to-production integration tests   |
| main       | `prod`    | Production-like, persistent data        |

Each branch automatically loads the matching profile in CI/CD.

----

## CI Workflow

location: `.github/workflows/main.yml`

The following workflow is triggered on `push` targeting the `main` branch:

```text
.github/workflows/main.yml
├─ Checkout & Maven cache                : Clone repository / restore dependencies
├─ Code format validation                : Spotless check
├─ Static analysis                       : Checkstyle + SpotBugs
├─ Build & Tests                         : Unit + integration tests (JUnit 5)
├─ Coverage (JaCoCo)                     : Generate XML report for CI
├─ Security scan (Trivy filesystem)      : CVE detection on project dependencies
├─ Build Docker image                    : Tagged with short SHA
├─ Security scan (Trivy Docker image)    : CVE detection on final container image
├─ Container smoke tests                 : Health check + API endpoint validation
├─ Release tagging                       : Git release tag (vX.Y.Z)
├─ Push Docker image to Docker Hub       : Version, short SHA and latest tags
├─ SonarCloud analysis                   : JaCoCo upload + Quality Gate
└─ Workspace cleanup                     : Final cleanup
```

Trivy is used to detect vulnerabilities both in:
- project dependencies (filesystem scan)
- the final Docker image (image scan)

It ensures that produced artifacts are secure before being deployed or published.

---

## Quality Gates Summary (all branches)

| Branch  | Build/Test | Checkstyle | SpotBugs | Coverage | Trivy | Newman | Docker | SonarCloud |
| ------- | ---------- | ---------- | -------- | -------- | ----- | ------ | ------ | ---------- |
| develop | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ❌     | ❌        |
| staging | ✔         | ✔          | ✔        | ✔       | ✔     | ✔     | ❌     | ❌        |
| main    | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ✔      | ✔         |

> **JaCoCo Coverage** reports are generated on all branches.

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

##  Code Quality & Continuous Inspection

The project uses SonarCloud to analyze:

- Bugs
- Code Smells
- Vulnerabilities
- Test coverage (JaCoCo)
- Code duplication

[See the full analysis on SonarCloud](https://sonarcloud.io/project/overview?id=val7304_flashcards)

> The Quality Gate must be **Green** for the CI/CD pipeline to be validated.

---

## CI/CD Platforms

### GitHub Actions: 
- **GitHub Actions**  — Automated build and Docker publishing  
- **Docker Hub**      — Stores ready-to-deploy images  
- **ci-scripts/**     — Standardized shell scripts reusable across CI platforms  

> Reusable in Jenkins or GitLab CI with minimal adaptations.

---

## Tests Instructions & Code Quality

#### Run locally:

This project uses Spotless to enforce consistent code formatting.

The CI pipeline runs:

```sh
./mvnw spotless:check
```

Before committing, or if formatting issues are detected, apply fixes locally (before the clean verify cmd) to ensure formatting is correct:

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

---

## Run CI/CD on Github Actions

#### Reports Generated:

| Branch           | Report             | Location / Artifact                                                |
| ---------------- | ------------------ | ------------------------------------------------------------------ |
| local (developer)| Checkstyle HTML    | `target/site/checkstyle.html`                                      |
| local (developer)| SpotBugs HTML      | `target/site/spotbugs.html`                                        |
| develop          | JaCoCo HTML + XML  | `target/site/jacoco/`       (XML uploaded as CI artifact)          |
| staging          | JaCoCo HTML + XML  | `target/site/jacoco/jacoco.xml` (HTML + XML uploaded as artifacts) |
| staging          | Application logs   | `spring.log` (uploaded as CI artifact)                             |
| main             | JaCoCo XML only    | `target/site/jacoco/jacoco.xml` (uploaded for SonarCloud)          |

JaCoCo XML is uploaded for SonarCloud usage (on `main` only).
 
> Limitation of the SonarCloud free plan: only the `main` branch is analyzed

A **Trivy** report is available in the GitHub Actions logs under the job `Scan filesystem with Trivy` 
where you will see the:  `Library  │ Vulnerability  │ Severity │ Status │ Installed Version │ Fixed Version │ `  

> The report highlights vulnerable dependencies detected from your `pom.xml`

---

## Notes for `main` CI

### **test profile:** 
JUnit tests run using the `test` Spring profile, isolated from production-like services.
The configuration file is located at: `src/test/resources/application-test.properties`

Uses an in-memory H2 database to ensure:
- fast execution
- deterministic results
- no dependency on PostgreSQL during test phases

> This explicit test profile fixes previous integration-test instability and guarantees clean CI runs.

### **prod profile:** 

The `prod` profile is activated automatically when the application is started manually or inside Docker.
The file is located at: `\src\main\resources\application.properties` and use `spring.profiles.active=prod` as default profile.

This profile is not used during CI tests, ensuring strict separation between test and production-like execution.
Designed for:
- persistent data
- Docker and deployment scenarios
- production parity

**Image versioning & traceability**
The CI pipeline produces three Docker tags per successful run:
- Release tag (e.g. v0.87.0) → human-readable, semantic version used for releases and documentation
- Short Git SHA (e.g. fd268dd) → immutable technical reference for audit, rollback and traceability
- latest → convenience tag pointing to the most recent successful production build

> All tags reference the same image digest, ensuring full consistency between GitHub releases and Docker Hub artifacts.

**Code formatting & quality gates**: 
Spotless is enforced in CI:
- formatting violations fail fast
- guarantees consistent code style across contributors
- Checkstyle and SpotBugs must both pass with 0 issues
- JaCoCo coverage is generated on all branches
- SonarCloud Quality Gate must be green on main

**Smoke tests** (container-level validation)
Before publishing images to Docker Hub:
- The Docker image is started in CI
- Spring Boot health endpoint is checked 
- A real API endpoint (/api/categories) is called
- Image publication is blocked if any step fails
> This ensures that only runnable, production-ready images are published.

**CI/CD readiness**
All unit and integration tests must succeed before merge
Fully compatible with:
- GitHub Actions
- Jenkins
- GitLab CI

> CI scripts are centralized and reusable (ci-scripts/)

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
