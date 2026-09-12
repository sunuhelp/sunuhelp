package com.sunuhelp.geo.controller;

import com.sunuhelp.geo.dto.request.GeocodeRequest;
import com.sunuhelp.geo.dto.response.GeocodeResponse;
import com.sunuhelp.geo.service.GeocodingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Geocodage")
public class GeocodingController {

    private final GeocodingService geocodingService;

    public GeocodingController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @PostMapping("/api/v1/geocode")
    @Operation(summary = "Convertit une adresse texte en coordonnees GPS - appele en interne par entity-service")
    public ResponseEntity<GeocodeResponse> geocode(@Valid @RequestBody GeocodeRequest request) {
        return ResponseEntity.ok(geocodingService.geocode(request.getAddress()));
    }
}
