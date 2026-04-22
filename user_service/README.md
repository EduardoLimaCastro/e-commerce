# User Service

Microservice responsible for managing users in the e-commerce platform. Built with Clean Architecture (Hexagonal / Ports & Adapters), separating domain logic from infrastructure concerns.

## Architecture

```
src/main/java/com/eduardocastro/user_service/
├── domain/                        # Enterprise business rules (no dependencies)
│   ├── entity/User.java           # Aggregate root
│   ├── valueobject/               # Email, Phone, Address
│   ├── enums/UserRole.java
│   ├── event/                     # UserCreatedEvent, UserUpdatedEvent
│   ├── exception/
│   └── repository/UserRepository  # Port (interface)
├── application/                   # Application business rules
│   ├── usecase/                   # Input ports (interfaces)
│   └── interactor/                # Use case implementations
└── infrastructure/                # Adapters
    ├── persistence/               # JPA adapter + Flyway migrations
    └── web/                       # REST controllers + DTOs
```

## Tech Stack

- **Java 21** + **Spring Boot 3.5**
- **PostgreSQL 15** — primary database
- **Flyway** — database migrations
- **Spring Data JPA** — persistence
- **Lombok** — boilerplate reduction
- **JUnit 5** + **Mockito** — unit & slice tests

## API Endpoints

Base URL: `http://localhost:8081/api/users`

| Method | Path         | Description         | Status codes    |
|--------|--------------|---------------------|-----------------|
| POST   | `/`          | Create a user       | 201 / 400       |
| GET    | `/`          | List all users      | 200             |
| GET    | `/{id}`      | Get user by ID      | 200 / 404       |
| PUT    | `/{id}`      | Update a user       | 200 / 404       |

### Request body (POST / PUT)

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "phone": "11987654321",
  "email": "john@example.com",
  "address": {
    "street": "Rua das Flores",
    "number": "123",
    "complement": "Apto 4",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01310100"
  },
  "role": "USER"
}
```

**Field rules:**
- `phone` — 11 digits (DDD + number), no symbols
- `email` — standard email format, stored lowercase
- `state` — 2 uppercase letters (e.g. `SP`, `RJ`)
- `zipCode` — 8 digits, no dashes (e.g. `01310100`)
- `role` — one of `ADMIN`, `USER`, `COORDINATOR`, `SUPERVISOR`
- `complement` — optional

### Response body

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "11987654321",
  "email": "john@example.com",
  "address": {
    "street": "Rua das Flores",
    "number": "123",
    "complement": null,
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01310100"
  },
  "role": "USER",
  "createdAt": "2024-01-01T12:00:00",
  "updatedAt": "2024-01-01T12:00:00"
}
```

## Running Locally

### Prerequisites

- Java 21
- Docker + Docker Compose
- Maven 3.9+

### Database only (recommended for development)

Start PostgreSQL on port `5433`:

```bash
docker-compose up postgres -d
```

Then run the service:

```bash
mvn spring-boot:run
```

The application starts on **port 8081**. Flyway applies migrations automatically on startup.

### Full stack with Docker

```bash
docker-compose up --build
```

### Database configuration (`application.yml`)

```
url:      jdbc:postgresql://localhost:5433/user_service
username: postgres
password: password
```

## Running Tests

Unit and slice tests (no database required):

```bash
mvn test -Dtest="!UserServiceApplicationTests"
```

All tests including context load (requires PostgreSQL running):

```bash
mvn test
```

### Test coverage

| Layer                  | Test class                    | Type       |
|------------------------|-------------------------------|------------|
| Domain — `User`        | `UserTest`                    | Unit       |
| Domain — `Email`       | `EmailTest`                   | Unit       |
| Domain — `Phone`       | `PhoneTest`                   | Unit       |
| Domain — `Address`     | `AddressTest`                 | Unit       |
| Use case — Create      | `CreateUserInteractorTest`    | Unit       |
| Use case — GetById     | `GetUserByIdInteractorTest`   | Unit       |
| Use case — List        | `ListUsersInteractorTest`     | Unit       |
| Use case — Update      | `UpdateUserInteractorTest`    | Unit       |
| Web mapper             | `UserWebMapperTest`           | Unit       |
| JPA mapper             | `UserJpaMapperTest`           | Unit       |
| REST controller        | `UserControllerTest`          | Slice (MockMvc) |

## Database Migrations

Managed by Flyway under `src/main/resources/db/migration/`:

| Version | Description                        |
|---------|------------------------------------|
| V1      | Create `users` table               |
| V2      | Add `email` and `role` columns     |
| V3      | Add `phone` column                 |
| V4      | Add address columns (embedded)     |
