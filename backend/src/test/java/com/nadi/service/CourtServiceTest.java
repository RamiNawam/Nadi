package com.nadi.service;

import com.nadi.model.Court;
import com.nadi.model.Money;
import com.nadi.model.SportType;
import com.nadi.repository.CourtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtServiceTest {

    @Mock
    private CourtRepository courtRepository;

    @InjectMocks
    private CourtService courtService;

    private Court testCourt;
    private Money testPrice;

    @BeforeEach
    void setUp() {
        testPrice = new Money(BigDecimal.valueOf(50.0), "USD");
        testCourt = new Court();
        testCourt.setId(UUID.randomUUID());
        testCourt.setLabel("Court 1");
        testCourt.setSport(SportType.TENNIS);
        testCourt.setCapacity(4);
        testCourt.setPricePerHour(testPrice);
    }

    @Test
    void testCreateCourt_Success() {
        when(courtRepository.save(any(Court.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Court result = courtService.createCourt("New Court", SportType.BASKETBALL, 10, testPrice);

        assertThat(result).isNotNull();
        assertThat(result.getLabel()).isEqualTo("New Court");
        assertThat(result.getSport()).isEqualTo(SportType.BASKETBALL);
        assertThat(result.getCapacity()).isEqualTo(10);
        verify(courtRepository).save(any(Court.class));
    }

    @Test
    void testFindById_Success() {
        when(courtRepository.findById(testCourt.getId())).thenReturn(Optional.of(testCourt));

        Optional<Court> result = courtService.findById(testCourt.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getLabel()).isEqualTo("Court 1");
    }

    @Test
    void testGetAllCourts() {
        List<Court> courts = Arrays.asList(testCourt);
        when(courtRepository.findAll()).thenReturn(courts);

        List<Court> result = courtService.getAllCourts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLabel()).isEqualTo("Court 1");
    }

    @Test
    void testFindBySport() {
        List<Court> courts = Arrays.asList(testCourt);
        when(courtRepository.findBySport(SportType.TENNIS)).thenReturn(courts);

        List<Court> result = courtService.findBySport(SportType.TENNIS);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSport()).isEqualTo(SportType.TENNIS);
    }

    @Test
    void testUpdateCourt_Success() {
        when(courtRepository.findById(testCourt.getId())).thenReturn(Optional.of(testCourt));
        when(courtRepository.save(any(Court.class))).thenReturn(testCourt);

        Court result = courtService.updateCourt(testCourt.getId(), "Updated Court", SportType.FOOTBALL, 20, testPrice);

        assertThat(result).isNotNull();
        verify(courtRepository).save(any(Court.class));
    }

    @Test
    void testUpdateCourt_NotFound() {
        when(courtRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courtService.updateCourt(UUID.randomUUID(), "Court", SportType.TENNIS, 4, testPrice))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Court not found");
    }

    @Test
    void testDeleteCourt_Success() {
        when(courtRepository.existsById(testCourt.getId())).thenReturn(true);
        doNothing().when(courtRepository).deleteById(any(UUID.class));

        courtService.deleteCourt(testCourt.getId());

        verify(courtRepository).deleteById(testCourt.getId());
    }

    @Test
    void testDeleteCourt_NotFound() {
        when(courtRepository.existsById(any(UUID.class))).thenReturn(false);

        assertThatThrownBy(() -> courtService.deleteCourt(UUID.randomUUID()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Court not found");
    }
}

