package com.nadi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application entry point for Nadi sports court reservation platform.
 * 
 * This Spring Boot application provides REST APIs for:
 * - User account management
 * - Venue and court information
 * - Reservation booking system
 * - Search and filtering capabilities
 * 
 * The application uses a clean layered architecture with separate packages
 * for different business domains (user, venue, reservation, search).
 */
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}

