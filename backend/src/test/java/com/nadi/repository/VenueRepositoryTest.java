package com.nadi.repository;

import com.nadi.model.GeoPoint;
import com.nadi.model.Venue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for VenueRepository using Mockito.
 * NOTE: These tests use mocked repositories and do NOT interact with the real database.
 * All data is mocked, so no cleanup is needed after tests run.
 */
@ExtendWith(MockitoExtension.class)
class VenueRepositoryTest {

    @Mock
    private VenueRepository venueRepository;

    private Venue testVenue;

    @BeforeEach
    void setUp() {
        testVenue = new Venue();
        testVenue.setId(UUID.randomUUID());
        testVenue.setName("Test Sports Center");
        testVenue.setAddress("123 Main St, Beirut");
        testVenue.setCafeteriaAvailable(true);
        testVenue.setLocation(new GeoPoint(33.8938, 35.5018));
    }

    @Test
    void testSaveAndFindById() {
        when(venueRepository.save(any(Venue.class))).thenReturn(testVenue);
        when(venueRepository.findById(testVenue.getId())).thenReturn(java.util.Optional.of(testVenue));
        
        Venue saved = venueRepository.save(testVenue);
        Venue found = venueRepository.findById(saved.getId()).orElse(null);
        
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test Sports Center");
        assertThat(found.getAddress()).isEqualTo("123 Main St, Beirut");
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        when(venueRepository.findByNameContainingIgnoreCase("sports")).thenReturn(Arrays.asList(testVenue));
        
        List<Venue> found = venueRepository.findByNameContainingIgnoreCase("sports");
        
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).containsIgnoringCase("sports");
    }

    @Test
    void testFindByAddressContainingIgnoreCase() {
        when(venueRepository.findByAddressContainingIgnoreCase("beirut")).thenReturn(Arrays.asList(testVenue));
        
        List<Venue> found = venueRepository.findByAddressContainingIgnoreCase("beirut");
        
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getAddress()).containsIgnoringCase("beirut");
    }
}
