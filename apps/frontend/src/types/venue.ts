/**
 * TypeScript interfaces for Venue-related data structures.
 * 
 * This file contains type definitions for:
 * - Venue information and details
 * - Court specifications and availability
 * - Location and contact information
 */

export interface Venue {
  id: number;
  name: string;
  description: string;
  address: string;
  city: string;
  latitude: number;
  longitude: number;
  phoneNumber?: string;
  email?: string;
  website?: string;
  operatingHours: OperatingHours;
  amenities: string[];
  courts: Court[];
  createdAt: string;
  updatedAt: string;
}

export interface Court {
  id: number;
  venueId: number;
  name: string;
  sportType: SportType;
  surfaceType: string;
  dimensions: string;
  hourlyRate: number;
  isIndoor: boolean;
  isAvailable: boolean;
  amenities: string[];
}

export interface OperatingHours {
  monday: TimeSlot;
  tuesday: TimeSlot;
  wednesday: TimeSlot;
  thursday: TimeSlot;
  friday: TimeSlot;
  saturday: TimeSlot;
  sunday: TimeSlot;
}

export interface TimeSlot {
  open: string;
  close: string;
  isClosed: boolean;
}

export type SportType = 'football' | 'basketball' | 'tennis' | 'padel';

export interface VenueSearchCriteria {
  sportType?: SportType;
  location?: {
    latitude: number;
    longitude: number;
    radius: number;
  };
  priceRange?: {
    min: number;
    max: number;
  };
  amenities?: string[];
  date?: string;
  timeSlot?: string;
}

