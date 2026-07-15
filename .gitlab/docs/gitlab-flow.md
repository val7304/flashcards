# GitLab Flow

The project follows a progressive promotion strategy.

```text
feature/*
     │
     ▼
 develop
     │
     ▼
 Automatic Promotion
     │
     ▼
 staging
     │
     ▼
 Automatic Promotion
     │
     ▼
   main
```

## Design principles

The GitLab CI/CD implementation was designed around a few key principles:

- Progressive delivery (develop → staging → main)
- Reusable job templates
- Centralized configuration
- Automated environment promotion
- Security-first approach
- Pipeline modularity
- Easy maintenance and extensibility

---

### Repository organization

```text
.gitlab
│
├── jobs
├── templates
├── variables
├── docs
├── issue_templates
└── merge_request_templates
```
Each directory has a dedicated purpose to keep the CI/CD configuration modular and maintainable.

---

## Branch strategy

### feature/*

Feature branches are used to develop new functionality or bug fixes.

A Merge Request targets the `develop` branch.

---

### develop

Continuous Integration environment.

Objectives:

- Validate code formatting
- Execute unit tests
- Build the application
- Run security scans

Successful pipelines are automatically promoted to `staging`.

---

### staging

Pre-production environment.

Objectives:

- Validate Docker images
- Execute functional tests
- Execute performance tests
- Validate application behavior

Successful pipelines are automatically promoted to `main`.

---

### main

Production environment.

Objectives:

- SonarCloud Quality Gate
- Security validation
- Smoke testing
- Docker Hub publication

---

## Automatic Promotion

The repository uses an automatic promotion pipeline.

After a successful pipeline:

```
develop
    │
    ▼
Merge Request
    │
    ▼
staging
```

and

```
staging
    │
    ▼
Merge Request
    │
    ▼
main
```

Merge Requests are automatically created and include:

- source branch
- target branch
- commit SHA
- pipeline status
- author information

The merge still requires approval and conflict resolution when necessary.

---

### Reusable Templates

The pipeline relies on reusable templates to avoid duplicated configuration.

| Template      | Purpose             |
| ------------- | ------------------- |
| maven.yml     | Maven configuration |
| docker.yml    | Docker jobs         |
| security.yml  | Trivy & Semgrep     |
| rules.yml     | Branch rules        |
| artifacts.yml | Shared artifacts    |


#### Issue & Merge Request Templates

Standardized templates ensure consistent reporting and reviews.

Available templates include:

1. Standardized issue for:
   - Bug reports
   - Feature requests
   - Documentation improvements

2. Merge Request template

> These templates help contributors to provide consistent and complete information like:
- Description
- Testing
- Checklist
- Related issues
- Review information

---

### Reusable Variables

Global variables are centralized in: `.gitlab/variables/global.yml`

They define shared configuration such as:

`Java version` `Maven image` `Maven options` 
`Docker images` `Trivy image` `Semgrep image` `K6 image`
  
Environment-specific variables are also defined as follow: 

- `develop.yml` -  `staging.yml` - `main.yml` and define spring profile, server port, ..

---

## Labels

The repository uses a predefined set of labels to organize issues and merge requests.

Examples include: `Bug` `Feature` `Documentation` `Security` `CI/CD` `Enhancement`

The complete list is documented in:  `.gitlab/Labels.md`

---

## Benefits

This workflow provides:

- Progressive validation
- Environment isolation
- Automated promotion
- Reduced manual operations
- Better traceability