package com.sunuhelp.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

/**
 * Appelle entity-service par son nom Eureka ("entity-service"), jamais
 * une URL en dur - le load balancing et la decouverte de service sont
 * geres automatiquement par Spring Cloud.
 */
@FeignClient(name = "entity-service")
public interface EntityServiceClient {

    @GetMapping("/api/v1/entities/{id}")
    EntityDetailsResponse getEntity(@PathVariable("id") UUID id);
}
