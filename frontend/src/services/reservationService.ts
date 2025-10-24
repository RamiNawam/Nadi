import axios, { AxiosInstance } from 'axios';
import { 
  Reservation, 
  CreateReservationRequest, 
  ReservationUpdateRequest,
  ReservationSummary 
} from '@/types/reservation';

/**
 * ReservationService handles all API calls related to reservation management.
 * 
 * This service provides methods for:
 * - Creating and managing reservations
 * - Checking availability
 * - Cancellation and modification
 */
class ReservationService {
  private api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Add request interceptor to include auth token
    this.api.interceptors.request.use((config) => {
      const token = localStorage.getItem('authToken');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    });
  }

  /**
   * Create a new reservation
   */
  async createReservation(reservationData: CreateReservationRequest): Promise<Reservation> {
    const response = await this.api.post('/reservations', reservationData);
    return response.data;
  }

  /**
   * Get user's reservations
   */
  async getUserReservations(userId: number): Promise<Reservation[]> {
    const response = await this.api.get(`/reservations/user/${userId}`);
    return response.data;
  }

  /**
   * Get reservation details by ID
   */
  async getReservationDetails(reservationId: number): Promise<Reservation> {
    const response = await this.api.get(`/reservations/${reservationId}`);
    return response.data;
  }

  /**
   * Update an existing reservation
   */
  async updateReservation(
    reservationId: number,
    updateData: ReservationUpdateRequest
  ): Promise<Reservation> {
    const response = await this.api.put(`/reservations/${reservationId}`, updateData);
    return response.data;
  }

  /**
   * Cancel a reservation
   */
  async cancelReservation(reservationId: number): Promise<void> {
    await this.api.delete(`/reservations/${reservationId}`);
  }

  /**
   * Get reservation summary for a user
   */
  async getReservationSummary(userId: number): Promise<ReservationSummary> {
    const response = await this.api.get(`/reservations/user/${userId}/summary`);
    return response.data;
  }
}

export default new ReservationService();

