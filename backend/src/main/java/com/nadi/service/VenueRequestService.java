package com.nadi.service;

import com.nadi.model.GeoPoint;
import com.nadi.model.Money;
import com.nadi.model.SportType;
import com.nadi.model.Venue;
import com.nadi.model.VenueAccount;
import com.nadi.model.VenueRequest;
import com.nadi.repository.VenueRequestRepository;
import com.nadi.repository.VenueAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class VenueRequestService {

    @Autowired
    private VenueRequestRepository venueRequestRepository;

    @Autowired
    private VenueAccountRepository venueAccountRepository;

    @Autowired
    private VenueService venueService;

    public VenueRequest submitVenueRequest(UUID venueAccountId, String venueName, String address, 
                                          boolean cafeteriaAvailable,
                                          Map<SportType, Integer> numberOfCourts,
                                          Map<SportType, Integer> playersPerCourt,
                                          Map<SportType, Money> pricePerHour) {
        Optional<VenueAccount> venueAccountOpt = venueAccountRepository.findById(venueAccountId);
        if (venueAccountOpt.isEmpty()) {
            throw new RuntimeException("Venue account not found");
        }

        List<VenueRequest> pendingRequests = venueRequestRepository.findByVenueAccountIdAndStatus(venueAccountId, "PENDING");
        if (!pendingRequests.isEmpty()) {
            throw new RuntimeException("You already have a pending venue request");
        }

        VenueRequest request = new VenueRequest();
        request.setId(UUID.randomUUID());
        request.setVenueAccountId(venueAccountId);
        request.setSubmittedAt(OffsetDateTime.now());
        request.setStatus("PENDING");
        request.setVenueName(venueName);
        request.setAddress(address);
        request.setCafeteriaAvailable(cafeteriaAvailable);
        request.setNumberOfCourts(numberOfCourts);
        request.setPlayersPerCourt(playersPerCourt);
        request.setPricePerHour(pricePerHour);

        return venueRequestRepository.save(request);
    }

    public Optional<VenueRequest> findById(UUID id) {
        return venueRequestRepository.findById(id);
    }

    public List<VenueRequest> getAllRequests() {
        return venueRequestRepository.findAll();
    }

    public List<VenueRequest> getPendingRequests() {
        return venueRequestRepository.findByStatus("PENDING");
    }

    public List<VenueRequest> getApprovedRequests() {
        return venueRequestRepository.findByStatus("APPROVED");
    }

    public List<VenueRequest> getRejectedRequests() {
        return venueRequestRepository.findByStatus("REJECTED");
    }

    public List<VenueRequest> getRequestsByVenueAccount(UUID venueAccountId) {
        return venueRequestRepository.findByVenueAccountId(venueAccountId);
    }

    public Optional<VenueRequest> getLatestRequestByVenueAccount(UUID venueAccountId) {
        List<VenueRequest> requests = venueRequestRepository.findByVenueAccountIdOrderBySubmittedAtDesc(venueAccountId);
        return requests.isEmpty() ? Optional.empty() : Optional.of(requests.get(0));
    }

    public void approveRequest(UUID id) {
        Optional<VenueRequest> requestOpt = venueRequestRepository.findById(id);
        if (requestOpt.isEmpty()) {
            throw new RuntimeException("Venue request not found");
        }
        VenueRequest request = requestOpt.get();
        
        Optional<VenueAccount> venueAccountOpt = venueAccountRepository.findById(request.getVenueAccountId());
        if (venueAccountOpt.isEmpty()) {
            throw new RuntimeException("Venue account not found");
        }
        VenueAccount venueAccount = venueAccountOpt.get();

        if (venueAccount.getVenue() != null) {
            throw new RuntimeException("Venue account already has an approved venue");
        }

        GeoPoint location = new GeoPoint(33.8938, 35.5018);
        Venue venue = venueService.createVenue(
            request.getVenueName(),
            request.getAddress(),
            location,
            request.isCafeteriaAvailable()
        );

        venueAccount.setVenue(venue);
        venueAccountRepository.save(venueAccount);

        request.approve();
        venueRequestRepository.save(request);
    }

    public void rejectRequest(UUID id, String reason) {
        Optional<VenueRequest> request = venueRequestRepository.findById(id);
        if (request.isEmpty()) {
            throw new RuntimeException("Venue request not found");
        }
        VenueRequest req = request.get();
        req.reject(reason);
        venueRequestRepository.save(req);
    }

    public VenueRequest updateVenueRequest(UUID id, String venueName, String address, 
                                          boolean cafeteriaAvailable,
                                          Map<SportType, Integer> numberOfCourts,
                                          Map<SportType, Integer> playersPerCourt,
                                          Map<SportType, Money> pricePerHour) {
        Optional<VenueRequest> requestOpt = venueRequestRepository.findById(id);
        if (requestOpt.isEmpty()) {
            throw new RuntimeException("Venue request not found");
        }
        VenueRequest request = requestOpt.get();
        
        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Can only update pending venue requests");
        }

        request.setVenueName(venueName);
        request.setAddress(address);
        request.setCafeteriaAvailable(cafeteriaAvailable);
        request.setNumberOfCourts(numberOfCourts);
        request.setPlayersPerCourt(playersPerCourt);
        request.setPricePerHour(pricePerHour);

        return venueRequestRepository.save(request);
    }
}

