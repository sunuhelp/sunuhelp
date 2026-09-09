package com.sunuhelp.entity.controller;

import com.sunuhelp.entity.dto.request.CreateOfferRequest;
import com.sunuhelp.entity.dto.response.OfferResponse;
import com.sunuhelp.entity.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@Tag(name = "Offres")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/api/v1/service-points/{servicePointId}/offers")
    @Operation(summary = "Ajoute une offre - proprietaire uniquement, facultatif")
    public ResponseEntity<OfferResponse> create(@PathVariable UUID servicePointId,
                                                  @Valid @RequestBody CreateOfferRequest request,
                                                  Authentication authentication) {
        UUID requesterId = (UUID) authentication.getPrincipal();
        OfferResponse response = offerService.create(servicePointId, request, requesterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/v1/service-points/{servicePointId}/offers")
    @Operation(summary = "Liste les offres d'un point de service - public")
    public ResponseEntity<List<OfferResponse>> findByServicePoint(@PathVariable UUID servicePointId, Locale locale) {
        return ResponseEntity.ok(offerService.findByServicePoint(servicePointId, locale.getLanguage()));
    }
}
