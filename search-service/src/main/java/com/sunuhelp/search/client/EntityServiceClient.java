package com.sunuhelp.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "entity-service")
public interface EntityServiceClient {

    @GetMapping("/api/v1/entities/{id}")
    EntityDetailsResponse getEntity(@PathVariable("id") UUID id);

    @GetMapping("/api/v1/entities/{entityId}/service-points")
    List<ServicePointDetailsResponse> getServicePoints(@PathVariable("entityId") UUID entityId);

    @GetMapping("/api/v1/service-points/{servicePointId}/opening-hours")
    List<OpeningHourDetailsResponse> getOpeningHours(@PathVariable("servicePointId") UUID servicePointId);
}
