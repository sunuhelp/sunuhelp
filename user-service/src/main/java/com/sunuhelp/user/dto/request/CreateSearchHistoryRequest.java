package com.sunuhelp.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/** queryText et categoryId facultatifs individuellement - au moins un des deux verifie cote service. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSearchHistoryRequest {
    private String queryText;
    private UUID categoryId;
    private Integer radiusKm;
    private Boolean openNowFilter;
    private BigDecimal searchLatitude;
    private BigDecimal searchLongitude;
}
