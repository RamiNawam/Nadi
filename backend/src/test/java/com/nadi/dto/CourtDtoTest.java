package com.nadi.dto;

import com.nadi.dto.court.CourtRequestDto;
import com.nadi.dto.court.CourtResponseDto;
import com.nadi.model.Money;
import com.nadi.model.SportType;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class CourtDtoTest {

    @Test
    void testCourtRequestDto_CompleteData() {
        CourtRequestDto request = new CourtRequestDto();
        request.setLabel("Court 1");
        request.setSport(SportType.FOOTBALL);
        request.setCapacity(12);
        
        Money pricePerHour = new Money();
        pricePerHour.setAmount(new BigDecimal("50.00"));
        pricePerHour.setCurrency("LBP");
        request.setPricePerHour(pricePerHour);

        assertThat(request.getLabel()).isEqualTo("Court 1");
        assertThat(request.getSport()).isEqualTo(SportType.FOOTBALL);
        assertThat(request.getCapacity()).isEqualTo(12);
        assertThat(request.getPricePerHour().getAmount()).isEqualTo(new BigDecimal("50.00"));
        assertThat(request.getPricePerHour().getCurrency()).isEqualTo("LBP");
    }

    @Test
    void testCourtResponseDto_CompleteData() {
        CourtResponseDto response = new CourtResponseDto();
        UUID testId = UUID.randomUUID();
        response.setId(testId);
        response.setLabel("Basketball Court A");
        response.setSport(SportType.BASKETBALL);
        response.setCapacity(10);
        
        Money pricePerHour = new Money();
        pricePerHour.setAmount(new BigDecimal("40.00"));
        pricePerHour.setCurrency("LBP");
        response.setPricePerHour(pricePerHour);

        assertThat(response.getId()).isEqualTo(testId);
        assertThat(response.getLabel()).isEqualTo("Basketball Court A");
        assertThat(response.getSport()).isEqualTo(SportType.BASKETBALL);
        assertThat(response.getCapacity()).isEqualTo(10);
        assertThat(response.getPricePerHour().getAmount()).isEqualTo(new BigDecimal("40.00"));
    }

    @Test
    void testCourtDto_AllSportTypes() {
        CourtRequestDto football = new CourtRequestDto();
        football.setSport(SportType.FOOTBALL);
        assertThat(football.getSport()).isEqualTo(SportType.FOOTBALL);
        
        CourtRequestDto basketball = new CourtRequestDto();
        basketball.setSport(SportType.BASKETBALL);
        assertThat(basketball.getSport()).isEqualTo(SportType.BASKETBALL);
        
        CourtRequestDto padel = new CourtRequestDto();
        padel.setSport(SportType.PADEL);
        assertThat(padel.getSport()).isEqualTo(SportType.PADEL);
        
        CourtRequestDto tennis = new CourtRequestDto();
        tennis.setSport(SportType.TENNIS);
        assertThat(tennis.getSport()).isEqualTo(SportType.TENNIS);
    }

    @Test
    void testCourtDto_DifferentCurrencies() {
        CourtRequestDto request = new CourtRequestDto();
        
        Money usd = new Money();
        usd.setAmount(new BigDecimal("20.00"));
        usd.setCurrency("USD");
        request.setPricePerHour(usd);
        assertThat(request.getPricePerHour().getCurrency()).isEqualTo("USD");
        
        Money lbp = new Money();
        lbp.setAmount(new BigDecimal("50000.00"));
        lbp.setCurrency("LBP");
        request.setPricePerHour(lbp);
        assertThat(request.getPricePerHour().getCurrency()).isEqualTo("LBP");
    }

    @Test
    void testCourtDto_CapacityRange() {
        CourtRequestDto request = new CourtRequestDto();
        
        request.setCapacity(2);
        assertThat(request.getCapacity()).isEqualTo(2);
        
        request.setCapacity(24);
        assertThat(request.getCapacity()).isEqualTo(24);
    }

    @Test
    void testCourtDto_PricePrecision() {
        CourtRequestDto request = new CourtRequestDto();
        Money price = new Money();
        price.setAmount(new BigDecimal("25.75"));
        price.setCurrency("LBP");
        request.setPricePerHour(price);

        assertThat(request.getPricePerHour().getAmount()).isEqualTo(new BigDecimal("25.75"));
    }

    @Test
    void testCourtDto_PriceNull() {
        CourtRequestDto request = new CourtRequestDto();
        request.setPricePerHour(null);
        assertThat(request.getPricePerHour()).isNull();
    }

    @Test
    void testCourtDto_LabelVariations() {
        CourtRequestDto request = new CourtRequestDto();
        
        request.setLabel("Court #1");
        assertThat(request.getLabel()).isEqualTo("Court #1");
        
        request.setLabel("Main Court");
        assertThat(request.getLabel()).isEqualTo("Main Court");
    }
}

