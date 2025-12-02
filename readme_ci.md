# Flashcards CI/CD Documentation

This folder contains the files and scripts related to the CI/CD pipelines for the **Flashcards** project.

## Flashcards CI/CD Status

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)
[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards/tags)
[![Docker Pulls](https://img.shields.io/docker/pulls/valeriejeanne/flashcards)](https://hub.docker.com/r/valeriejeanne/flashcards)
[![Docker Image Size](https://img.shields.io/docker/image-size/valeriejeanne/flashcards/latest)](https://hub.docker.com/r/valeriejeanne/flashcards)
[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/staging.yml)

![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)
![Build](https://img.shields.io/badge/Build-success-brightgreen)

These badges show the current CI/CD pipeline status, Docker image version, usage statistics, result scan of CheckStyle and Spotbugs.

---

## Overview

This document describes the **CI/CD pipelines** and validation processes of the Flashcards application.

The pipelines automate:
- Build & packaging (Maven)
- Static analysis (Checkstyle, SpotBugs)
- Unit and integration tests
- Docker build & publishing
- Deployment preparation (GitHub Actions, Jenkins, GitLab CI)

---

## Structure

```text
flashcards/
├── src/                  # Application source code
├── pom.xml               # Maven configuration and dependencies
├── Dockerfile            # Docker image build instructions
├── ci-scripts/           # Helper scripts for CI/CD (build/test/deploy)
├── .github/workflows/    # GitHub Actions CI/CD definitions
├── readme.md             # Main project documentation
└── readme_ci.md          # CI/CD documentation (this file)
```

--- 

## CI/CD Platforms

### GitHub Actions: 
- **GitHub Actions** — Automated build and Docker publishing  
- **Docker Hub** — Stores ready-to-deploy images  
- **ci-scripts/** — Standardized shell scripts reusable across CI platforms  
Reusable in Jenkins or GitLab CI with minimal adaptations.

**The next lab will involve deploying to GitLab.**

--- 

## Build and Quality Stages

| Stage                | Tool / Action                | Description |
|----------------------|------------------------------|--------------|
| **Build**            | `./mvnw clean package`       | Compiles and packages the Spring Boot application |
| **Tests**            | `./mvnw test`                | Runs unit and integration tests using JUnit and Mockito |
| **Checkstyle**       | `./mvnw checkstyle:check`    | Enforces Java code style and formatting rules |
| **SpotBugs**         | `./mvnw spotbugs:check`      | Performs static bytecode analysis to detect potential bugs |
| **Docker Build**     | `docker build .`             | Builds and tags the Docker image for deployment |
| **Publish**          | `docker push`                | Pushes the built image to Docker Hub for distribution |

---
## Tests & Code Quality

### Build and Tests

```sh
./mvnw clean verify

# or
./mvnw clean package
./mvnw test
```

Integration tests use `MockMvc` and `SpringBootTest`.

Common example:
> Integration test failure: expected HTTP 204 but got 200 → adjust controller or test case.

---

## Code Quality & Static Analysis

Code quality is continuously checked using Maven plugins integrated into the build lifecycle. These tools enforce best practices, prevent common bugs, and ensure the project remains production-ready.

### 1. Checkstyle

Checkstyle enforces consistent code style, ensuring readability and long-term maintainability.

```sh
./mvnw checkstyle:check
```

#### Ensures:
- No unused imports
- Proper spacing and comments  
- Consistent naming conventions  

#### Reports generated:
- XML: `target/checkstyle-result.xml`
- HTML: `target/site/checkstyle.html`

**Latest status**: No Checkstyle violations (0 errors)

---

### 2. SpotBugs

SpotBugs performs bytecode analysis to detect potential logic errors or resource issues.

```sh
./mvnw spotbugs:check
```

Detects: null-pointer risks, synchronization errors, unused fields.  

#### Reports:
- XML: `target/spotbugsXml.xml`
- HTML: `target/site/spotbugs.html`

- Configuration defined in: `spotbugs.xml`
 
**Latest status**: 0 SpotBugs issues detected

#### Notes:
- Framework dependencies like ResponseEntity and JpaRepository are safely ignored
- No null-pointer, unused field, or synchronization issues detected

---

### Validation and Build Consistency
- ./mvnw clean verify passes successfully
- Validity checks confirm consistency between mappers, DTOs, and entities
- Project compiles cleanly on Java 17 with Spring Boot 3

```sh
./mvnw clean verify

```
#### Ensures:
- Mappers, DTOs, and entities are consistent
- Application compiles cleanly under Java 17 / Spring Boot 3
- Code passes all Checkstyle, SpotBugs, and test phases

---

## Local Pre-Commit Validation
To ensure the same quality gates as CI/CD before pushing changes:

```sh
# Clean and rebuild
./mvnw clean verify

# Run static code analysis
./mvnw checkstyle:check
./mvnw spotbugs:check

# Run tests
./mvnw test

```

If all commands pass successfully, your code is production-ready and can be safely committed and pushed.

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
