package com.sunuhelp.search.client;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ServicePointDetailsResponse {
    private UUID id;
    private UUID entityId;
    private String type;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private boolean isOpen247;
    private String temporaryStatus;
    private String geocodingStatus;
}
