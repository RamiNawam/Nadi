package com.nadi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("reservations")
public class Reservation {
    @Id
    private UUID id;
    private String code;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String status;
    private Money totalPrice;

    private String contactPhone;
    private UUID userAccountId;
    private UUID courtId;

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void reschedule(OffsetDateTime newStart, OffsetDateTime newEnd) {
        this.startTime = newStart;
        this.endTime = newEnd;
        this.status = "MODIFIED";
    }
}

