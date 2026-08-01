# Staging Pipeline

The `staging` branch represents the pre-production environment.

Every successful promotion from `develop` triggers the staging pipeline.

```text
staging:
   ▼
Build (UT - H2)
   │
   ▼
Integration Tests (PostgreSQL) (JAR produced)
   │
   ├───────────────┐    (security)
   ▼               ▼
Trivy Filesystem   Semgrep SAST
   │
   └──────────────┐
                  ▼
            Docker Build
                  │
                  ▼
            Trivy Image (security-image)
                  │
                  ▼
         Functional Tests
                  │
                  ▼
         Performance Tests
                  │
                  ▼
            Auto Promote
```

---

## Purpose

The staging pipeline validates the application in an environment that closely matches production.

It performs:

- Project compilation
- Unit tests
- Package generation
- Code coverage report generation
- Static security analysis
- Dependency vulnerability scanning
- Docker image build
- Functional API testing
- Performance testing

Only fully validated code can be promoted to the production branch.

---

## Jobs

### staging-build

Responsibilities:

- Validate formatting with Spotless
- Compile the application
- Execute unit tests

Artifacts:

- Test reports
- JaCoCo reports

---

### staging-package

Responsibilities:

- Build the application package
- Generate the JaCoCo coverage report

Artifacts:

- JAR file
- JaCoCo reports

---

### semgrep-sast

Runs static code security analysis.

---

### staging-trivy

Runs a filesystem vulnerability scan.

Purpose:

- Detect vulnerable dependencies
- Report HIGH and CRITICAL vulnerabilities

---

### staging-docker-build

Responsibilities:

- Build the Docker image
- Export the image as an artifact

Artifacts:

- Docker image archive

---

### staging-functional

Runs end-to-end API validation.

Purpose:

- Start PostgreSQL
- Deploy the application
- Execute Postman collections
- Validate expected responses

---

### staging-load-test

Runs performance testing using K6.

Purpose:

- Validate application stability under load
- Produce performance reports

---

## Pipeline summary

| Stage | Purpose |
|--------|---------|
| Build | Compile and execute unit tests |
| Package | Build JAR and generate coverage |
| Semgrep | Static security analysis |
| Trivy | Dependency vulnerability scanning |
| Docker | Build Docker image |
| Functional | API validation with Postman |
| Performance | Load testing with K6 |

---

The staging pipeline validates the complete application before production deployment.