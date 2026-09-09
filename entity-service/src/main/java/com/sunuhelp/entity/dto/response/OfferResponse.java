package com.sunuhelp.entity.dto.response;

import com.sunuhelp.entity.enums.Availability;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class OfferResponse {
    private UUID id;
    private UUID servicePointId;
    private BigDecimal price;
    private Availability availability;
    private boolean hasPhoto;
    private String title;
    private String description;
}
