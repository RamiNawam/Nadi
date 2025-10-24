# Nadi - Sports Court Reservation Platform

A web app for booking sports courts in Beirut. Users can find venues, check availability, and make reservations for football, basketball, padel, and tennis courts.

## Project Structure

```
nadi/
├── backend/                 # Spring Boot API
│   ├── src/main/java/com/nadi/
│   │   ├── controller/      # REST endpoints
│   │   ├── model/          # JPA entities
│   │   ├── repository/     # Data access
│   │   ├── service/        # Business logic
│   │   └── MainApplication.java
│   ├── pom.xml
│   └── README.md
├── frontend/               # React frontend
│   ├── src/
│   │   ├── components/     # UI components
│   │   ├── pages/         # Page components
│   │   ├── services/      # API calls
│   │   └── types/         # TypeScript types
│   ├── package.json
│   └── README.md
├── .gitignore
├── README.md
└── LICENSE
```

## Getting Started

### Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

API runs on `http://localhost:8080/api`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:3000`

## Tech Stack

**Backend:**
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (dev)
- Maven

**Frontend:**
- React 18
- TypeScript
- Vite
- Axios

## Current Status

**Done:**
- Project setup
- Database entities
- Basic API structure
- Frontend components

**Todo:**
- User auth
- Venue search
- Booking system
- Payment integration

## Development Notes

The backend uses a layered architecture with controllers, services, repositories, and models. All service methods currently throw `UnsupportedOperationException` - they need to be implemented.

The frontend has TypeScript interfaces defined but the service methods aren't implemented yet.

Database uses H2 in-memory for development. CORS is configured for local development.

## Contributing

1. Fork the repo
2. Create a feature branch
3. Make changes
4. Submit a PR

## License

MIT License
