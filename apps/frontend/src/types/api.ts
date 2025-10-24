/**
 * TypeScript interfaces matching backend entities
 */

export interface User {
  id: string;
  name: string;
  email: string;
  phone: string;
  role: UserRole;
  createdAt: string;
}

export enum UserRole {
  USER = 'USER',
  VENUE_OWNER = 'VENUE_OWNER',
  DEVELOPER = 'DEVELOPER',
  SUPERADMIN = 'SUPERADMIN'
}

export interface Venue {
  id: string;
  name: string;
  phone: string;
  address: string;
  city: string;
  country: string;
  location: {
    type: 'Point';
    coordinates: [number, number]; // [longitude, latitude]
  };
  openingHours?: Record<string, string>;
  ownerId: string;
  createdAt: string;
}

export interface Sport {
  id: string;
  name: SportType;
  minPlayers: number;
  maxPlayers: number;
  defaultSlotMinutes: number;
}

export enum SportType {
  FOOTBALL = 'FOOTBALL',
  BASKETBALL = 'BASKETBALL',
  PADEL = 'PADEL',
  TENNIS = 'TENNIS'
}

export interface Court {
  id: string;
  venueId: string;
  sportId: string;
  name: string;
  surface?: string;
  maxPlayers: number;
  isActive: boolean;
}

export interface CourtPriceRule {
  id: string;
  courtId: string;
  dayOfWeek: number; // 0-6 (Sunday-Saturday)
  startTime: string; // HH:mm format
  endTime: string; // HH:mm format
  pricePerSlot: number;
  currency: string;
}

export interface Reservation {
  id: string;
  userId: string;
  courtId: string;
  startTime: string;
  endTime: string;
  playersCount: number;
  priceTotal: number;
  currency: string;
  status: ReservationStatus;
  createdAt: string;
  holdExpiresAt?: string;
}

export enum ReservationStatus {
  HELD = 'HELD',
  CONFIRMED = 'CONFIRMED',
  CANCELLED = 'CANCELLED',
  EXPIRED = 'EXPIRED'
}

export interface ApiKey {
  id: string;
  userId: string;
  key: string;
  rateLimit: number;
  createdAt: string;
  lastUsed?: string;
}

// API Request/Response DTOs
export interface SearchVenuesRequest {
  lat?: number;
  lon?: number;
  radius?: number;
  sport?: SportType;
  minPrice?: number;
  maxPrice?: number;
  players?: number;
}

export interface CreateHoldRequest {
  courtId: string;
  startTime: string;
  endTime: string;
  userId: string;
  playersCount: number;
}

export interface ConfirmReservationRequest {
  userId: string;
}

export interface CancelReservationRequest {
  userId: string;
  reason?: string;
}

export interface CreateVenueRequest {
  name: string;
  phone: string;
  address: string;
  latitude: number;
  longitude: number;
}

export interface CreateCourtRequest {
  venueId: string;
  sportId: string;
  name: string;
  surface?: string;
  maxPlayers: number;
}

export interface PricingRuleRequest {
  dayOfWeek: number;
  startTime: string;
  endTime: string;
  pricePerSlot: number;
}

export interface GenerateApiKeyRequest {
  userId: string;
  rateLimit?: number;
}

export interface RotateApiKeyRequest {
  userId: string;
  oldKey: string;
}

// API Response wrapper
export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data?: T;
  errors?: string[];
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}
