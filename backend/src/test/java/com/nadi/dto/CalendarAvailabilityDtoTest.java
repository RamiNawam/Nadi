package com.nadi.dto;

import com.nadi.dto.calendar.CalendarAvailabilityRequestDto;
import com.nadi.dto.calendar.CalendarAvailabilityResponseDto;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class CalendarAvailabilityDtoTest {

    @Test
    void testCalendarAvailabilityRequestDto_CompleteData() {
        CalendarAvailabilityRequestDto request = new CalendarAvailabilityRequestDto();
        String[][] grid = new String[48][7];
        grid[0][0] = "FREE";
        grid[1][1] = "RESERVED";
        
        request.setCalendarGrid(grid);
        request.setSlotDurationMinutes(30);

        assertThat(request.getCalendarGrid()).isNotNull();
        assertThat(request.getCalendarGrid()[0][0]).isEqualTo("FREE");
        assertThat(request.getCalendarGrid()[1][1]).isEqualTo("RESERVED");
        assertThat(request.getSlotDurationMinutes()).isEqualTo(30);
    }

    @Test
    void testCalendarAvailabilityResponseDto_CompleteData() {
        CalendarAvailabilityResponseDto response = new CalendarAvailabilityResponseDto();
        UUID testId = UUID.randomUUID();
        String[][] grid = new String[48][7];
        grid[0][0] = "FREE";
        
        response.setId(testId);
        response.setCalendarGrid(grid);
        response.setSlotDurationMinutes(30);

        assertThat(response.getId()).isEqualTo(testId);
        assertThat(response.getCalendarGrid()).isNotNull();
        assertThat(response.getSlotDurationMinutes()).isEqualTo(30);
    }

    @Test
    void testCalendarDto_GridDimensions() {
        CalendarAvailabilityRequestDto request = new CalendarAvailabilityRequestDto();
        String[][] grid = new String[48][7];
        
        // Initialize all slots
        for (int i = 0; i < 48; i++) {
            for (int j = 0; j < 7; j++) {
                grid[i][j] = "FREE";
            }
        }
        
        request.setCalendarGrid(grid);
        assertThat(request.getCalendarGrid().length).isEqualTo(48);
        assertThat(request.getCalendarGrid()[0].length).isEqualTo(7);
    }

    @Test
    void testCalendarDto_SlotDurationMinutes() {
        CalendarAvailabilityRequestDto request = new CalendarAvailabilityRequestDto();
        
        request.setSlotDurationMinutes(30);
        assertThat(request.getSlotDurationMinutes()).isEqualTo(30);
        
        request.setSlotDurationMinutes(15);
        assertThat(request.getSlotDurationMinutes()).isEqualTo(15);
        
        request.setSlotDurationMinutes(60);
        assertThat(request.getSlotDurationMinutes()).isEqualTo(60);
    }

    @Test
    void testCalendarDto_GridStates() {
        CalendarAvailabilityRequestDto request = new CalendarAvailabilityRequestDto();
        String[][] grid = new String[48][7];
        
        grid[0][0] = "FREE";
        grid[10][2] = "RESERVED";
        grid[20][4] = "BLOCKED";
        
        request.setCalendarGrid(grid);
        
        assertThat(request.getCalendarGrid()[0][0]).isEqualTo("FREE");
        assertThat(request.getCalendarGrid()[10][2]).isEqualTo("RESERVED");
        assertThat(request.getCalendarGrid()[20][4]).isEqualTo("BLOCKED");
    }

    @Test
    void testCalendarDto_NullGrid() {
        CalendarAvailabilityRequestDto request = new CalendarAvailabilityRequestDto();
        request.setCalendarGrid(null);
        
        assertThat(request.getCalendarGrid()).isNull();
    }

    @Test
    void testCalendarDto_EmptyGrid() {
        CalendarAvailabilityResponseDto response = new CalendarAvailabilityResponseDto();
        String[][] emptyGrid = new String[0][0];
        response.setCalendarGrid(emptyGrid);
        
        assertThat(response.getCalendarGrid()).isEmpty();
    }

    @Test
    void testCalendarDto_FullWeek() {
        CalendarAvailabilityRequestDto request = new CalendarAvailabilityRequestDto();
        String[][] grid = new String[48][7];
        
        // Initialize all 48 time slots across 7 days
        for (int i = 0; i < 48; i++) {
            for (int j = 0; j < 7; j++) {
                grid[i][j] = (j == 0 || j == 6) ? "BLOCKED" : "FREE";
            }
        }
        
        request.setCalendarGrid(grid);
        
        // Check weekend slots
        assertThat(request.getCalendarGrid()[0][0]).isEqualTo("BLOCKED");
        assertThat(request.getCalendarGrid()[0][6]).isEqualTo("BLOCKED");
        
        // Check weekday slots
        assertThat(request.getCalendarGrid()[10][1]).isEqualTo("FREE");
        assertThat(request.getCalendarGrid()[20][5]).isEqualTo("FREE");
    }
}

