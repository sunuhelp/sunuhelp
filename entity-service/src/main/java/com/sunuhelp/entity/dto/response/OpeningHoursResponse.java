package com.sunuhelp.entity.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
public class OpeningHoursResponse {
    private UUID id;
    private DayOfWeek dayOfWeek;
    private boolean closed;
    private LocalTime openingTime;
    private LocalTime closingTime;
}
