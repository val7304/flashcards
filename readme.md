# Flashcards Application

[![CI/CD](https://github.com/val7304/flashcards/actions/workflows/main.yml/badge.svg)](https://github.com/val7304/flashcards/actions/workflows/main.yml)
[![Docker Image](https://img.shields.io/docker/v/valeriejeanne/flashcards?sort=semver)](https://hub.docker.com/r/valeriejeanne/flashcards)

## Description

Flashcards is a Java Spring Boot application for managing flashcards and their categories based on DevOps technologies and one-liner scripts.
It was designed as a demonstration project to practise and illustrate DevOps concepts:

## Features:

* Full CRUD on Categories and Flashcards
* Simple, extensible and testable REST API
* Automatic data injection via data.sql
* Unit and integration tests

## Technologies used
* Java 17
* Spring Boot 3
* Maven
* PostgreSQL 
* Tests: JUnit 5, Mockito, Spring Test

## Project structure
```text
src/
 ├─ main/
 │   ├─ java/com/example/flashcards/
 │   │   ├─ controller/       # REST controllers
 │   │   ├─ service/          # Business services
 │   │   ├─ model/            # Entities
 │   │   ├─ dto/              # DTO for API exchanges
 │   │   └─ repository/       # JPA interfaces
 │   └─ resources/
 │       └─ application.properties
 └─ test/
     ├─ controller/           # Unit tests for controllers
     ├─ service/              # Unit tests for services
     └─ integration/          # Integration tests
```

## Installation
#### 1. Clone the project
```sh
git clone https://github.com/val7304/flashcards.git
cd flashcards
```

#### 2. Change password for user in postgres: 
a. Rename src/main/resources/application-example.properties to application.properties  
b. Modify user and password in application.properties; user must be “postgres”:
```sh
spring.datasource.url=jdbc:postgresql://localhost:5432/flashcardsdb
spring.datasource.username=postgres
spring.datasource.password=secret
```

#### 3. Create the flashcardsdb database as mentionned in application.properties
```sh
./init-db.sh
```

#### 3. Build & Tests
```sh
./mvnw clean package
./mvnw test
```

### Tests
- Unit services and controllers: ./mvn test or ./mvnw test
- REST API integration: tests with MockMvc and Spring Boot Test

### Example of a common error:
integration test:
Expected status 204 for a deletion but code 200 returned → adjust the controller or tests.

#### 4. Launch the application
```sh
./mvnw clean install
./mvnw spring-boot:run
```

### Access the application via browser or Postman or curl - your choice
```sh
http://localhost:8080/api/categories
http://localhost:8080/api/flashcards
```

### data.sql
The data.sql file creates and injects data automatically when the application starts to run.
Located file:  flashcards\src\main\resources\data.sql

It contains: 
#### 5 categories: 
	* ('Bash one-liner'),
	* ('Kubernetes one-liner'),
	* ('Git short-liner'),
	* ('Keytool one-liner'),
	* ('Vagrant one-liner');

#### 5x5 flashcards covering the categories: 
example: 
```sh
	* -- flashcard Bash one-liner (category_id = 1)
	INSERT INTO flashcard (question, answer, category_id) VALUES
	('Version of redhat used', 'cat /etc/redhat-release', 1),
	('See the File System info', 'df -h .', 1),
```

### endpoints available:
```sh
# get all flashcards
GET     /api/flashcards
# search a word (branch) in question
GET     /api/flashcards/search?question={branch}
# get flashcard by id
GET     /api/flashcards/{id}
# add flashcard
POST    /api/flashcards
# modify flashcards by id
PUT     /api/flashcards/{id}
# delete flashcards by id
DELETE  /api/flashcards/{id}

# get all category
GET     /api/categories
# get category by a part of word located in name field
GET     /api/categories/search?name={Bash}
# get category by id
GET     /api/categories/{id}
# add category (id auto)
POST    /api/categories
# modify category by id
PUT     /api/categories/{id}
# delete category by id
DELETE  /api/categories/{id}
```

### Scenario:

#### Test all the endpoints for flashcards
```sh
# get all flashcards
GET     /api/flashcards

# add flashcard
POST    /api/flashcards
body: 
{
  "question": "test ADD flashcard",
  "answer": "ADD test flashcard",
  "category": {
    "id": 1
  }
}

# search a word (test ADD flashcard) in question to get the new_ID created
GET     /api/flashcards/search?question={test}

# or get flashcard by id OR get all flashcards
GET     /api/flashcards/{new_ID}

# delete flashcards by id
DELETE  /api/flashcards/{new_ID}

# get all flashcards to check 

```