import axios, { AxiosInstance, AxiosResponse } from 'axios';
import { User, LoginRequest, RegisterRequest, AuthResponse } from '@/types/user';

/**
 * UserService handles all API calls related to user management.
 * 
 * This service provides methods for:
 * - User authentication (login/register)
 * - Profile management
 * - Account operations
 */
class UserService {
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
   * Register a new user account
   */
  async register(userData: RegisterRequest): Promise<AuthResponse> {
    const response: AxiosResponse<AuthResponse> = await this.api.post('/users/register', userData);
    return response.data;
  }

  /**
   * Authenticate user login
   */
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response: AxiosResponse<AuthResponse> = await this.api.post('/users/login', credentials);
    return response.data;
  }

  /**
   * Get user profile by ID
   */
  async getUserProfile(userId: number): Promise<User> {
    const response: AxiosResponse<User> = await this.api.get(`/users/${userId}`);
    return response.data;
  }

  /**
   * Update user profile
   */
  async updateUserProfile(userId: number, userData: Partial<User>): Promise<User> {
    const response: AxiosResponse<User> = await this.api.put(`/users/${userId}`, userData);
    return response.data;
  }

  /**
   * Delete user account
   */
  async deleteUser(userId: number): Promise<void> {
    await this.api.delete(`/users/${userId}`);
  }

  /**
   * Logout user (clear local storage)
   */
  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  }
}

export default new UserService();

