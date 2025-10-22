# CICD deployments
This folder contains the files and scripts related to the CI/CD pipelines for the **Flashcards** project.

### structure folder: 

flashcards/
│
├── src/                          # source code (Spring Boot)
├── pom.xml                       # Build Maven
├── Dockerfile                    # Image Docker Build
│
├── ci-scripts/                   # common scripts pipelines
│ ├── build.sh
│ ├── test.sh
│ └── docker-build.sh
│
├── .github/
│ └── workflows/
│ └── main.yml                    # GitHub Actions
│
├── readme.md
└── readme_ci.md                  # actual file


### Factored scripts in ci-scripts/

- **GitHub Actions** : build, test, build version Docker Image  

Each plateform will use its own files
This project is gradually evolving to illustrate best practices in multi-platform CI/CD.
