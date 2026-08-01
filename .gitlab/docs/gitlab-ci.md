## GitLab CI/CD

This repository contains a complete GitLab CI/CD implementation for the Flashcards application.
The GitLab implementation includes:

* Multi-environment pipelines (`develop`, `staging`, `main`)
* Automatic promotion between environments
* Static code analysis with SonarCloud
* Security scanning with Semgrep and Trivy
* Docker image build and validation
* Functional, performance and smoke testing
* Reusable GitLab CI templates

---

## GitLab Documentations

- [GitLab Flow](https://gitlab.com/val7304/flashcards/-/blob/main/.gitlab/docs/gitlab-flow.md)
- [Automatic promotion](https://gitlab.com/val7304/flashcards/-/blob/main/.gitlab/docs/gitlab-flow.md#automatic-promotion)
- [Develop pipeline](https://gitlab.com/val7304/flashcards/-/blob/main/.gitlab/docs/develop.md)
- [Staging pipeline](https://gitlab.com/val7304/flashcards/-/blob/main/.gitlab/docs/staging.md)
- [Production pipeline](https://gitlab.com/val7304/flashcards/-/blob/main/.gitlab/docs/main.md)
- [GitHub Actions vs GitLab CI](https://gitlab.com/val7304/flashcards/-/blob/main/.gitlab/docs/github-vs-gitlab.md)

---

## Pipeline Strategy

The pipeline follows a progressive promotion workflow:

```text
                Merge Request
                     │
                     ▼
feature/* ─────► develop
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

Each environment has its own pipeline with quality gates, security checks and deployment strategy.
Each environment has its own dedicated pipeline adapted to its purpose.

| Pipeline      | Goal                      |
| ------------- | ------------------------- |
| develop       | Continuous Integration    |
| staging       | Pre-production validation |
| main          | Production release        |
| auto-promote  | Between branches          |

Each pipeline increases the validation level before promoting the application to the next environment.

---

## Repository organization

```text
FLASHCARDS
├──.github
├──.gitlab
│   ├─── docs
│   ├─── issue_templates
│   ├─── jobs
│   │    ├─── develop.yml
│   │    ├─── staging.yml
│   │    ├─── main.yml
│   │    └─── auto-promote.yml
│   ├─── merge_request_templates
│   │    └─── Default.md
│   ├─── templates
│   │    ├─── artifacts.yml
│   │    ├─── docker.yml
│   │    ├─── maven.yml
│   │    ├─── rules.yml
│   │    └─── security.yml
│   ├─── variables
│   │    ├─── global.yml
│   │    ├─── develop.yml
│   │    ├─── staging.yml
│   │    └─── main.yml
│   └─── Labels.md
├── ci-scripts/  
│   ├── docker-build.sh      ← shared
│   ├── docker-push.sh       ← shared 
│   ├── sonar.sh             ← shared
│   ├── test.sh              ← shared
│   ├── package.sh           ← Gitlab only
│   ├── trivy-fs.sh          ← Gitlab only
│   └── trivy-image.sh       ← Gitlab only
└── .gitlab-ci.yml   
```

* `.gitlab/jobs` : Contains all environment pipelines.
* `.gitlab/templates`: Reusable job templates.
* `.gitlab/variables`: Global CI variables.
* `.gitlab/merge_request_templates`: Merge request templates.
* `.gitlab/issue_templates`: Issue templates.
* `.gitlab/Labels.md`: labels used in Gitlab.

* `ci-scripts/`: Shared shell scripts reused by GitHub Actions and GitLab CI whenever possible.

---

### semgrep-sast

Provided by the GitLab `Security/SAST.gitlab-ci.yml` template and launched automatically on all branches during the `security` stage.

Rules and excluded paths are configured in the `global variables` file:

  * Enable `INFO` log level.
  * Exclude the "SpotBugs" analyser (already executed during the *-build stage).
  * Exclude the "target/" and "load-test/" directories.
 
Runs static security analysis on the source code.

Purpose:

- Detect insecure coding patterns
- Identify common vulnerabilities
- Enforce secure coding practices

---

## Maintainer

**Valérie Hermans**
- GitHub: https://github.com/val7304
- Email: valerie_hermans@outlook.com
