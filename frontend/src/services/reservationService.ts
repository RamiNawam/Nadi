import type {
  Reservation,
  CreateHoldRequest,
  ConfirmReservationRequest,
  CancelReservationRequest
} from '../types/api';

/**
 * Reservation service for booking flow
 */
export class ReservationService {
  /**
   * Create a hold on a court
   */
  async createHold(_request: CreateHoldRequest): Promise<Reservation> {
    // TODO: Implement API call
    // return apiClient.post<Reservation>('/reservations/hold', request);
    throw new Error('Not implemented yet');
  }

  /**
   * Confirm a held reservation
   */
  async confirmReservation(_reservationId: string, _request: ConfirmReservationRequest): Promise<Reservation> {
    // TODO: Implement API call
    // return apiClient.post<Reservation>(`/reservations/${reservationId}/confirm`, request);
    throw new Error('Not implemented yet');
  }

  /**
   * Cancel a reservation
   */
  async cancelReservation(_reservationId: string, _request: CancelReservationRequest): Promise<Reservation> {
    // TODO: Implement API call
    // return apiClient.post<Reservation>(`/reservations/${reservationId}/cancel`, request);
    throw new Error('Not implemented yet');
  }

  /**
   * Get user's reservations
   */
  async getMyReservations(_userId: string): Promise<Reservation[]> {
    // TODO: Implement API call
    // return apiClient.get<Reservation[]>(`/reservations/my?userId=${userId}`);
    throw new Error('Not implemented yet');
  }
}

export const reservationService = new ReservationService();