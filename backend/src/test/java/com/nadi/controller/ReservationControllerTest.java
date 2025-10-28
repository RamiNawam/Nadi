package com.nadi.controller;

import com.nadi.dto.reservation.ReservationRequestDto;
import com.nadi.dto.reservation.ReservationResponseDto;
import com.nadi.model.Money;
import com.nadi.model.Reservation;
import com.nadi.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private ReservationRequestDto requestDto;
    private Reservation testReservation;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        
        requestDto = new ReservationRequestDto();
        requestDto.setUserAccountId(UUID.randomUUID());
        requestDto.setCourtId(UUID.randomUUID());
        requestDto.setStartTime(OffsetDateTime.now());
        requestDto.setEndTime(OffsetDateTime.now().plusHours(2));
        requestDto.setContactPhone("+96170123456");
        
        Money price = new Money();
        price.setAmount(new BigDecimal("100.00"));
        price.setCurrency("LBP");
        requestDto.setTotalPrice(price);

        testReservation = new Reservation();
        testReservation.setId(testId);
        testReservation.setCode("RES-12345");
        testReservation.setUserAccountId(UUID.randomUUID());
        testReservation.setCourtId(UUID.randomUUID());
        testReservation.setStartTime(OffsetDateTime.now());
        testReservation.setEndTime(OffsetDateTime.now().plusHours(2));
        testReservation.setStatus("PENDING");
        testReservation.setContactPhone("+96170123456");
        testReservation.setTotalPrice(price);
    }

    @Test
    void testCreateReservation_Success() {
        when(reservationService.createReservation(any(), any(), any(), any(), any(), anyString()))
            .thenReturn(testReservation);

        ResponseEntity<ReservationResponseDto> response = reservationController.createReservation(requestDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("RES-12345");
    }

    @Test
    void testGetReservation_Exists() {
        when(reservationService.findById(testId)).thenReturn(Optional.of(testReservation));

        ResponseEntity<ReservationResponseDto> response = reservationController.getReservation(testId.toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testGetReservation_NotFound() {
        when(reservationService.findById(testId)).thenReturn(Optional.empty());

        ResponseEntity<ReservationResponseDto> response = reservationController.getReservation(testId.toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testGetReservationsByUser_Success() {
        List<Reservation> reservations = Arrays.asList(testReservation);
        when(reservationService.findByUserId(testId)).thenReturn(reservations);

        ResponseEntity<List<ReservationResponseDto>> response = reservationController.getReservationsByUser(testId.toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void testGetReservationsByCourt_Success() {
        List<Reservation> reservations = Arrays.asList(testReservation);
        when(reservationService.findByCourtId(testId)).thenReturn(reservations);

        ResponseEntity<List<ReservationResponseDto>> response = reservationController.getReservationsByCourt(testId.toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testGetReservationsByStatus_Success() {
        List<Reservation> reservations = Arrays.asList(testReservation);
        when(reservationService.findByStatus("PENDING")).thenReturn(reservations);

        ResponseEntity<List<ReservationResponseDto>> response = reservationController.getReservationsByStatus("PENDING");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testConfirmReservation_Success() {
        doNothing().when(reservationService).confirmReservation(testId);

        ResponseEntity<Void> response = reservationController.confirmReservation(testId.toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(reservationService).confirmReservation(testId);
    }

    @Test
    void testCancelReservation_Success() {
        doNothing().when(reservationService).cancelReservation(testId);

        ResponseEntity<Void> response = reservationController.cancelReservation(testId.toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(reservationService).cancelReservation(testId);
    }

    @Test
    void testDeleteReservation_Success() {
        doNothing().when(reservationService).deleteReservation(testId);

        ResponseEntity<Void> response = reservationController.deleteReservation(testId.toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(reservationService).deleteReservation(testId);
    }
}

