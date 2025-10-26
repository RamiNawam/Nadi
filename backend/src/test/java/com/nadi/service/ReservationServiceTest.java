package com.nadi.service;

import com.nadi.model.Money;
import com.nadi.model.Reservation;
import com.nadi.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation testReservation;
    private Money testPrice;
    private UUID testUserId;
    private UUID testCourtId;
    private OffsetDateTime testStart;
    private OffsetDateTime testEnd;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testCourtId = UUID.randomUUID();
        testStart = OffsetDateTime.now().plusHours(1);
        testEnd = OffsetDateTime.now().plusHours(2);
        testPrice = new Money(BigDecimal.valueOf(100.0), "USD");

        testReservation = new Reservation();
        testReservation.setId(UUID.randomUUID());
        testReservation.setCode("RES-001");
        testReservation.setUserAccountId(testUserId);
        testReservation.setCourtId(testCourtId);
        testReservation.setStartTime(testStart);
        testReservation.setEndTime(testEnd);
        testReservation.setTotalPrice(testPrice);
        testReservation.setStatus("PENDING");
    }

    @Test
    void testCreateReservation_Success() {
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = reservationService.createReservation(testUserId, testCourtId, testStart, testEnd, testPrice, "+1234567890");

        assertThat(result).isNotNull();
        assertThat(result.getUserAccountId()).isEqualTo(testUserId);
        assertThat(result.getCourtId()).isEqualTo(testCourtId);
        assertThat(result.getStatus()).isEqualTo("PENDING");
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void testFindByUserId() {
        List<Reservation> reservations = Arrays.asList(testReservation);
        when(reservationRepository.findByUserAccountId(testUserId)).thenReturn(reservations);

        List<Reservation> result = reservationService.findByUserId(testUserId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserAccountId()).isEqualTo(testUserId);
    }

    @Test
    void testConfirmReservation_Success() {
        when(reservationRepository.findById(testReservation.getId())).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        reservationService.confirmReservation(testReservation.getId());

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void testConfirmReservation_NotFound() {
        when(reservationRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.confirmReservation(UUID.randomUUID()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Reservation not found");
    }

    @Test
    void testCancelReservation_Success() {
        when(reservationRepository.findById(testReservation.getId())).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        reservationService.cancelReservation(testReservation.getId());

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void testFindByStatus() {
        List<Reservation> reservations = Arrays.asList(testReservation);
        when(reservationRepository.findByStatus("PENDING")).thenReturn(reservations);

        List<Reservation> result = reservationService.findByStatus("PENDING");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo("PENDING");
    }
}

