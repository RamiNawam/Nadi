package com.nadi.auth.controller;

import com.nadi.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Developer API key management endpoints
 */
@RestController
@RequestMapping("/api/v1/developer")
@CrossOrigin(origins = "http://localhost:3000")
public class DeveloperController {

    @Autowired
    private AuthService authService;

    /**
     * Get developer's API keys
     */
    @GetMapping("/api-key")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<?> getApiKeys(@RequestParam String userId) {
        // TODO: Return user's API keys
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Generate new API key
     */
    @PostMapping("/api-key")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<?> generateApiKey(@RequestBody GenerateApiKeyRequest request) {
        // TODO: Generate new API key
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Rotate API key
     */
    @PostMapping("/api-key/rotate")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<?> rotateApiKey(@RequestBody RotateApiKeyRequest request) {
        // TODO: Rotate API key
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Revoke API key
     */
    @DeleteMapping("/api-key/{key}")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<?> revokeApiKey(@PathVariable String key, @RequestParam String userId) {
        // TODO: Revoke API key
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // DTOs
    public static class GenerateApiKeyRequest {
        public String userId;
        public int rateLimit = 1000;
    }

    public static class RotateApiKeyRequest {
        public String userId;
        public String oldKey;
    }
}
