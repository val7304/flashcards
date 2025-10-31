# CI/CD Deployments

This folder contains the files and scripts related to the CI/CD pipelines for the **Flashcards** project.

# Flashcards App - CI/CD Status

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)
[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards)
[![Docker Pulls](https://img.shields.io/docker/pulls/valeriejeanne/flashcards)](https://hub.docker.com/r/valeriejeanne/flashcards)
[![Docker Image Size](https://img.shields.io/docker/image-size/valeriejeanne/flashcards/latest)](https://hub.docker.com/r/valeriejeanne/flashcards)

These badges show the current CI/CD pipeline status, Docker image version, and usage statistics.

### structure folder: 

```text
flashcards/
├── src/                  # Application source code
├── pom.xml               # Maven configuration and dependencies
├── Dockerfile            # Docker image build instructions
├── ci-scripts/           # Helper scripts used by pipelines (build/test/deploy)
├── .github/workflows/    # GitHub Actions CI/CD pipelines
├── readme.md             # Main project documentation
└── readme_ci.md          # CI/CD documentation (this file)
```

### CI/CD Overview
GitHub Actions — handles build, tests, and Docker image publishing.
Docker Hub — hosts the built images for deployment and testing.
ci-scripts/ — reusable bash scripts to standardize build and test steps.

Each platform (GitHub Actions, Jenkins, GitLab CI, etc.) can reuse the same logic with minimal modification.

### Purpose
This project evolves progressively to illustrate multi-platform CI/CD best practices, including:

- Environment variable management (DB, credentials)
- Profile-based builds (dev / prod)
- Automated testing and code quality checks (Checkstyle, JUnit)
- Continuous integration → Docker image build → Continuous deployment

