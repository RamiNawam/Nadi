package com.nadi.dto.reservation;

import com.nadi.model.Money;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ReservationResponseDto {
    private UUID id;
    private String code;
    private UUID userAccountId;
    private UUID courtId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String status;
    private Money totalPrice;
    private String contactPhone;
}
