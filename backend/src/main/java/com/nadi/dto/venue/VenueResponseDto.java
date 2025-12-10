package com.nadi.dto.venue;

import com.nadi.model.GeoPoint;
import lombok.Data;

@Data
public class VenueResponseDto {
    private String id;
    private String name;
    private String address;
    private boolean cafeteriaAvailable;
    private GeoPoint location;
}
