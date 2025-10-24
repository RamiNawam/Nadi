package com.nadi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Nadi sports venue reservation platform
 */
@SpringBootApplication
@EnableScheduling
public class NadiApplication {
    public static void main(String[] args) {
        SpringApplication.run(NadiApplication.class, args);
    }
}
