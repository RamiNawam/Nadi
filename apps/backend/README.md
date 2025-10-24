# Nadi Backend

Spring Boot API for sports venue reservation platform using MongoDB.

## Architecture

### Layered Structure
```
com.nadi/
├── NadiApplication.java     # Main application class
├── config/                  # MongoDB, Security configuration
├── common/                  # DTOs, ApiResponse, pagination
├── auth/                    # Authentication, API keys
├── user/                    # User management
├── venue/                   # Venue management
├── sport/                   # Sport definitions
├── court/                   # Court management
├── pricing/                 # Price calculation
├── reservation/             # Booking flow
└── search/                  # Geospatial search
```

### Package Organization
Each feature package contains:
- `model/` - MongoDB entities with annotations
- `repository/` - Data access interfaces
- `service/` - Business logic (TODO methods)
- `controller/` - REST endpoints (stub implementations)

## MongoDB Configuration

### Connection
```properties
spring.data.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/nadi}
```

### Indexes
- **Geospatial**: `venues.location` (2dsphere) for nearby search
- **Unique**: `users.email`, `users.phone`, `api_keys.key`
- **Compound**: `reservations(courtId, startTime, endTime)` for overlap checking
- **Performance**: `reservations.holdExpiresAt` for cleanup

### Initialization
MongoDB indexes are created automatically via `scripts/mongo-init.js` when using Docker Compose.

## Entities

### User
- Basic user information with role-based access
- Unique email and phone constraints
- Roles: USER, VENUE_OWNER, DEVELOPER, SUPERADMIN

### Venue
- Sports facility with geospatial location
- Uses GeoJsonPoint for MongoDB 2dsphere queries
- Owner relationship for venue management

### Sport
- Available sports: FOOTBALL, BASKETBALL, PADEL, TENNIS
- Player count requirements and default slot duration

### Court
- Individual courts within venues
- Sport type and capacity information
- Active/inactive status

### CourtPriceRule
- Pricing by day of week and time slots
- Flexible pricing structure for different times

### Reservation
- Booking records with hold-confirm-cancel flow
- Compound index for efficient overlap checking
- Hold expiration for automatic cleanup

### ApiKey
- Developer API access with rate limiting
- Key rotation and management

## Booking Flow

### Hold Creation
1. Check for overlapping reservations using compound index
2. Calculate price using pricing rules
3. Create reservation with HELD status
4. Set holdExpiresAt to 15 minutes from now

### Confirmation
1. Verify user ownership
2. Check reservation is still HELD and not expired
3. Update status to CONFIRMED
4. Clear holdExpiresAt field

### Expiration
- Scheduled job runs every minute
- Finds expired holds and updates status to EXPIRED
- Uses indexed query on holdExpiresAt for efficiency

## Security

### Role-Based Access Control
- `@PreAuthorize` annotations on controller methods
- Role hierarchy: USER < VENUE_OWNER < DEVELOPER < SUPERADMIN
- Placeholder implementation - JWT authentication needed

### CORS Configuration
- Configured for frontend on localhost:3000
- Allows all methods and headers for development

## Running the Application

### Prerequisites
- Java 17+
- Maven 3.6+
- MongoDB (via Docker Compose)

### Local Development
```bash
# Start MongoDB
docker compose up -d

# Run application
mvn spring-boot:run
```

### Environment Variables
- `MONGODB_URI` - MongoDB connection string
- `PORT` - Server port (default: 8080)

## API Documentation

### Base URL
`http://localhost:8080/api/v1`

### Authentication
Currently uses basic auth for development. JWT implementation needed for production.

### Sample Requests
See `scripts/sample-requests.http` for example API calls.

## Development Status

### Completed
- ✅ Project structure and configuration
- ✅ MongoDB entities with proper annotations
- ✅ Repository interfaces with method signatures
- ✅ Service layer with TODO methods
- ✅ Controller stubs with DTOs
- ✅ Security configuration (placeholder)
- ✅ Docker setup for MongoDB

### TODO
- Implement business logic in service methods
- Add database queries in repositories
- Implement JWT authentication
- Add comprehensive error handling
- Write unit and integration tests
- Add API documentation (OpenAPI/Swagger)
- Implement rate limiting
- Add logging and monitoring

## Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## Building

```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/nadi-backend-1.0.0.jar
```