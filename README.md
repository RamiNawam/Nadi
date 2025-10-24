# Nadi - Sports Court Reservation Platform

Nadi is a full-stack web application that helps users in Beirut reserve sports courts (football, basketball, padel, tennis), find nearby venues, and manage their accounts.

## 🏗️ Project Structure

```
nadi/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/com/nadi/
│   │   ├── user/           # User management
│   │   ├── venue/          # Venue and court management
│   │   ├── reservation/    # Booking system
│   │   ├── search/         # Search functionality
│   │   └── MainApplication.java
│   ├── pom.xml
│   └── README.md
├── frontend/               # React + TypeScript frontend
│   ├── src/
│   │   ├── components/     # UI components
│   │   ├── pages/         # Page components
│   │   ├── services/      # API services
│   │   └── types/         # TypeScript interfaces
│   ├── package.json
│   └── README.md
├── .gitignore
├── README.md
└── LICENSE
```

## 🚀 Quick Start

### Backend (Spring Boot)

1. Navigate to backend directory:
   ```bash
   cd backend
   ```

2. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. Backend will be available at `http://localhost:8080/api`

### Frontend (React + TypeScript)

1. Navigate to frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start development server:
   ```bash
   npm run dev
   ```

4. Frontend will be available at `http://localhost:3000`

## 🛠️ Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database for development
- **Maven** - Build tool

### Frontend
- **React 18** - UI library
- **TypeScript** - Type safety
- **Vite** - Build tool and dev server
- **React Router** - Client-side routing
- **Axios** - HTTP client
- **ESLint + Prettier** - Code quality

## 📋 Features (Planned)

### Current Status
- ✅ Project structure setup
- ✅ Backend layered architecture
- ✅ Frontend component structure
- ✅ TypeScript type definitions
- ✅ API service layer
- ✅ Basic welcome page

### Upcoming Features
- 🔄 User authentication and registration
- 🔄 Venue search and discovery
- 🔄 Court availability checking
- 🔄 Reservation booking system
- 🔄 User profile management
- 🔄 Payment integration
- 🔄 Real-time notifications

## 🏛️ Architecture

### Backend Architecture
The backend follows a clean layered architecture:

- **Model Layer** - Domain entities and data models
- **Repository Layer** - Data access using JPA interfaces
- **Service Layer** - Business logic (currently placeholder methods)
- **Controller Layer** - REST API endpoints (currently placeholder mappings)

### Frontend Architecture
The frontend uses a modular component-based architecture:

- **Components** - Reusable UI components
- **Pages** - Page-level components and routing
- **Services** - API communication layer
- **Types** - TypeScript interfaces for type safety

## 🔧 Development

### Backend Development
- All service methods currently throw `UnsupportedOperationException`
- Database uses H2 in-memory for development
- CORS configured for frontend integration
- Comprehensive TODO comments for implementation guidance

### Frontend Development
- Service methods structured but not implemented
- TypeScript interfaces defined for all data structures
- ESLint and Prettier configured for code quality
- Vite provides fast development experience

## 📚 Documentation

- [Backend README](backend/README.md) - Detailed backend documentation
- [Frontend README](frontend/README.md) - Detailed frontend documentation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🎯 Next Steps

1. Implement domain models with JPA annotations
2. Add database schema and relationships
3. Implement business logic in service classes
4. Add validation and error handling
5. Implement authentication and authorization
6. Create comprehensive test suites
7. Add CI/CD pipeline
8. Deploy to production environment

