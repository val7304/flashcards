name: Build and Test Flashcards App
on:
  push:
    branches: [ main ]
  pull_request:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Cache Maven dependencies
        uses: actions/cache@v4
        with:
          path: ~/.m2
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
          restore-keys: |
            ${{ runner.os }}-maven-

      - name: Build
        run: ci-scripts/build.sh

      - name: Run tests
        run: ci-scripts/test.sh

      # Optionnel: Build Docker image si Dockerfile présent
      - name: Build Docker image
        run: ci-scripts/docker-build.sh
        if: success() && hashFiles('Dockerfile') != ''
