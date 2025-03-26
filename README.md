### Repository Overview
[![Java CI with Maven](https://github.com/c5ms/ddd-sample-cargotracker/actions/workflows/maven.yml/badge.svg)](https://github.com/c5ms/ddd-sample-cargotracker/actions/workflows/maven.yml)

The `modern-ddd-cargotracker` repository is a Java-based project that implements a cargo tracking system using modern Domain-Driven Design (DDD) principles. It leverages Spring Boot 3, JPA (Spring Data JPA), and JMS (Spring ActiveMQ) to provide a robust and scalable solution.

### Key Features

#### 1. Technology Stack
- **Spring Boot 3**: Serves as the foundation for rapid development and easy deployment.
- **JPA (Spring Data JPA)**: Simplifies database access and management.
- **JMS (Spring ActiveMQ)**: Enables asynchronous communication and event-driven architecture.

#### 2. Design Patterns and Architectural Principles
- **Strategy Design Pattern**: Offers multiple strategies (ThreadPool, MessageQueue, Directly) for handling reports. Refer to [Strategy Design Pattern](https://java-design-patterns.com/patterns/strategy/).
- **Behavior - Driven Development (BDD)**: Implements BDD testing at the application layer. See [BDD in Action](https://livebook.manning.com/book/bdd-in-action/chapter-10).
- **Layer - Safety**: Each layer is designed to be delete - free. For example, you can safely delete the interface layer folder without causing security issues. Refer to [related article](http://www.javapractices.com/topic/TopicAction.do?Id=205).
- **Domain Layer Isolation**: The domain layer is separated into a single JAR file to prevent accidental use of classes outside the domain layer.
- **Command - Query Responsibility Segregation (CQRS)**: Separates write and read operations for better scalability and maintainability.

### Running the Application

```shell
mvn package
cd target
tar -xf cargotracker-application-1.0.0.tar.gz
cd cargotracker-application-1.0.0
./bin/run
```
## Open API

Open http://127.0.0.1:9000/swagger-ui/index.html you will see the open api

![open-api.png](src/img/open-api.png)

## Domain architecture
![domain_architecture.png](src/uml/domain_architecture.png)

### How to Contribute
If you want to contribute to this project, you can fork the repository, create a new branch for your feature or bug fix, and submit a pull request to the `main` branch. Make sure to follow the existing code style and write appropriate tests for your changes.