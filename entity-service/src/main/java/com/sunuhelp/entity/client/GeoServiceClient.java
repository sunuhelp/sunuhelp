package com.sunuhelp.entity.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Appelle geo-service par son nom Eureka, jamais une URL en dur -
 * cohérent avec les autres appels inter-services deja construits
 * (search-service vers entity-service/category-service).
 */
@FeignClient(name = "geo-service")
public interface GeoServiceClient {

    @PostMapping("/api/v1/geocode")
    GeocodeResult geocode(@RequestBody GeocodeRequest request);

    record GeocodeRequest(String address) {}
    record GeocodeResult(java.math.BigDecimal latitude, java.math.BigDecimal longitude,
                          java.math.BigDecimal confidenceScore, boolean fromCache) {}
}
