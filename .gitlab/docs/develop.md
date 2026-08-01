# Develop Pipeline

The `develop` branch is the Continuous Integration (CI) environment.

Every commit pushed to `develop` triggers the pipeline below.

```text
develop:
   ▼   
Build  (test)
   │
   ├──────────────────┐   (security)
   ▼                  ▼
Trivy Filesystem     Semgrep SAST
   │
   ▼
auto-promote
```

---

## Purpose

The objective of the `develop` pipeline is to validate every code change before it can be promoted to the next environment.

| Job             | Purpose                                    |
| --------------- | ------------------------------------------ |
| `develop-build` | Spotless validation, compilation and tests |
| `Semgrep`       | Static Application Security Testing (SAST) |
| `develop-trivy` | Filesystem dependency vulnerability scan   |

It performs:

- Code formatting validation (Spotless)
- Project compilation
- Unit tests
- Static Application Security Testing (Semgrep)
- Filesystem vulnerability scanning (Trivy)

If every stage succeeds, the branch is considered stable and can be promoted to `staging`.

---

## Jobs

### develop-build

Responsibilities:

- Validate formatting with Spotless
- Compile the application
- Execute unit tests and IT tests in H2 base memory
- Generate the application package

Artifacts:

- JAR file
- JaCoCo reports
- Test reports

---

### semgrep-sast

Runs static code security analysis.

---

### develop-trivy

Runs a filesystem vulnerability scan.

Purpose:

- Detect vulnerable dependencies
- Report HIGH and CRITICAL vulnerabilities
