package com.nadi.service;

import com.nadi.model.Court;
import com.nadi.model.Money;
import com.nadi.model.SportType;
import com.nadi.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourtService {

    @Autowired
    private CourtRepository courtRepository;

    public Court createCourt(String label, SportType sport, int capacity, Money pricePerHour) {
        Court court = new Court();
        court.setId(UUID.randomUUID());
        court.setLabel(label);
        court.setSport(sport);
        court.setCapacity(capacity);
        court.setPricePerHour(pricePerHour);

        return courtRepository.save(court);
    }

    public Optional<Court> findById(UUID id) {
        return courtRepository.findById(id);
    }

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public List<Court> findBySport(SportType sport) {
        return courtRepository.findBySport(sport);
    }

    public Court updateCourt(UUID id, String label, SportType sport, int capacity, Money pricePerHour) {
        Optional<Court> existing = courtRepository.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Court not found");
        }

        Court court = existing.get();
        court.setLabel(label);
        court.setSport(sport);
        court.setCapacity(capacity);
        court.setPricePerHour(pricePerHour);

        return courtRepository.save(court);
    }

    public void deleteCourt(UUID id) {
        if (!courtRepository.existsById(id)) {
            throw new RuntimeException("Court not found");
        }
        courtRepository.deleteById(id);
    }
}

