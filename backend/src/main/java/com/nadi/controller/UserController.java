package com.nadi.controller;

import com.nadi.model.User;
import com.nadi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User endpoints
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    // TODO: Implement user registration endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: Implement user login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: Implement get user profile endpoint
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: Implement update user profile endpoint
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long userId, @RequestBody User user) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: Implement delete user endpoint
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Placeholder for login request DTO
    public static class LoginRequest {
        private String email;
        private String password;
        
        // TODO: Add getters and setters
    }
}

