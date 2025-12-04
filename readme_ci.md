# Flashcards CI Pipeline Documentation (Branch: `staging`)

[![CI - Staging](https://github.com/val7304/flashcards/actions/workflows/staging.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/staging.yml)
[![CI/CD - Main](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)

The `staging` CI pipeline is dedicated to **code quality**, **testing**, **coverage**, **static analysis**, and **security scanning**.  
It replicates a realistic enterprise-level integration pipeline.

> **Note**  
> Docker build/publish and SonarCloud analysis are executed **only on the `main` branch**.

![Checkstyle](https://img.shields.io/badge/Checkstyle-passed-brightgreen)
![SpotBugs](https://img.shields.io/badge/SpotBugs-clean-brightgreen)

---

## Project structure

 ```text
flashcards/
 ├─ .github/workflows/staging.yml       # Staging CI pipeline
 ├─ src/main/java/                      # Application source code
 ├─ src/test/java/                      # Unit & integration tests
 ├─ src/main/resources/
 │   ├─ application.properties
 │   └─ application-staging.properties
 │   └─ data.sql
 ├─ config/checkstyle/                  
 │   ├─ checkstyle.xml
 │   └─ checkstyle-suppressions.xml
 ├─ postman/
 │   └─ flashcards.postman_collection.json
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
├─ Checkout & Maven cache                : Clone repo / restore dependencies
├─ Static analysis                       : Checkstyle + SpotBugs
├─ PostgreSQL service                    : Real database for integration tests
├─ Build & Tests                         : Unit + integration tests
├─ Coverage (JaCoCo)                     : Generate XML + HTML reports
├─ Security scan (Trivy filesystem)      : CVE detection on project directory
├─ Start Spring Boot (staging profile)   : Run app on port 8081
├─ API tests with Newman                 : Live API validation
├─ Upload artifacts                      : Checkstyle, SpotBugs, logs, coverage
└─ Workspace cleanup                     : Final cleanup
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

## Quality Gates Summary (Develop vs Staging)

| Branch  | Build/Test | Checkstyle | SpotBugs | Coverage | Trivy | Newman | Docker | SonarCloud |
| ------- | ---------- | ---------- | -------- | -------- | ----- | ------ | ------ | ---------- |
| develop | ✔         | ✔          | ✔        | ✔       | ✔     | ❌    | ❌    | ❌         |
| staging | ✔         | ✔          | ✔        | ✔       | ✔     | ✔     | ❌    | ❌         |


> **JaCoCo Coverage** reports are generated on both branches.

---

### Integration tests run with:

- @SpringBootTest
- Real PostgreSQL service
- Web environment on port 8081 (CI)
- Application started with profile: staging

---

## Run CI Locally (staging equivalent)

Full pipeline equivalent:
```sh
./mvnw clean verify     #inclut tests + JaCoCo
```

Individual checks:
```sh
./mvnw checkstyle:check 
./mvnw spotbugs:check
./mvnw test
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

A **Trivy** report checks is included on `actions/runs/`, job name: `Scan filesystem with Trivy` 
where you will see the   `Library  │ Vulnerability  │ Severity │ Status │ Installed Version │ Fixed Version │ `  recommended to use from your analyzed pom.xml

---

#### Notes for Staging CI

- No Docker image is built
- No SonarCloud analysis (only on `main`)
- The application runs on port 8081
- Full API validation is performed using Newman
- All logs and reports are uploaded, even on failure

---

**Maintainer:** Valérie Hermans  
[valerie_hermans@outlook.com](mailto:valerie_hermans@outlook.com)  
[GitHub Profile](https://github.com/val7304)
