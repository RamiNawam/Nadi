package com.nadi.controller;

import com.nadi.dto.court.CourtRequestDto;
import com.nadi.dto.court.CourtResponseDto;
import com.nadi.model.Court;
import com.nadi.model.Money;
import com.nadi.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/courts")
@CrossOrigin(origins = "*")
public class CourtController {

    @Autowired
    private CourtService courtService;

    @PostMapping
    public ResponseEntity<CourtResponseDto> createCourt(@RequestBody CourtRequestDto request) {
        Court court = courtService.createCourt(
            request.getLabel(),
            request.getSport(),
            request.getCapacity(),
            request.getPricePerHour()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponseDto(court));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourtResponseDto> getCourt(@PathVariable String id) {
        return courtService.findById(java.util.UUID.fromString(id))
            .map(court -> ResponseEntity.ok(mapToResponseDto(court)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CourtResponseDto>> getAllCourts() {
        List<CourtResponseDto> courts = courtService.getAllCourts().stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/sport/{sport}")
    public ResponseEntity<List<CourtResponseDto>> getCourtsBySport(@PathVariable String sport) {
        com.nadi.model.SportType sportType = com.nadi.model.SportType.valueOf(sport.toUpperCase());
        List<CourtResponseDto> courts = courtService.findBySport(sportType).stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(courts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourtResponseDto> updateCourt(@PathVariable String id, @RequestBody CourtRequestDto request) {
        Court court = courtService.updateCourt(
            java.util.UUID.fromString(id),
            request.getLabel(),
            request.getSport(),
            request.getCapacity(),
            request.getPricePerHour()
        );
        return ResponseEntity.ok(mapToResponseDto(court));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable String id) {
        courtService.deleteCourt(java.util.UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    private CourtResponseDto mapToResponseDto(Court court) {
        CourtResponseDto dto = new CourtResponseDto();
        dto.setId(court.getId());
        dto.setLabel(court.getLabel());
        dto.setSport(court.getSport());
        dto.setCapacity(court.getCapacity());
        dto.setPricePerHour(court.getPricePerHour());
        return dto;
    }
}

