package com.nadi.service;

import com.nadi.model.GeoPoint;
import com.nadi.model.Venue;
import com.nadi.repository.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueService venueService;

    private Venue testVenue;
    private GeoPoint testLocation;

    @BeforeEach
    void setUp() {
        testLocation = new GeoPoint(33.8938, 35.5018);
        testVenue = new Venue();
        testVenue.setId(UUID.randomUUID());
        testVenue.setName("Test Sports Center");
        testVenue.setAddress("123 Main St, Beirut");
        testVenue.setLocation(testLocation);
        testVenue.setCafeteriaAvailable(true);
    }

    @Test
    void testCreateVenue_Success() {
        when(venueRepository.save(any(Venue.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Venue result = venueService.createVenue("New Venue", "Address", testLocation, true);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("New Venue");
        assertThat(result.isCafeteriaAvailable()).isTrue();
        verify(venueRepository).save(any(Venue.class));
    }

    @Test
    void testFindById_Success() {
        when(venueRepository.findById(testVenue.getId())).thenReturn(Optional.of(testVenue));

        Optional<Venue> result = venueService.findById(testVenue.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test Sports Center");
    }

    @Test
    void testGetAllVenues() {
        List<Venue> venues = Arrays.asList(testVenue);
        when(venueRepository.findAll()).thenReturn(venues);

        List<Venue> result = venueService.getAllVenues();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Sports Center");
    }

    @Test
    void testFindByName() {
        when(venueRepository.findByNameContainingIgnoreCase("sports")).thenReturn(Arrays.asList(testVenue));

        List<Venue> result = venueService.findByName("sports");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).containsIgnoringCase("sports");
    }

    @Test
    void testUpdateVenue_Success() {
        when(venueRepository.findById(testVenue.getId())).thenReturn(Optional.of(testVenue));
        when(venueRepository.save(any(Venue.class))).thenReturn(testVenue);

        Venue result = venueService.updateVenue(testVenue.getId(), "Updated Name", "New Address", testLocation, false);

        assertThat(result).isNotNull();
        verify(venueRepository).save(any(Venue.class));
    }

    @Test
    void testUpdateVenue_NotFound() {
        when(venueRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> venueService.updateVenue(UUID.randomUUID(), "Name", "Address", testLocation, true))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Venue not found");
    }

    @Test
    void testDeleteVenue_Success() {
        when(venueRepository.existsById(testVenue.getId())).thenReturn(true);
        doNothing().when(venueRepository).deleteById(any(UUID.class));

        venueService.deleteVenue(testVenue.getId());

        verify(venueRepository).deleteById(testVenue.getId());
    }

    @Test
    void testDeleteVenue_NotFound() {
        when(venueRepository.existsById(any(UUID.class))).thenReturn(false);

        assertThatThrownBy(() -> venueService.deleteVenue(UUID.randomUUID()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Venue not found");
    }
}

