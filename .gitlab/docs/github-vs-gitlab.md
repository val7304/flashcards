## Github vs gitlab

| Capability          | GitHub Actions    | GitLab CI       |
| ------------------- | ----------------- | --------------- |
| Pipeline definition | Workflow          | Pipeline        |
| Reusable logic      | Composite Actions | Templates       |
| Security scanning   | CodeQL + Trivy    | Semgrep + Trivy |
| Docker publication  | Docker Hub        | Docker Hub      |
| Automatic promotion | Workflow          | Native pipeline |
| Quality             | SonarCloud        | SonarCloud      |

> Both implementations provide equivalent validation levels while taking advantage of the native capabilities offered by each platform.

---

| Feature             | GitHub                 | GitLab    |
| ------------------- | ---------------------- | --------- |
| CI                  | ✅                    | ✅        |
| CD                  | ✅                    | ✅        |
| Sonar               | ✅                    | ✅        |
| Docker              | ✅                    | ✅        |
| Security            | ✅                    | ✅        |
| functional tests    | ✅                    | ✅        |
| Performance tests   | ✅                    | ✅        |
| Automatic promotion | ✅                    | ✅        |
| Reusable templates  | ⚠️ Composite Actions  | ✅ Native |

---

| Capability                                 | GitHub                             | GitLab                     |
| ------------------------------------------ | ---------------------------------- | -------------------------- |
| CI/CD engine                               | GitHub Actions                     | GitLab CI                  |
| Static Application Security Testing (SAST) | CodeQL                             | Semgrep                    |
| Dependency scanning                        | Trivy Action                       | Trivy (`trivy-fs.sh`)      |
| Container image scanning                   | Trivy Action                       | Trivy (`trivy-image.sh`)        |
| Code quality                               | SonarCloud + shared `sonar.sh`     | SonarCloud + shared `sonar.sh`  |
| Build                                      | Maven + shared `test.sh`           | Maven + shared `test.sh`   |
| Docker build                               | Shared `docker-build.sh`           | Shared `docker-build.sh`   |
| Docker publish                             | Native workflow steps              | Shared `docker-push.sh`    |
| Functional tests                           | Newman                             | Newman                     |
| Performance tests                          | K6                                 | K6                         |
| Smoke tests                                | Docker                             | Docker                     |
| Automatic promotion                        | GitHub workflow                    | GitLab pipeline            |
| Reusable logic                             | Composite Actions + shared scripts | Templates + shared scripts |

Both CI/CD implementations provide an equivalent validation process while using the native capabilities of each platform. Shared shell scripts are reused whenever possible to keep the build logic consistent across GitHub Actions and GitLab CI.

### Tools

| Category            | Tool       |
| ------------------- | ---------- |
| Build               | Maven      |
| Formatting          | Spotless   |
| Code Quality        | SonarCloud |
| SAST                | Semgrep    |
| Dependency Scanning | Trivy      |
| Containerisation    | Docker     |
| Functional Testing  | Newman     |
| Performance Testing | K6         |
| Database            | PostgreSQL |
| CI/CD               | GitLab CI  |
