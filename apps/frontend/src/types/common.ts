/**
 * TypeScript interfaces for API responses and common data structures.
 * 
 * This file contains type definitions for:
 * - Generic API response wrappers
 * - Error handling
 * - Pagination
 * - Common utility types
 */

export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface ApiError {
  message: string;
  code?: string;
  details?: Record<string, any>;
}

export interface PaginatedResponse<T> {
  data: T[];
  pagination: {
    page: number;
    limit: number;
    total: number;
    totalPages: number;
  };
}

export interface SearchResponse<T> {
  results: T[];
  totalCount: number;
  filters: Record<string, any>;
}

export interface Location {
  latitude: number;
  longitude: number;
  address?: string;
  city?: string;
}

export interface TimeSlot {
  start: string;
  end: string;
  isAvailable: boolean;
}

export interface PriceRange {
  min: number;
  max: number;
  currency: string;
}

export interface FilterOptions {
  sportTypes: string[];
  priceRanges: PriceRange[];
  amenities: string[];
  locations: string[];
}

