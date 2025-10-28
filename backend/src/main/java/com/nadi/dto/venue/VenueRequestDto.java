package com.nadi.dto.venue;

import com.nadi.model.GeoPoint;
import lombok.Data;

@Data
public class VenueRequestDto {
    private String name;
    private String address;
    private boolean cafeteriaAvailable;
    private GeoPoint location;
}
