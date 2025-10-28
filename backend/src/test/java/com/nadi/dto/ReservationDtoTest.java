package com.nadi.dto;

import com.nadi.dto.reservation.ReservationRequestDto;
import com.nadi.dto.reservation.ReservationResponseDto;
import com.nadi.model.Money;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class ReservationDtoTest {

    @Test
    void testReservationRequestDto_CompleteData() {
        ReservationRequestDto request = new ReservationRequestDto();
        UUID userId = UUID.randomUUID();
        UUID courtId = UUID.randomUUID();
        
        request.setUserAccountId(userId);
        request.setCourtId(courtId);
        request.setStartTime(OffsetDateTime.now());
        request.setEndTime(OffsetDateTime.now().plusHours(2));
        request.setContactPhone("+96170123456");
        
        Money price = new Money();
        price.setAmount(new BigDecimal("100.00"));
        price.setCurrency("LBP");
        request.setTotalPrice(price);

        assertThat(request.getUserAccountId()).isEqualTo(userId);
        assertThat(request.getCourtId()).isEqualTo(courtId);
        assertThat(request.getStartTime()).isNotNull();
        assertThat(request.getEndTime()).isNotNull();
        assertThat(request.getContactPhone()).isEqualTo("+96170123456");
        assertThat(request.getTotalPrice().getAmount()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    void testReservationResponseDto_CompleteData() {
        ReservationResponseDto response = new ReservationResponseDto();
        UUID testId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID courtId = UUID.randomUUID();
        
        response.setId(testId);
        response.setCode("RES-2024-001");
        response.setUserAccountId(userId);
        response.setCourtId(courtId);
        response.setStartTime(OffsetDateTime.now());
        response.setEndTime(OffsetDateTime.now().plusHours(2));
        response.setStatus("CONFIRMED");
        response.setContactPhone("+96170123456");
        
        Money price = new Money();
        price.setAmount(new BigDecimal("100.00"));
        price.setCurrency("LBP");
        response.setTotalPrice(price);

        assertThat(response.getId()).isEqualTo(testId);
        assertThat(response.getCode()).isEqualTo("RES-2024-001");
        assertThat(response.getUserAccountId()).isEqualTo(userId);
        assertThat(response.getCourtId()).isEqualTo(courtId);
        assertThat(response.getStatus()).isEqualTo("CONFIRMED");
        assertThat(response.getContactPhone()).isEqualTo("+96170123456");
        assertThat(response.getTotalPrice().getAmount()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    void testReservationDto_StatusEnum() {
        ReservationResponseDto response = new ReservationResponseDto();
        
        response.setStatus("PENDING");
        assertThat(response.getStatus()).isEqualTo("PENDING");
        
        response.setStatus("CONFIRMED");
        assertThat(response.getStatus()).isEqualTo("CONFIRMED");
        
        response.setStatus("MODIFIED");
        assertThat(response.getStatus()).isEqualTo("MODIFIED");
        
        response.setStatus("CANCELLED");
        assertThat(response.getStatus()).isEqualTo("CANCELLED");
    }

    @Test
    void testReservationDto_DateTimeRange() {
        ReservationRequestDto request = new ReservationRequestDto();
        OffsetDateTime start = OffsetDateTime.now();
        OffsetDateTime end = start.plusHours(2);
        
        request.setStartTime(start);
        request.setEndTime(end);

        assertThat(request.getEndTime()).isAfter(request.getStartTime());
        assertThat(end).isAfter(start);
    }

    @Test
    void testReservationDto_PhoneNumberFormats() {
        ReservationRequestDto request = new ReservationRequestDto();
        
        request.setContactPhone("+96170123456");
        assertThat(request.getContactPhone()).isEqualTo("+96170123456");
        
        request.setContactPhone("+1234567890");
        assertThat(request.getContactPhone()).isEqualTo("+1234567890");
    }

    @Test
    void testReservationDto_PriceCalculations() {
        ReservationRequestDto request = new ReservationRequestDto();
        
        Money lowPrice = new Money();
        lowPrice.setAmount(new BigDecimal("50.00"));
        lowPrice.setCurrency("LBP");
        request.setTotalPrice(lowPrice);
        assertThat(request.getTotalPrice().getAmount()).isEqualTo(new BigDecimal("50.00"));
        
        Money highPrice = new Money();
        highPrice.setAmount(new BigDecimal("500.00"));
        highPrice.setCurrency("LBP");
        request.setTotalPrice(highPrice);
        assertThat(request.getTotalPrice().getAmount()).isEqualTo(new BigDecimal("500.00"));
    }

    @Test
    void testReservationDto_DifferentCurrencies() {
        ReservationResponseDto response = new ReservationResponseDto();
        
        Money lbp = new Money();
        lbp.setAmount(new BigDecimal("100.00"));
        lbp.setCurrency("LBP");
        response.setTotalPrice(lbp);
        assertThat(response.getTotalPrice().getCurrency()).isEqualTo("LBP");
        
        Money usd = new Money();
        usd.setAmount(new BigDecimal("0.50"));
        usd.setCurrency("USD");
        response.setTotalPrice(usd);
        assertThat(response.getTotalPrice().getCurrency()).isEqualTo("USD");
    }

    @Test
    void testReservationDto_MultipleTimeSlots() {
        ReservationRequestDto request1 = new ReservationRequestDto();
        request1.setStartTime(OffsetDateTime.now().plusHours(1));
        request1.setEndTime(OffsetDateTime.now().plusHours(2));
        
        ReservationRequestDto request2 = new ReservationRequestDto();
        request2.setStartTime(OffsetDateTime.now().plusHours(3));
        request2.setEndTime(OffsetDateTime.now().plusHours(4));

        assertThat(request2.getStartTime()).isAfter(request1.getEndTime());
    }
}

