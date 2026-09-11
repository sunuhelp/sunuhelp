package com.sunuhelp.search.client;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
public class OpeningHourDetailsResponse {
    private DayOfWeek dayOfWeek;
    private boolean closed;
    private LocalTime openingTime;
    private LocalTime closingTime;
}
