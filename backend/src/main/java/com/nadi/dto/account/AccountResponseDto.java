package com.nadi.dto.account;

import com.nadi.dto.venue.VenueResponseDto;
import lombok.Data;
import java.util.UUID;

@Data
public class AccountResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String status;
    private String type; // user | venue | developer
    private VenueResponseDto venue; // For venue accounts
}
