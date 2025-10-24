# Nadi Backend

This is the backend service for Nadi, a sports court reservation platform for Beirut.

## Architecture

The backend follows a clean layered architecture with the following structure:

### Domain Packages

- **`user`** - User account management and authentication
- **`venue`** - Sports venues and court information
- **`reservation`** - Booking system and reservation management
- **`search`** - Advanced search and filtering capabilities

### Layer Structure

Each domain package contains:

- **`model`** - Domain entities and data models
- **`repository`** - Data access layer (JPA repositories)
- **`service`** - Business logic layer
- **`controller`** - REST API endpoints

## Technology Stack

- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database for development
- **Maven** - Build tool

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080/api`

### Database Console

For development, H2 console is available at `http://localhost:8080/api/h2-console`

## API Endpoints

The following REST endpoints are planned (not yet implemented):

### User Management
- `POST /api/users/register` - User registration
- `POST /api/users/login` - User authentication
- `GET /api/users/{userId}` - Get user profile
- `PUT /api/users/{userId}` - Update user profile
- `DELETE /api/users/{userId}` - Delete user account

### Venue Management
- `GET /api/venues/search/location` - Search venues by location
- `GET /api/venues/search/sport` - Search venues by sport type
- `GET /api/venues/{venueId}` - Get venue details
- `GET /api/venues/{venueId}/courts/available` - Get available courts

### Reservation Management
- `POST /api/reservations` - Create reservation
- `GET /api/reservations/user/{userId}` - Get user reservations
- `GET /api/reservations/{reservationId}` - Get reservation details
- `PUT /api/reservations/{reservationId}` - Modify reservation
- `DELETE /api/reservations/{reservationId}` - Cancel reservation

### Search
- `POST /api/search/venues` - Advanced venue search
- `GET /api/search/venues/nearby` - Find nearby venues
- `GET /api/search/venues/sport` - Search by sport type
- `GET /api/search/venues/available` - Search available venues

## Development Notes

- All service methods currently throw `UnsupportedOperationException` as placeholders
- Database configuration uses H2 in-memory database for development
- CORS is configured for frontend integration on `http://localhost:3000`
- All endpoints include comprehensive TODO comments for future implementation

## Next Steps

1. Implement domain models with JPA annotations
2. Add database schema and relationships
3. Implement business logic in service classes
4. Add validation and error handling
5. Implement authentication and authorization
6. Add comprehensive testing

