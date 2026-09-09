package com.sunuhelp.entity.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** Remplace la semaine complete d'horaires d'un point de service en un seul appel. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetOpeningHoursRequest {

    @NotEmpty
    @Valid
    private List<OpeningHourRequest> days;
}
