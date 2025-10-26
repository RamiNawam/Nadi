package com.nadi.service;

import com.nadi.model.Money;
import com.nadi.model.SportType;
import com.nadi.model.VenueRequest;
import com.nadi.repository.VenueRequestRepository;
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

    public VenueRequest submitVenueRequest(String venueName, String address, boolean cafeteriaAvailable,
                                          Map<SportType, Integer> numberOfCourts,
                                          Map<SportType, Integer> playersPerCourt,
                                          Map<SportType, Money> pricePerHour) {
        VenueRequest request = new VenueRequest();
        request.setId(UUID.randomUUID());
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

    public void approveRequest(UUID id) {
        Optional<VenueRequest> request = venueRequestRepository.findById(id);
        if (request.isEmpty()) {
            throw new RuntimeException("Venue request not found");
        }
        VenueRequest req = request.get();
        req.approve();
        venueRequestRepository.save(req);
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
}

