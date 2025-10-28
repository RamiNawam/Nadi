package com.nadi.dto.venue;

import com.nadi.model.GeoPoint;
import lombok.Data;
import java.util.UUID;

@Data
public class VenueResponseDto {
    private UUID id;
    private String name;
    private String address;
    private boolean cafeteriaAvailable;
    private GeoPoint location;
}
