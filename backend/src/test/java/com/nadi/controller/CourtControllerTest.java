package com.nadi.controller;

import com.nadi.dto.court.CourtRequestDto;
import com.nadi.dto.court.CourtResponseDto;
import com.nadi.model.Court;
import com.nadi.model.Money;
import com.nadi.model.SportType;
import com.nadi.service.CourtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtControllerTest {

    @Mock
    private CourtService courtService;

    @InjectMocks
    private CourtController courtController;

    private CourtRequestDto requestDto;
    private Court testCourt;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        
        requestDto = new CourtRequestDto();
        requestDto.setLabel("Court 1");
        requestDto.setSport(SportType.FOOTBALL);
        requestDto.setCapacity(12);
        
        Money pricePerHour = new Money();
        pricePerHour.setAmount(new BigDecimal("50.00"));
        pricePerHour.setCurrency("LBP");
        requestDto.setPricePerHour(pricePerHour);

        testCourt = new Court();
        testCourt.setId(testId);
        testCourt.setLabel("Court 1");
        testCourt.setSport(SportType.FOOTBALL);
        testCourt.setCapacity(12);
        testCourt.setPricePerHour(pricePerHour);
    }

    @Test
    void testCreateCourt_Success() {
        when(courtService.createCourt(anyString(), any(), anyInt(), any()))
            .thenReturn(testCourt);

        ResponseEntity<CourtResponseDto> response = courtController.createCourt(requestDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getLabel()).isEqualTo("Court 1");
    }

    @Test
    void testGetCourt_Exists() {
        when(courtService.findById(testId)).thenReturn(Optional.of(testCourt));

        ResponseEntity<CourtResponseDto> response = courtController.getCourt(testId.toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testGetCourt_NotFound() {
        when(courtService.findById(testId)).thenReturn(Optional.empty());

        ResponseEntity<CourtResponseDto> response = courtController.getCourt(testId.toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testGetAllCourts_Success() {
        List<Court> courts = Arrays.asList(testCourt);
        when(courtService.getAllCourts()).thenReturn(courts);

        ResponseEntity<List<CourtResponseDto>> response = courtController.getAllCourts();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void testGetCourtsBySport_FOOTBALL() {
        List<Court> courts = Arrays.asList(testCourt);
        when(courtService.findBySport(SportType.FOOTBALL)).thenReturn(courts);

        ResponseEntity<List<CourtResponseDto>> response = courtController.getCourtsBySport("FOOTBALL");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testUpdateCourt_Success() {
        when(courtService.updateCourt(any(), anyString(), any(), anyInt(), any()))
            .thenReturn(testCourt);

        ResponseEntity<CourtResponseDto> response = courtController.updateCourt(
            testId.toString(),
            requestDto
        );

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testDeleteCourt_Success() {
        doNothing().when(courtService).deleteCourt(testId);

        ResponseEntity<Void> response = courtController.deleteCourt(testId.toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(courtService).deleteCourt(testId);
    }
}

