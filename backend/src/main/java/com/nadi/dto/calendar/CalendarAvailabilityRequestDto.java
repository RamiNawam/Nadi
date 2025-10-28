package com.nadi.dto.calendar;

import lombok.Data;

@Data
public class CalendarAvailabilityRequestDto {
    private String[][] calendarGrid; // 48 x 7
    private int slotDurationMinutes;
}
