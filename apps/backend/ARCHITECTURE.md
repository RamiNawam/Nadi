# Nadi Backend Architecture

## Overview
The Nadi backend follows a **layered architecture** pattern, organized by technical layers rather than by domain.

## Directory Structure

```
com.nadi/
├── config/          # Configuration classes (Security, MongoDB, Web)
├── controller/       # REST API endpoints
├── dto/             # Data Transfer Objects (Request/Response)
├── exception/       # Exception handling
├── model/           # MongoDB document entities
├── repository/      # Data access layer (Spring Data MongoDB)
├── service/         # Business logic layer
└── common/          # Shared DTOs and utilities
```

## Layer Responsibilities

### Controllers (`controller/`)
- Handle HTTP requests/responses
- Validate request DTOs
- Call service layer
- Return response DTOs with appropriate HTTP status codes
- Examples: `UserController`, `VenueController`, `ReservationController`

### Services (`service/`)
- Implement business logic
- Orchestrate repository calls
- Convert entities to DTOs and vice versa
- Handle validation and authorization
- Throw custom exceptions (`NadiException`)
- Examples: `UserService`, `VenueService`, `ReservationService`

### Repositories (`repository/`)
- Interface with MongoDB
- Extend `MongoRepository<T, ID>`
- Define custom query methods
- Examples: `UserRepository`, `VenueRepository`, `CourtRepository`

### Models (`model/`)
- MongoDB document entities
- Use `@Document` annotation
- Define validation rules
- Examples: `User`, `Venue`, `Court`, `Reservation`, `Sport`

### DTOs (`dto/`)
- Data Transfer Objects for API requests/responses
- Separate from domain models
- Request DTOs: Input validation
- Response DTOs: Output structure
- Examples: `UserRequestDto`, `VenueResponseDto`, `ReservationRequestDto`

### Exception Handling (`exception/`)
- Centralized error handling
- `NadiException`: Custom base exception
- `NadiExceptionHandler`: Global `@ControllerAdvice` handler
- Returns consistent error response format

## Data Flow

```
Client Request
    ↓
Controller (receives DTO)
    ↓
Service (business logic, converts DTO ↔ Entity)
    ↓
Repository (database operations)
    ↓
MongoDB
    ↓
Repository returns Entity
    ↓
Service converts to DTO
    ↓
Controller returns DTO response
```

## Key Principles

1. **Separation of Concerns**: Each layer has a single responsibility
2. **DTOs for API**: Controllers never expose entities directly
3. **Business Logic in Services**: Services contain all business rules
4. **Centralized Exception Handling**: All exceptions handled by `NadiExceptionHandler`
5. **Repository Pattern**: Clean data access abstraction
6. **MongoDB Integration**: Spring Data MongoDB for persistence

## Example Flow

### Creating a User:

1. **Client** sends POST request with `UserRequestDto`
2. **UserController** validates and calls `UserService.createUser(dto)`
3. **UserService**:
   - Validates email/phone uniqueness
   - Creates `User` entity
   - Saves via `UserRepository`
   - Converts to `UserResponseDto`
4. **UserController** returns `UserResponseDto` with HTTP 201 Created
5. **If error**: `NadiExceptionHandler` catches and returns error DTO

### Searching for Venues:

1. **Client** sends GET request with location parameters
2. **SearchController** calls `SearchService.searchVenues(...)`
3. **SearchService**:
   - Calls `VenueRepository.findByLocationNear(...)`
   - Converts `Venue` entities to `VenueResponseDto`
4. **SearchController** returns list of `VenueResponseDto`

## Technologies

- **Spring Boot 3.2.0**: Framework
- **Spring Data MongoDB**: Persistence
- **Spring Security**: Authentication/Authorization (currently disabled for development)
- **MongoDB**: Document database
- **Maven**: Build tool

## Development Guidelines

1. Always use DTOs in controllers, never entities
2. Business logic belongs in services, not controllers or repositories
3. Keep controllers thin—they should just delegate to services
4. Use `@Document` for MongoDB entities, not `@Entity`
5. Handle all exceptions with custom exceptions and the global handler
6. Follow RESTful conventions for API design
