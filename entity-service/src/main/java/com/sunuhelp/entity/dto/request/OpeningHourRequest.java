package com.sunuhelp.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpeningHourRequest {

    @NotNull
    private DayOfWeek dayOfWeek;

    private boolean closed;

    /** Requis si closed = false. */
    private LocalTime openingTime;

    /** Requis si closed = false. */
    private LocalTime closingTime;
}
