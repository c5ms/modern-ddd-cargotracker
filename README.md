# DDDSample
[![Java CI with Maven](https://github.com/c5ms/ddd-sample-cargotracker/actions/workflows/maven.yml/badge.svg)](https://github.com/c5ms/ddd-sample-cargotracker/actions/workflows/maven.yml)


## Technic stack
- Spring boot 3
- JPA (Spring Data JPA)
- JMS (Spring Activemq)

## Features
- Provide **strategy design pattern** for handing report, support: ThreadPool, MessageQueue, Directly see https://java-design-patterns.com/patterns/strategy/
- Utilize BDD test on application layer. see https://livebook.manning.com/book/bdd-in-action/chapter-10
- Delete-free on each layer, e.g., you can free to delete the folder of interface layer without safely. see http://www.javapractices.com/topic/TopicAction.do?Id=205
- Separate domain layer into a single jar, it will protect you to not use the class outside the domain layer incidentally
- Utilize Command-and-Query-Responsibility-Segregation to separate the write and read processing.

## Domain architecture
![domain_architecture.png](src/uml/domain_architecture.png)

## Get Started

```shell
mvn package
cd target
tar -xf cargotracker-application-1.0.0.tar.gz
cd cargotracker-application-1.0.0
./bin/run
```

Open http://127.0.0.1:9000/swagger-ui/index.html you will see the open api

![open-api.png](src/img/open-api.png)

