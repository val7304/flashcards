# Develop Pipeline

The `develop` branch is the Continuous Integration (CI) environment.

Every commit pushed to `develop` triggers the pipeline below.

```text
develop
   │
   ▼
Spotless
   │
   ▼
Build & Unit Tests
   │
   ▼
Package
   │
   ▼
Semgrep
   │
   ▼
Trivy Filesystem Scan
```

---

## Purpose

The objective of the `develop` pipeline is to validate every code change before it can be promoted to the next environment.

It performs:

- Code formatting validation (Spotless)
- Project compilation
- Unit tests
- Package generation
- Static Application Security Testing (Semgrep)
- Filesystem vulnerability scanning (Trivy)

If every stage succeeds, the branch is considered stable and can be promoted to `staging`.

---

## Jobs

### develop-build

Responsibilities:

- Validate formatting with Spotless
- Compile the application
- Execute unit tests
- Generate the application package

Artifacts:

- JAR file
- JaCoCo reports
- Test reports

---

### develop-semgrep

Runs static security analysis on the source code.

Purpose:

- Detect insecure coding patterns
- Identify common vulnerabilities
- Enforce secure coding practices

---

### develop-trivy

Runs a filesystem vulnerability scan.

Purpose:

- Detect vulnerable dependencies
- Report HIGH and CRITICAL vulnerabilities

---

## Pipeline summary

| Stage | Purpose |
|--------|---------|
| Spotless | Code formatting validation |
| Build | Compile the application |
| Tests | Execute unit tests |
| Package | Build the JAR |
| Semgrep | Static code security analysis |
| Trivy | Dependency vulnerability scanning |

---

The `develop` pipeline focuses on fast feedback for developers before code reaches the pre-production environment.