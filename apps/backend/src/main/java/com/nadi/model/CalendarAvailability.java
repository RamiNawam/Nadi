package com.nadi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("calendar_availability")
public class CalendarAvailability {
    @Id
    private UUID id;

    /** 48 half-hour rows × 7 day columns; values: "FREE" | "RESERVED" | "BLOCKED" */
    private String[][] calendarGrid = new String[48][7];

    private int slotDurationMinutes = 30;
    private List<TimeSlot> timeSlots = new ArrayList<>();

    public void addTimeSlot(TimeSlot slot) {
        timeSlots.add(slot);
    }

    public void removeTimeSlot(TimeSlot slot) {
        timeSlots.remove(slot);
    }

    public List<TimeSlot> getAvailableSlots(String dayIso) {
        return new ArrayList<>();
    }

    public void markSlot(int dayIndex, int slotIndex, String status) {
        calendarGrid[slotIndex][dayIndex] = status;
    }
}

