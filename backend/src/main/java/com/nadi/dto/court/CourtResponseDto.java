package com.nadi.dto.court;

import com.nadi.model.Money;
import com.nadi.model.SportType;
import lombok.Data;
import java.util.UUID;

@Data
public class CourtResponseDto {
    private UUID id;
    private String label;
    private SportType sport;
    private int capacity;
    private Money pricePerHour;
}
