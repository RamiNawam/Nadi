package com.nadi.service;

import com.nadi.model.GeoPoint;
import com.nadi.model.Venue;
import com.nadi.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;

    public Venue createVenue(String name, String address, GeoPoint location, boolean cafeteriaAvailable) {
        Venue venue = new Venue();
        venue.setId(UUID.randomUUID().toString()); // Convert UUID to String
        venue.setName(name);
        venue.setAddress(address);
        venue.setLocation(location);
        venue.setCafeteriaAvailable(cafeteriaAvailable);

        return venueRepository.save(venue);
    }

    public Optional<Venue> findById(String id) {
        return venueRepository.findById(id);
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public List<Venue> findByName(String name) {
        return venueRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Venue> findByAddress(String address) {
        return venueRepository.findByAddressContainingIgnoreCase(address);
    }

    public Venue updateVenue(String id, String name, String address, GeoPoint location, boolean cafeteriaAvailable) {
        Optional<Venue> existing = venueRepository.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Venue not found");
        }

        Venue venue = existing.get();
        venue.setName(name);
        venue.setAddress(address);
        venue.setLocation(location);
        venue.setCafeteriaAvailable(cafeteriaAvailable);

        return venueRepository.save(venue);
    }

    public void deleteVenue(String id) {
        if (!venueRepository.existsById(id)) {
            throw new RuntimeException("Venue not found");
        }
        venueRepository.deleteById(id);
    }
}

