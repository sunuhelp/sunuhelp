package com.sunuhelp.entity.dto.response;

import com.sunuhelp.entity.enums.GeocodingStatus;
import com.sunuhelp.entity.enums.ServicePointType;
import com.sunuhelp.entity.enums.TemporaryStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ServicePointResponse {
    private UUID id;
    private UUID entityId;
    private String name;
    private ServicePointType type;
    private String address;
    private String coverageZone;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private boolean isOpen247;
    private TemporaryStatus temporaryStatus;
    private LocalDateTime temporaryStatusUntil;
    private GeocodingStatus geocodingStatus;
    private String phoneNumber;
    private boolean currentlyOpen;
}
