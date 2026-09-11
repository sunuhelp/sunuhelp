package com.sunuhelp.entity.controller;

import com.sunuhelp.entity.dto.request.CreateServicePointRequest;
import com.sunuhelp.entity.dto.request.SetOpeningHoursRequest;
import com.sunuhelp.entity.dto.request.SetTemporaryStatusRequest;
import com.sunuhelp.entity.dto.response.OpeningHoursResponse;
import com.sunuhelp.entity.dto.response.ServicePointResponse;
import com.sunuhelp.entity.service.ServicePointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Points de service")
public class ServicePointController {

    private final ServicePointService servicePointService;

    public ServicePointController(ServicePointService servicePointService) {
        this.servicePointService = servicePointService;
    }

    @PostMapping("/api/v1/entities/{entityId}/service-points")
    @Operation(summary = "Ajoute un point de service - proprietaire uniquement")
    public ResponseEntity<ServicePointResponse> create(@PathVariable UUID entityId,
                                                         @Valid @RequestBody CreateServicePointRequest request,
                                                         Authentication authentication) {
        UUID requesterId = (UUID) authentication.getPrincipal();
        ServicePointResponse response = servicePointService.create(entityId, request, requesterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/v1/entities/{entityId}/service-points")
    @Operation(summary = "Liste les points de service d'une fiche - public")
    public ResponseEntity<List<ServicePointResponse>> findByEntity(@PathVariable UUID entityId) {
        return ResponseEntity.ok(servicePointService.findByEntity(entityId));
    }

    @PutMapping("/api/v1/service-points/{id}/temporary-status")
    @Operation(summary = "Definit un statut temporaire (fermeture exceptionnelle...) - proprietaire uniquement")
    public ResponseEntity<Void> setTemporaryStatus(@PathVariable UUID id,
                                                     @Valid @RequestBody SetTemporaryStatusRequest request,
                                                     Authentication authentication) {
        UUID requesterId = (UUID) authentication.getPrincipal();
        servicePointService.setTemporaryStatus(id, request, requesterId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/service-points/{id}/opening-hours")
    @Operation(summary = "Lit les horaires - public, utilise notamment par search-service")
    public ResponseEntity<List<OpeningHoursResponse>> getOpeningHours(@PathVariable UUID id) {
        return ResponseEntity.ok(servicePointService.findOpeningHours(id));
    }

    @PutMapping("/api/v1/service-points/{id}/opening-hours")
    @Operation(summary = "Remplace les horaires de la semaine - proprietaire uniquement")
    public ResponseEntity<List<OpeningHoursResponse>> setOpeningHours(@PathVariable UUID id,
                                                                        @Valid @RequestBody SetOpeningHoursRequest request,
                                                                        Authentication authentication) {
        UUID requesterId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(servicePointService.setOpeningHours(id, request, requesterId));
    }
}
