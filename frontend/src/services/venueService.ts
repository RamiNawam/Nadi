import axios, { AxiosInstance } from 'axios';
import { Venue, Court, VenueSearchCriteria } from '@/types/venue';

/**
 * VenueService handles all API calls related to venue management.
 * 
 * This service provides methods for:
 * - Venue search and discovery
 * - Court availability checking
 * - Venue details retrieval
 */
class VenueService {
  private api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  /**
   * Search venues by location
   */
  async searchVenuesByLocation(
    latitude: number,
    longitude: number,
    radius: number
  ): Promise<Venue[]> {
    const response = await this.api.get('/venues/search/location', {
      params: { latitude, longitude, radius },
    });
    return response.data;
  }

  /**
   * Search venues by sport type
   */
  async searchVenuesBySport(sportType: string): Promise<Venue[]> {
    const response = await this.api.get('/venues/search/sport', {
      params: { sportType },
    });
    return response.data;
  }

  /**
   * Get venue details by ID
   */
  async getVenueDetails(venueId: number): Promise<Venue> {
    const response = await this.api.get(`/venues/${venueId}`);
    return response.data;
  }

  /**
   * Get available courts for a venue
   */
  async getAvailableCourts(
    venueId: number,
    date: string,
    timeSlot: string
  ): Promise<Court[]> {
    const response = await this.api.get(`/venues/${venueId}/courts/available`, {
      params: { date, timeSlot },
    });
    return response.data;
  }

  /**
   * Advanced venue search with multiple criteria
   */
  async searchVenues(criteria: VenueSearchCriteria): Promise<Venue[]> {
    const response = await this.api.post('/search/venues', criteria);
    return response.data;
  }

  /**
   * Find nearby venues
   */
  async findNearbyVenues(
    latitude: number,
    longitude: number,
    radius: number
  ): Promise<Venue[]> {
    const response = await this.api.get('/search/venues/nearby', {
      params: { latitude, longitude, radius },
    });
    return response.data;
  }
}

export default new VenueService();
