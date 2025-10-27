# CICD deployments
This folder contains the files and scripts related to the CI/CD pipelines for the **Flashcards** project.

# Flashcards App - CI/CD Status

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)
[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards)
[![Docker Pulls](https://img.shields.io/docker/pulls/valeriejeanne/flashcards)](https://hub.docker.com/r/valeriejeanne/flashcards)
[![Docker Image Size](https://img.shields.io/docker/image-size/valeriejeanne/flashcards/latest)](https://hub.docker.com/r/valeriejeanne/flashcards)

These badges show the current CI/CD status, Docker image version, and pull statistics.

### structure folder: 

```
flashcards/
├── src/                  # source code
├── pom.xml               # Maven configuration
├── Dockerfile            # Docker image build
├── ci-scripts/           # scripts used by pipelines
├── .github/workflows/    # GitHub Actions pipelines
├── readme.md             # project description
└── readme_ci.md          # CI/CD documentation
```

### Factored scripts in ci-scripts/

- **GitHub Actions** : build, test, build version Docker Image  

Each plateform will use its own files
This project is gradually evolving to illustrate best practices in multi-platform CI/CD.
