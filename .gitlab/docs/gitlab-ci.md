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


## GitLab Documentation

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

| Branch    | Purpose |
|---------  |---------|
| `develop` | Continuous Integration |
| `staging` | Pre-production validation |
| `main`    | Production release |

Each pipeline progressively increases the validation level before code reaches production.

---

## Repository organization

```text
FLASHCARDS
├───.gitlab
│   ├─── docs
│   ├─── issue_templates
│   ├─── jobs
│   │    ├─── develop.yml
│   │    ├─── staging.yml
│   │    ├─── main.yml
│   │    └─── auto-promote.yml
│   ├─── merge_request_templates
│   │    └─── Default.md
│   ├───templates
│   │    ├─── artifacts.yml
│   │    ├─── docker.yml
│   │    ├─── maven.yml
│   │    ├─── rules.yml
│   │    └─── security.yml
│   ├───variables
│   │    ├─── global.yml
│   │    ├─── develop.yml
│   │    ├─── staging.yml
│   │    └─── main.yml
│   └─── Labels.md
├───.gitlab-ci.yml   
```

* `.gitlab/jobs` : Contains all environment pipelines.
* `.gitlab/templates`: Reusable job templates.
* `.gitlab/variables`: Global CI variables.
* `.gitlab/merge_request_templates`: Merge request templates.
* `.gitlab/issue_templates`: Issue templates.

* `.gitlab/Labels.md`: labels used in Gitlab.

---
## Maintainer

**Valérie Hermans**
- GitHub: https://github.com/val7304
- Email: valerie_hermans@outlook.com
