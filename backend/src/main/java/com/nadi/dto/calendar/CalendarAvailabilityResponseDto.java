package com.nadi.dto.calendar;

import lombok.Data;
import java.util.UUID;

@Data
public class CalendarAvailabilityResponseDto {
    private UUID id;
    private String[][] calendarGrid; // 48 x 7
    private int slotDurationMinutes;
}
