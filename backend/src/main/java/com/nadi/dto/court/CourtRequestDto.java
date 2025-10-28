package com.nadi.dto.court;

import com.nadi.model.Money;
import com.nadi.model.SportType;
import lombok.Data;

@Data
public class CourtRequestDto {
    private String label;
    private SportType sport;
    private int capacity;
    private Money pricePerHour;
}
