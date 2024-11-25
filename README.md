# DDDSample 

## Technic stack
- Spring boot 3
- JPA (Spring Data JPA)
- JMS (Spring Activemq)


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

