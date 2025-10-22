# CICD deployments
This folder contains the files and scripts related to the CI/CD pipelines for the **Flashcards** project.

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
