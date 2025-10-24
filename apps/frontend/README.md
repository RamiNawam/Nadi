# Nadi Frontend

React + TypeScript frontend for the Nadi sports venue reservation platform.

## Tech Stack

- **React 18** - UI library
- **TypeScript** - Type safety
- **Vite** - Build tool and dev server
- **React Router** - Client-side routing
- **Axios** - HTTP client (via custom ApiClient)

## Project Structure

```
src/
├── components/
│   └── Layout.tsx          # Main layout component
├── pages/
│   ├── HomePage.tsx        # Welcome page
│   ├── SearchPage.tsx      # Venue search
│   └── VenuePage.tsx       # Venue details
├── services/
│   ├── ApiClient.ts        # HTTP client wrapper
│   ├── SearchService.ts    # Venue search API calls
│   └── ReservationService.ts # Booking API calls
├── types/
│   └── api.ts             # TypeScript interfaces
├── router.tsx             # Application routing
├── App.tsx               # Main app component
├── main.tsx              # Entry point
└── index.css             # Global styles
```

## Features

### Current Implementation
- ✅ Basic routing structure
- ✅ TypeScript interfaces matching backend
- ✅ API client with error handling
- ✅ Service layer with method signatures
- ✅ Responsive layout component

### TODO Implementation
- 🔄 Venue search with geospatial filters
- 🔄 Court availability display
- 🔄 Booking flow (hold → confirm → cancel)
- 🔄 User authentication
- 🔄 Owner dashboard for venue management
- 🔄 Developer API key management

## API Integration

### Services
- **SearchService** - Venue discovery and search
- **ReservationService** - Booking flow management
- **ApiClient** - HTTP client with error handling

### Type Safety
All API requests and responses are typed using interfaces that match the backend DTOs:
- `User`, `Venue`, `Court`, `Reservation`
- Request/Response DTOs
- API response wrapper

## Development

### Prerequisites
- Node.js 18+
- npm or yarn

### Setup
```bash
# Install dependencies
npm install

# Start development server
npm run dev
```

### Environment Variables
Create `.env.local` file:
```env
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_API_KEY=your_api_key_here
```

### Available Scripts
- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint
- `npm run type-check` - Run TypeScript compiler

## API Client Usage

```typescript
import { searchService } from './services/SearchService';

// Search venues
const venues = await searchService.searchVenues({
  lat: 33.8938,
  lon: 35.5018,
  radius: 5000,
  sport: 'FOOTBALL'
});

// Get venue details
const venue = await searchService.getVenueDetails('venue-id');
```

## Routing

### Current Routes
- `/` - Home page
- `/search` - Venue search
- `/venue/:id` - Venue details

### Future Routes
- `/reservations` - User reservations
- `/profile` - User profile
- `/owner` - Venue owner dashboard
- `/developer` - API key management

## Styling

Currently uses basic CSS. Consider adding:
- CSS Modules or Styled Components
- Tailwind CSS for utility classes
- Component library (Material-UI, Ant Design)

## State Management

Currently uses local component state. Consider adding:
- React Context for global state
- Redux Toolkit for complex state
- React Query for server state

## Testing

```bash
# Run tests (when implemented)
npm run test

# Run tests with coverage
npm run test:coverage
```

## Building for Production

```bash
# Build optimized bundle
npm run build

# Preview production build
npm run preview
```

## Development Status

### Completed
- ✅ Project structure and configuration
- ✅ TypeScript interfaces
- ✅ API client with error handling
- ✅ Service layer with method signatures
- ✅ Basic routing
- ✅ Layout component

### TODO
- Implement API calls in services
- Add venue search UI
- Implement booking flow
- Add user authentication
- Add error handling and loading states
- Write unit tests
- Add responsive design
- Implement state management