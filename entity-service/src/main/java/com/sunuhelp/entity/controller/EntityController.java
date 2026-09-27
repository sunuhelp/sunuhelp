package com.sunuhelp.entity.controller;

import com.sunuhelp.common.dto.PageResponse;
import com.sunuhelp.entity.dto.request.CreateEntityRequest;
import com.sunuhelp.entity.dto.request.UpdateEntityRequest;
import com.sunuhelp.entity.dto.response.EntityResponse;
import com.sunuhelp.entity.service.EntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/entities")
@Tag(name = "Entites")
public class EntityController {

    private final EntityService entityService;

    public EntityController(EntityService entityService) {
        this.entityService = entityService;
    }

    @PostMapping
    @Operation(summary = "Cree une fiche - tout compte authentifie peut devenir proprietaire")
    public ResponseEntity<EntityResponse> create(@Valid @RequestBody CreateEntityRequest request,
                                                   Authentication authentication, Locale locale) {
        UUID ownerId = (UUID) authentication.getPrincipal();
        EntityResponse response = entityService.create(request, ownerId, locale.getLanguage());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Placee AVANT /{id} - sinon Spring interpreterait "mine" comme une
     * tentative d'UUID pour findById, piege classique de routing.
     */
    @GetMapping("/mine")
    @Operation(summary = "Liste les fiches actives du compte connecte")
    public ResponseEntity<PageResponse<EntityResponse>> findMine(Authentication authentication, Locale locale,
                                                                    Pageable pageable) {
        UUID ownerId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(entityService.findByOwner(ownerId, locale.getLanguage(), pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifie la fiche - proprietaire uniquement, retire le badge Verifiee")
    public ResponseEntity<EntityResponse> update(@PathVariable UUID id,
                                                   @Valid @RequestBody UpdateEntityRequest request,
                                                   Authentication authentication, Locale locale) {
        UUID requesterId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(entityService.update(id, request, requesterId, locale.getLanguage()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulte une fiche, publique - incremente le compteur de vues")
    public ResponseEntity<EntityResponse> findById(@PathVariable UUID id, Locale locale) {
        entityService.recordView(id);
        return ResponseEntity.ok(entityService.findById(id, locale.getLanguage()));
    }
}
