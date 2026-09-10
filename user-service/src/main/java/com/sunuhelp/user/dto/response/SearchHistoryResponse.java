package com.sunuhelp.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SearchHistoryResponse {
    private UUID id;
    private String queryText;
    private UUID categoryId;
    private Integer radiusKm;
    private Boolean openNowFilter;
    private BigDecimal searchLatitude;
    private BigDecimal searchLongitude;
    private LocalDateTime searchedAt;
}
