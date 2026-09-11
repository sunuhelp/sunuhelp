package com.sunuhelp.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "category-service")
public interface CategoryServiceClient {

    @GetMapping("/api/v1/categories/{id}")
    CategoryDetailsResponse getCategory(@PathVariable("id") UUID id);
}
