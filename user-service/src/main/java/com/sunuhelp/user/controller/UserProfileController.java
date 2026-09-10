package com.sunuhelp.user.controller;

import com.sunuhelp.user.dto.request.UpdateProfileRequest;
import com.sunuhelp.user.dto.response.UserProfileResponse;
import com.sunuhelp.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Profil")
public class UserProfileController {

    private final UserProfileService profileService;

    public UserProfileController(UserProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @Operation(summary = "Consulte son propre profil - le cree s'il n'existe pas encore")
    public ResponseEntity<UserProfileResponse> getOwnProfile(Authentication authentication) {
        UUID accountId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(profileService.getOrCreate(accountId));
    }

    @PutMapping
    @Operation(summary = "Modifie son profil - tous les champs sont facultatifs")
    public ResponseEntity<UserProfileResponse> update(@Valid @RequestBody UpdateProfileRequest request,
                                                         Authentication authentication) {
        UUID accountId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(profileService.update(accountId, request));
    }

    @PutMapping("/history-tracking/enable")
    @Operation(summary = "Active le suivi de l'historique de recherche")
    public ResponseEntity<Void> enableHistoryTracking(Authentication authentication) {
        UUID accountId = (UUID) authentication.getPrincipal();
        profileService.enableHistoryTracking(accountId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/history-tracking/disable")
    @Operation(summary = "Desactive le suivi de l'historique de recherche")
    public ResponseEntity<Void> disableHistoryTracking(Authentication authentication) {
        UUID accountId = (UUID) authentication.getPrincipal();
        profileService.disableHistoryTracking(accountId);
        return ResponseEntity.noContent().build();
    }
}
