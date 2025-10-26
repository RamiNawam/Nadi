package com.nadi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    private UUID id;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String status;
}

