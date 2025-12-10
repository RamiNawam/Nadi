package com.nadi.dto.auth;

import lombok.Data;
import java.util.UUID;

@Data
public class LoginResponseDto {
    private String token;
    private UUID userId;
    private String name;
    private String email;
    private String accountType; // user | venue | developer
}

