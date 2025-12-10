package com.nadi.controller;

import com.nadi.dto.venue.VenueRequestDto;
import com.nadi.dto.venue.VenueResponseDto;
import com.nadi.model.GeoPoint;
import com.nadi.model.Venue;
import com.nadi.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/venues")
@CrossOrigin(origins = "*")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @PostMapping
    public ResponseEntity<VenueResponseDto> createVenue(@RequestBody VenueRequestDto request) {
        Venue venue = venueService.createVenue(
            request.getName(),
            request.getAddress(),
            request.getLocation(),
            request.isCafeteriaAvailable()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponseDto(venue));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueResponseDto> getVenue(@PathVariable String id) {
        return venueService.findById(id)
            .map(venue -> ResponseEntity.ok(mapToResponseDto(venue)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<VenueResponseDto>> getAllVenues() {
        List<VenueResponseDto> venues = venueService.getAllVenues().stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/search")
    public ResponseEntity<List<VenueResponseDto>> searchVenues(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String address
    ) {
        List<Venue> venues = new ArrayList<>();
        if (name != null) {
            venues.addAll(venueService.findByName(name));
        } else if (address != null) {
            venues.addAll(venueService.findByAddress(address));
        } else {
            venues = venueService.getAllVenues();
        }
        
        List<VenueResponseDto> result = venues.stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueResponseDto> updateVenue(@PathVariable String id, @RequestBody VenueRequestDto request) {
        Venue venue = venueService.updateVenue(
            id,
            request.getName(),
            request.getAddress(),
            request.getLocation(),
            request.isCafeteriaAvailable()
        );
        return ResponseEntity.ok(mapToResponseDto(venue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable String id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }

    private VenueResponseDto mapToResponseDto(Venue venue) {
        VenueResponseDto dto = new VenueResponseDto();
        dto.setId(venue.getId());
        dto.setName(venue.getName());
        dto.setAddress(venue.getAddress());
        dto.setLocation(venue.getLocation());
        dto.setCafeteriaAvailable(venue.isCafeteriaAvailable());
        return dto;
    }
}

