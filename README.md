# reactive-streaming-api

![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=egjarabo_reactive-streaming-api&metric=alert_status)
![Coverage](https://sonarcloud.io/api/project_badges/measure?project=egjarabo_reactive-streaming-api&metric=coverage)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)

Reactive REST API built with Spring WebFlux, R2DBC and PostgreSQL.
Part of a backend portfolio targeting Senior Java Engineer roles.

## Tech Stack

- **Java 21** — Virtual Threads, Records, Pattern Matching
- **Spring WebFlux** — Reactive REST API with Project Reactor
- **R2DBC** — Reactive database connectivity
- **PostgreSQL** — Relational database
- **Flyway** — Database migration versioning
- **Testcontainers** — Integration tests with real PostgreSQL
- **JaCoCo** — Code coverage (95%+)
- **ArchUnit** — Architecture rules validation
- **SonarCloud** — Code quality analysis
- **Docker** — Multi-stage image build
- **GitHub Actions** — CI/CD pipeline

## Architecture

Package by feature — all code related to a domain is kept together:
```
com.egjarabo.streaming
├── category        # Category domain
├── product         # Product domain
└── common          # Shared exceptions and utilities
```

## Getting Started

### Prerequisites
- Docker
- Java 21

### Run locally
```bash
# Start PostgreSQL
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

### Run tests
```bash
./mvnw verify
```

## API Endpoints

### Categories
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/categories | Get all categories |
| GET | /api/v1/categories/{id} | Get category by id |
| POST | /api/v1/categories | Create category |
| DELETE | /api/v1/categories/{id} | Delete category |

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/products | Get all products |
| GET | /api/v1/products/{id} | Get product by id |
| GET | /api/v1/products/category/{categoryId} | Get products by category |
| POST | /api/v1/products | Create product |
| PATCH | /api/v1/products/{id}/stock | Update stock |
| DELETE | /api/v1/products/{id} | Delete product |

## Quality

- **95%+** line coverage with JaCoCo
- **22 integration tests** with Testcontainers
- **5 architecture rules** enforced with ArchUnit
- **SonarCloud Quality Gate: Passed**