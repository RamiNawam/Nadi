package com.nadi.dto;

import com.nadi.dto.venue.VenueRequestDto;
import com.nadi.dto.venue.VenueResponseDto;
import com.nadi.model.GeoPoint;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class VenueDtoTest {

    @Test
    void testVenueRequestDto_CompleteData() {
        VenueRequestDto request = new VenueRequestDto();
        request.setName("Beirut Sports Center");
        request.setAddress("Hamra, Beirut");
        request.setCafeteriaAvailable(true);
        
        GeoPoint location = new GeoPoint();
        location.setLat(33.8938);
        location.setLng(35.5018);
        request.setLocation(location);

        assertThat(request.getName()).isEqualTo("Beirut Sports Center");
        assertThat(request.getAddress()).isEqualTo("Hamra, Beirut");
        assertThat(request.isCafeteriaAvailable()).isTrue();
        assertThat(request.getLocation()).isNotNull();
        assertThat(request.getLocation().getLat()).isEqualTo(33.8938);
        assertThat(request.getLocation().getLng()).isEqualTo(35.5018);
    }

    @Test
    void testVenueResponseDto_CompleteData() {
        VenueResponseDto response = new VenueResponseDto();
        UUID testId = UUID.randomUUID();
        response.setId(testId);
        response.setName("Downtown Courts");
        response.setAddress("Downtown, Beirut");
        response.setCafeteriaAvailable(false);
        
        GeoPoint location = new GeoPoint();
        location.setLat(33.8894);
        location.setLng(35.5099);
        response.setLocation(location);

        assertThat(response.getId()).isEqualTo(testId);
        assertThat(response.getName()).isEqualTo("Downtown Courts");
        assertThat(response.getAddress()).isEqualTo("Downtown, Beirut");
        assertThat(response.isCafeteriaAvailable()).isFalse();
        assertThat(response.getLocation()).isNotNull();
    }

    @Test
    void testVenueDto_CafeteriaAvailable() {
        VenueRequestDto request = new VenueRequestDto();
        request.setCafeteriaAvailable(true);
        assertThat(request.isCafeteriaAvailable()).isTrue();

        request.setCafeteriaAvailable(false);
        assertThat(request.isCafeteriaAvailable()).isFalse();
    }

    @Test
    void testVenueDto_LocationNull() {
        VenueRequestDto request = new VenueRequestDto();
        request.setName("Test Venue");
        request.setLocation(null);

        assertThat(request.getName()).isEqualTo("Test Venue");
        assertThat(request.getLocation()).isNull();
    }

    @Test
    void testVenueDto_LocationZero() {
        VenueRequestDto request = new VenueRequestDto();
        GeoPoint location = new GeoPoint();
        location.setLat(0.0);
        location.setLng(0.0);
        request.setLocation(location);

        assertThat(request.getLocation().getLat()).isEqualTo(0.0);
        assertThat(request.getLocation().getLng()).isEqualTo(0.0);
    }

    @Test
    void testVenueDto_LongAddress() {
        VenueRequestDto request = new VenueRequestDto();
        String longAddress = "123 Main Street, Building 5, Floor 2, Downtown Beirut, Lebanon";
        request.setAddress(longAddress);

        assertThat(request.getAddress()).isEqualTo(longAddress);
    }

    @Test
    void testVenueDto_EmptyName() {
        VenueRequestDto request = new VenueRequestDto();
        request.setName("");
        assertThat(request.getName()).isEmpty();
    }
}
