/**
 * TypeScript interfaces for User-related data structures.
 * 
 * This file contains type definitions for:
 * - User profile information
 * - Authentication requests and responses
 * - User preferences and settings
 */

export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  dateOfBirth?: string;
  createdAt: string;
  updatedAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
}

export interface AuthResponse {
  user: User;
  token: string;
}

export interface UserProfile {
  user: User;
  preferences: UserPreferences;
}

export interface UserPreferences {
  favoriteSports: string[];
  notificationSettings: NotificationSettings;
  language: string;
}

export interface NotificationSettings {
  emailNotifications: boolean;
  smsNotifications: boolean;
  pushNotifications: boolean;
}

