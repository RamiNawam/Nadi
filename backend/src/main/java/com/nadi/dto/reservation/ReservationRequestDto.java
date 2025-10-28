package com.nadi.dto.reservation;

import com.nadi.model.Money;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ReservationRequestDto {
    private UUID userAccountId;
    private UUID courtId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Money totalPrice;
    private String contactPhone;
}
