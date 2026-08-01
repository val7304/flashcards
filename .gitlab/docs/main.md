# Production Pipeline

The `main` branch represents the production environment.

Only validated code from the `staging` branch should reach this pipeline.

```text
main: 
   ▼
Build & Unit Tests   (test)
   │
   ├─────────────────┐ (security)
   ▼                 ▼
Trivy Filesystem     Semgrep SAST
   │             
   ▼
Publish sonarCloud      (quality)
   │
   ▼
Docker Build
   │
   ▼
Trivy Image      (security-image)
   │
   ▼
prod-smoke

(  │
   ▼
prod-publish)        as soon as possible
```

---

## Purpose

The production pipeline performs the final validation before publishing the application.

It combines quality analysis, security scanning, container validation and production publishing.

It performs:

- Project compilation
- Unit tests
- Code coverage generation
- Static security analysis
- Dependency vulnerability scanning
- SonarCloud quality analysis
- Docker image creation
- Container vulnerability scanning
- Smoke testing
- Docker Hub publication

---

## Jobs

### prod-build

Responsibilities:

- Validate formatting with Spotless
- Compile the application
- Execute unit tests

Artifacts:

- Test reports
- JaCoCo reports

---

### prod-package

Responsibilities:

- Resolve Maven dependencies
- Build the application
- Generate coverage reports

Artifacts:

- JAR file
- Compiled classes
- JaCoCo reports

---

### semgrep-sast

Runs static code security analysis.

---

### prod-trivy-fs

Runs a filesystem vulnerability scan.

Purpose:

- Detect vulnerable dependencies
- Block HIGH and CRITICAL vulnerabilities

---

### prod-sonarqube

Runs SonarCloud analysis.

Purpose:

- Code quality analysis
- Maintainability
- Reliability
- Security Hotspots
- Quality Gate validation

---

### prod-docker-build

Builds the production Docker image.

Artifacts:

- Docker image archive

---

### prod-trivy-image

Scans the Docker image before publication.

Purpose:

- Detect vulnerabilities inside the container image

---

### prod-smoke

Starts the application inside Docker.

Checks:

- PostgreSQL connectivity
- Spring Boot startup
- Health endpoint
- Basic API availability

---

### prod-publish

Publishes the validated Docker image.

Actions:

- Login to Docker Hub
- Tag images
- Push version tags
- Update the `latest` tag

---

## Pipeline summary

| Stage | Purpose |
|--------|---------|
| Build | Compile and execute unit tests |
| Package | Build application and coverage |
| Semgrep | Static security analysis |
| Trivy FS | Dependency vulnerability scan |
| SonarCloud | Quality Gate validation |
| Docker | Build production image |
| Trivy Image | Container vulnerability scan |
| Smoke | Production validation |
| Publish | Push image to Docker Hub |

---

The production pipeline is the final quality gate before releasing the application to Docker Hub.