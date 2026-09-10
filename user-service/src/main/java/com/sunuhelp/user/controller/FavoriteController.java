package com.sunuhelp.user.controller;

import com.sunuhelp.user.dto.request.CreateFavoriteRequest;
import com.sunuhelp.user.dto.response.FavoriteResponse;
import com.sunuhelp.user.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/favorites")
@Tag(name = "Favoris")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    @Operation(summary = "Ajoute une fiche aux favoris")
    public ResponseEntity<FavoriteResponse> create(@Valid @RequestBody CreateFavoriteRequest request,
                                                      Authentication authentication) {
        UUID accountId = (UUID) authentication.getPrincipal();
        FavoriteResponse response = favoriteService.create(accountId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Liste ses favoris")
    public ResponseEntity<Page<FavoriteResponse>> findOwn(Authentication authentication, Pageable pageable) {
        UUID accountId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(favoriteService.findByAccount(accountId, pageable));
    }

    @DeleteMapping("/{entityId}")
    @Operation(summary = "Retire une fiche des favoris - suppression physique")
    public ResponseEntity<Void> delete(@PathVariable UUID entityId, Authentication authentication) {
        UUID accountId = (UUID) authentication.getPrincipal();
        favoriteService.delete(accountId, entityId);
        return ResponseEntity.noContent().build();
    }
}
