# Nadi - Sports Venue Reservation Platform

A monorepo for a sports court reservation app in Beirut. Users can discover venues, check availability, and make reservations for football, basketball, padel, and tennis courts.

## Repository Structure

```
nadi/
├── apps/
│   ├── backend/        # Spring Boot + MongoDB API
│   └── frontend/       # React + TypeScript frontend
├── docs/
│   └── uml/           # PlantUML diagrams
├── scripts/
│   ├── mongo-init.js  # MongoDB initialization
│   └── sample-requests.http # API examples
├── docker-compose.yml # MongoDB for local dev
├── .gitignore
└── README.md
```

## Quick Start

### 1. Start MongoDB
```bash
docker compose up -d
```

### 2. Start Backend
```bash
cd apps/backend
mvn spring-boot:run
```
API runs on `http://localhost:8080/api`

### 3. Start Frontend
```bash
cd apps/frontend
npm install
npm run dev
```
Frontend runs on `http://localhost:3000`

## Tech Stack

**Backend:**
- Java 17 + Spring Boot 3.2.0
- Spring Data MongoDB with geospatial support
- Spring Security (role-based access)
- MongoDB with 2dsphere indexes

**Frontend:**
- React 18 + TypeScript
- Vite build tool
- Axios for API calls

**Infrastructure:**
- MongoDB for data storage
- Docker Compose for local development
- Optional Redis for rate limiting

## Key Features

### Geospatial Search
- Uses MongoDB 2dsphere index on venue locations
- Find venues within radius of user location
- Efficient nearby search queries

### Booking Flow (No Payments)
- **Hold**: Create temporary reservation (15 min expiry)
- **Confirm**: Convert hold to confirmed booking
- **Cancel**: Cancel any reservation
- **Expire**: Automatic cleanup of expired holds

### Role-Based Access
- **User**: Browse, search, book courts
- **Venue Owner**: Manage venues, courts, pricing
- **Developer**: API key management
- **Super Admin**: System administration

## API Endpoints

### Public
- `GET /api/v1/sports` - Available sports
- `GET /api/v1/venues` - Search venues with filters
- `GET /api/v1/venues/{id}` - Venue details
- `GET /api/v1/courts/{id}/availability` - Court availability

### Booking
- `POST /api/v1/reservations/hold` - Create hold
- `POST /api/v1/reservations/{id}/confirm` - Confirm reservation
- `POST /api/v1/reservations/{id}/cancel` - Cancel reservation

### Owner (Requires VENUE_OWNER role)
- `POST /api/v1/owner/venues` - Create venue
- `PATCH /api/v1/owner/venues/{id}` - Update venue
- `POST /api/v1/owner/courts` - Create court
- `POST /api/v1/owner/courts/{id}/price-rules` - Set pricing

### Developer (Requires DEVELOPER role)
- `GET /api/v1/developer/api-key` - Get API keys
- `POST /api/v1/developer/api-key` - Generate API key
- `POST /api/v1/developer/api-key/rotate` - Rotate API key

## Database Design

### Collections
- **users** - User accounts with roles
- **venues** - Sports facilities with geospatial location
- **sports** - Available sports (football, basketball, padel, tennis)
- **courts** - Individual courts within venues
- **court_price_rules** - Pricing by day/time
- **reservations** - Booking records with hold/confirm flow
- **api_keys** - Developer API access

### Indexes
- Geospatial: `venues.location` (2dsphere)
- Unique: `users.email`, `users.phone`, `api_keys.key`
- Compound: `reservations(courtId, startTime, endTime)` for overlap checking
- Performance: `reservations.holdExpiresAt` for cleanup

## Development Notes

### Current Status
- ✅ Repository structure and configuration
- ✅ MongoDB entities with proper annotations
- ✅ Repository interfaces with method signatures
- ✅ Service layer with TODO methods
- ✅ Controller stubs with DTOs
- ✅ Security configuration (placeholder)
- ✅ Docker setup for MongoDB

### Implementation Needed
- Business logic in service methods
- Database queries in repositories
- Authentication and authorization
- Frontend API integration
- Error handling and validation
- Testing

### Race Safety Strategy
- Use MongoDB transactions for booking operations
- Compound index enables efficient overlap checking
- Scheduled job expires holds (not TTL deletion)
- Service-level validation prevents double bookings

## Documentation

- [UML Diagrams](docs/uml/) - System design documentation
- [API Examples](scripts/sample-requests.http) - Sample HTTP requests
- [Backend README](apps/backend/README.md) - Backend details
- [Frontend README](apps/frontend/README.md) - Frontend details

## Contributing

This is a structure-only scaffold. All business logic needs to be implemented.

1. Fork the repository
2. Create a feature branch
3. Implement the TODO methods
4. Add tests
5. Submit a pull request

## License

MIT License - see [LICENSE](LICENSE) file for details.