# Spring Cloud Component Descriptions

| Component | Description |
|-----------|-------------|
| **Eureka Server** | A service registry that allows microservices to register themselves and discover other registered services. (`spring-cloud-netflix Eureka Server`) |
| **Eureka Discovery Client** | A REST-based client that enables a microservice to register itself with a Eureka Server and look up other services for load-balanced communication and failover. |
| **Spring Cloud Gateway** | An API gateway built on Spring MVC that routes requests to backend services and adds cross-cutting features like security, monitoring, and resiliency. |
| **Config Server** | A central configuration service that serves application settings from Git, SVN, or HashiCorp Vault to all microservices in a distributed system. |
| **Config Client** | A client that connects to a Spring Cloud Config Server at startup to fetch and apply the application's external configuration. |
