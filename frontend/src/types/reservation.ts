/**
 * TypeScript interfaces for Reservation-related data structures.
 * 
 * This file contains type definitions for:
 * - Reservation details and status
 * - Booking requests and confirmations
 * - Payment information
 */

export interface Reservation {
  id: number;
  userId: number;
  courtId: number;
  venueId: number;
  date: string;
  startTime: string;
  endTime: string;
  duration: number; // in hours
  totalPrice: number;
  status: ReservationStatus;
  paymentStatus: PaymentStatus;
  createdAt: string;
  updatedAt: string;
  court?: {
    name: string;
    sportType: string;
  };
  venue?: {
    name: string;
    address: string;
  };
}

export type ReservationStatus = 
  | 'pending' 
  | 'confirmed' 
  | 'cancelled' 
  | 'completed' 
  | 'no_show';

export type PaymentStatus = 
  | 'pending' 
  | 'paid' 
  | 'failed' 
  | 'refunded';

export interface CreateReservationRequest {
  courtId: number;
  date: string;
  startTime: string;
  endTime: string;
  userId: number;
}

export interface ReservationUpdateRequest {
  date?: string;
  startTime?: string;
  endTime?: string;
}

export interface ReservationSummary {
  totalReservations: number;
  upcomingReservations: number;
  completedReservations: number;
  cancelledReservations: number;
}

