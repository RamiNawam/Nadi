package com.nadi.service;

import com.nadi.model.Money;
import com.nadi.model.SportType;
import com.nadi.model.VenueRequest;
import com.nadi.model.Venue;
import com.nadi.model.VenueAccount;
import com.nadi.repository.VenueRequestRepository;
import com.nadi.repository.VenueAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueRequestServiceTest {

    @Mock
    private VenueRequestRepository venueRequestRepository;

    @Mock
    private VenueAccountRepository venueAccountRepository;

    @Mock
    private VenueService venueService;

    @InjectMocks
    private VenueRequestService venueRequestService;

    private VenueRequest testRequest;
    private VenueAccount venueAccount;
    private UUID venueAccountId;
    private Map<SportType, Integer> numberOfCourts;
    private Map<SportType, Integer> playersPerCourt;
    private Map<SportType, Money> pricePerHour;

    @BeforeEach
    void setUp() {
        numberOfCourts = new HashMap<>();
        numberOfCourts.put(SportType.TENNIS, 4);
        
        playersPerCourt = new HashMap<>();
        playersPerCourt.put(SportType.TENNIS, 4);
        
        pricePerHour = new HashMap<>();
        pricePerHour.put(SportType.TENNIS, new Money(BigDecimal.valueOf(50.0), "USD"));

        venueAccountId = UUID.randomUUID();
        venueAccount = new VenueAccount();
        venueAccount.setId(venueAccountId);

        testRequest = new VenueRequest();
        testRequest.setId(UUID.randomUUID());
        testRequest.setVenueAccountId(venueAccountId);
        testRequest.setVenueName("New Sports Venue");
        testRequest.setAddress("Beirut Downtown");
        testRequest.setCafeteriaAvailable(true);
        testRequest.setStatus("PENDING");
        testRequest.setNumberOfCourts(numberOfCourts);
        testRequest.setPlayersPerCourt(playersPerCourt);
        testRequest.setPricePerHour(pricePerHour);
    }

    @Test
    void testSubmitVenueRequest_Success() {
        when(venueAccountRepository.findById(venueAccountId)).thenReturn(Optional.of(venueAccount));
        when(venueRequestRepository.findByVenueAccountIdAndStatus(venueAccountId, "PENDING")).thenReturn(List.of());
        when(venueRequestRepository.save(any(VenueRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VenueRequest result = venueRequestService.submitVenueRequest(
            venueAccountId,
            "Venue",
            "Address",
            true,
            numberOfCourts,
            playersPerCourt,
            pricePerHour
        );

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("PENDING");
        verify(venueRequestRepository).save(any(VenueRequest.class));
    }

    @Test
    void testApproveRequest_Success() {
        when(venueRequestRepository.findById(testRequest.getId())).thenReturn(Optional.of(testRequest));
        when(venueRequestRepository.save(any(VenueRequest.class))).thenReturn(testRequest);
        when(venueAccountRepository.findById(venueAccountId)).thenReturn(Optional.of(venueAccount));
        when(venueService.createVenue(anyString(), anyString(), any(), anyBoolean())).thenReturn(new Venue());

        venueRequestService.approveRequest(testRequest.getId());

        verify(venueRequestRepository).save(any(VenueRequest.class));
    }

    @Test
    void testApproveRequest_NotFound() {
        when(venueRequestRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> venueRequestService.approveRequest(UUID.randomUUID()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Venue request not found");
    }

    @Test
    void testRejectRequest_Success() {
        when(venueRequestRepository.findById(testRequest.getId())).thenReturn(Optional.of(testRequest));
        when(venueRequestRepository.save(any(VenueRequest.class))).thenReturn(testRequest);

        venueRequestService.rejectRequest(testRequest.getId(), "Not approved");

        verify(venueRequestRepository).save(any(VenueRequest.class));
    }

    @Test
    void testGetPendingRequests() {
        List<VenueRequest> requests = Arrays.asList(testRequest);
        when(venueRequestRepository.findByStatus("PENDING")).thenReturn(requests);

        List<VenueRequest> result = venueRequestService.getPendingRequests();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo("PENDING");
    }
}

