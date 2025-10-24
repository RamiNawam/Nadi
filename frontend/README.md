# Nadi Frontend

This is the frontend application for Nadi, a sports court reservation platform for Beirut.

## Architecture

The frontend follows a modular React + TypeScript architecture with the following structure:

### Directory Structure

- **`src/components`** - Reusable UI components
- **`src/pages`** - Page-level components and routes
- **`src/services`** - API service layer for backend communication
- **`src/types`** - TypeScript type definitions and interfaces
- **`src/utils`** - Utility functions and helpers

### Technology Stack

- **React 18** - UI library
- **TypeScript** - Type safety and development experience
- **Vite** - Build tool and development server
- **React Router** - Client-side routing
- **Axios** - HTTP client for API calls
- **ESLint + Prettier** - Code quality and formatting

## Getting Started

### Prerequisites

- Node.js 18 or higher
- npm or yarn package manager

### Installation

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Copy environment variables:
   ```bash
   cp env.example .env
   ```

4. Update `.env` file with your configuration:
   ```env
   VITE_API_BASE_URL=http://localhost:8080/api
   VITE_APP_NAME=Nadi
   ```

### Development

Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:3000`

### Building for Production

Build the application:
```bash
npm run build
```

Preview the production build:
```bash
npm run preview
```

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint
- `npm run lint:fix` - Fix ESLint issues
- `npm run format` - Format code with Prettier

## Project Structure

### Components

- **`Layout`** - Main application layout with header and footer
- **`HomePage`** - Landing page with welcome message

### Services

- **`userService`** - User authentication and profile management
- **`venueService`** - Venue search and discovery
- **`reservationService`** - Reservation creation and management

### Types

- **`user.ts`** - User-related type definitions
- **`venue.ts`** - Venue and court type definitions
- **`reservation.ts`** - Reservation type definitions
- **`common.ts`** - Common utility types

## Features (Planned)

### Current Implementation
- ✅ Basic project structure
- ✅ Welcome page with sports categories
- ✅ Responsive layout with navigation
- ✅ TypeScript type definitions
- ✅ API service layer structure

### Future Features
- 🔄 User authentication and registration
- 🔄 Venue search and filtering
- 🔄 Court availability checking
- 🔄 Reservation booking system
- 🔄 User profile management
- 🔄 Payment integration
- 🔄 Real-time notifications

## API Integration

The frontend is configured to communicate with the backend API running on `http://localhost:8080/api`. All API calls are handled through service classes that provide:

- Automatic token management
- Error handling
- Type-safe responses
- Request/response interceptors

## Development Notes

- All service methods are structured but not yet implemented
- Components include placeholder functionality
- TypeScript interfaces are defined for all data structures
- ESLint and Prettier are configured for code quality
- Vite provides fast development and build processes

## Next Steps

1. Implement user authentication flow
2. Create venue search and discovery pages
3. Build reservation booking interface
4. Add user profile management
5. Implement responsive design improvements
6. Add comprehensive error handling
7. Write unit and integration tests

