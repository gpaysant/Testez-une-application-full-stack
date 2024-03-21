# Yoga App
With this application, the users can create and participate in yoga sessions. 

This contains front-end and back-end development.

The purpose of this application is to create unit tests, integration tests and end-to-end.

More precisely : 
* Jest for front-end tests
* JUnit 5 for back-end tests
* Cypress for end-to-end tests

## Getting Started

### Prerequisites
* Maven
* Java 11
* Angular 14
* NodeJS 16
* MySQL

## Install Database 

1. Create Database
  ```sh
  CREATE DATABASE test;
  ```
2. Run script SQL 
  ```sh
  USE test;
  SOURCE C:/[FOLDER_OF_APPLICATION_TEST]/ressources/sql/script.sql
  ```
3. Configure informations for your database in applications.properties 
  ```sh
  spring.datasource.url=jdbc:mysql://localhost:3306/test?allowPublicKeyRetrieval=true
  spring.datasource.username=[YOUR_USERNAME]
  spring.datasource.password=[YOUR_PASSWORD]
  ```

## Install the project

1. API Application

* Go to API folder
  ```sh
  cd back
  ```

* Install all dependencies and create jar executable :
  ```sh
  mvn clean install
  ```

* Run API project :
  ```sh
  java -jar api-0.0.1-SNAPSHOT.jar
  ```


2. Angular application

* Go to angular folder
  ```sh
  cd front
  ```

* Install NPM packages
  ```sh
  npm install
  ```

* Builds and serves application
  ```sh
  npm run start
  ```


## Running tests
Run these commands to run tests and generate report coverage 

1. With Jest
  ```sh
  npm run test
  ```
> Generate coverage report here : front/coverage/jest/lcov-report/index.html

2. With Cypress

Run this command before the next one
  ```sh
  npm run e2e
  ```
  ```sh
  npm run e2e:coverage
  ```
> Generate coverage report here : front/coverage/lcov-report/index.html

3. With JUnit 5
  ```sh
  mvn clean test
  ```
> Generate coverage report here : back/target/site/jacoco/index.html


## Language/Framework
* Mysql/Java/Lombok/Spring boot/Spring Security(JWT authentication)
* Cypress/Jest/JUnit5
* Postman

<!-- CONTACT -->
## Contact

Paysant Gérald - geraldpaysant@gmail.com

Project Link : [https://github.com/gpaysant/Testez-une-application-full-stack](https://github.com/gpaysant/Testez-une-application-full-stack)
