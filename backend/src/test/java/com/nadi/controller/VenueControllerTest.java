package com.nadi.controller;

import com.nadi.dto.venue.VenueRequestDto;
import com.nadi.dto.venue.VenueResponseDto;
import com.nadi.model.GeoPoint;
import com.nadi.model.Venue;
import com.nadi.service.VenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueControllerTest {

    @Mock
    private VenueService venueService;

    @InjectMocks
    private VenueController venueController;

    private VenueRequestDto requestDto;
    private Venue testVenue;
    private String testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID().toString();
        
        requestDto = new VenueRequestDto();
        requestDto.setName("Beirut Sports Center");
        requestDto.setAddress("Hamra, Beirut");
        requestDto.setCafeteriaAvailable(true);
        
        GeoPoint location = new GeoPoint();
        location.setLat(33.8938);
        location.setLng(35.5018);
        requestDto.setLocation(location);

        testVenue = new Venue();
        testVenue.setId(testId);
        testVenue.setName("Beirut Sports Center");
        testVenue.setAddress("Hamra, Beirut");
        testVenue.setCafeteriaAvailable(true);
        testVenue.setLocation(location);
    }

    @Test
    void testCreateVenue_Success() {
        when(venueService.createVenue(anyString(), anyString(), any(), anyBoolean()))
            .thenReturn(testVenue);

        VenueRequestDto request = new VenueRequestDto();
        request.setName("Beirut Sports Center");
        request.setAddress("Hamra, Beirut");
        request.setCafeteriaAvailable(true);
        request.setLocation(new GeoPoint());

        ResponseEntity<VenueResponseDto> response = venueController.createVenue(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Beirut Sports Center");
    }

    @Test
    void testGetVenue_Exists() {
        when(venueService.findById(testId)).thenReturn(Optional.of(testVenue));

        ResponseEntity<VenueResponseDto> response = venueController.getVenue(testId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Beirut Sports Center");
    }

    @Test
    void testGetVenue_NotFound() {
        when(venueService.findById(testId)).thenReturn(Optional.empty());

        ResponseEntity<VenueResponseDto> response = venueController.getVenue(testId);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testGetAllVenues_Success() {
        List<Venue> venues = Arrays.asList(testVenue);
        when(venueService.getAllVenues()).thenReturn(venues);

        ResponseEntity<List<VenueResponseDto>> response = venueController.getAllVenues();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void testSearchVenues_ByName() {
        List<Venue> venues = Arrays.asList(testVenue);
        when(venueService.findByName("Beirut")).thenReturn(venues);

        ResponseEntity<List<VenueResponseDto>> response = venueController.searchVenues("Beirut", null);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testSearchVenues_ByAddress() {
        List<Venue> venues = Arrays.asList(testVenue);
        when(venueService.findByAddress("Hamra")).thenReturn(venues);

        ResponseEntity<List<VenueResponseDto>> response = venueController.searchVenues(null, "Hamra");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testUpdateVenue_Success() {
        when(venueService.updateVenue(anyString(), anyString(), anyString(), any(), anyBoolean()))
            .thenReturn(testVenue);

        ResponseEntity<VenueResponseDto> response = venueController.updateVenue(
            testId,
            requestDto
        );

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testDeleteVenue_Success() {
        doNothing().when(venueService).deleteVenue(testId);

        ResponseEntity<Void> response = venueController.deleteVenue(testId);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(venueService).deleteVenue(testId);
    }
}

