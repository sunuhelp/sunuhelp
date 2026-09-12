package com.sunuhelp.media.controller;

import com.sunuhelp.media.dto.response.MediaFileResponse;
import com.sunuhelp.media.enums.MediaOwnerType;
import com.sunuhelp.media.service.MediaFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
@Tag(name = "Fichiers")
public class MediaFileController {

    private final MediaFileService mediaFileService;

    public MediaFileController(MediaFileService mediaFileService) {
        this.mediaFileService = mediaFileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload un fichier (logo, photo, document, avatar) - authentifie")
    public ResponseEntity<MediaFileResponse> upload(@RequestParam MultipartFile file,
                                                       @RequestParam MediaOwnerType ownerType,
                                                       @RequestParam UUID ownerId,
                                                       Authentication authentication) {
        UUID uploaderId = (UUID) authentication.getPrincipal();
        MediaFileResponse response = mediaFileService.upload(file, ownerType, ownerId, uploaderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Liste les fichiers d'un proprietaire donne")
    public ResponseEntity<List<MediaFileResponse>> findByOwner(@RequestParam MediaOwnerType ownerType,
                                                                  @RequestParam UUID ownerId) {
        return ResponseEntity.ok(mediaFileService.findByOwner(ownerType, ownerId));
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Telecharge le fichier - controle d'acces selon isPublic")
    public ResponseEntity<byte[]> download(@PathVariable UUID id, Authentication authentication) {
        UUID requesterId = (UUID) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        byte[] content = mediaFileService.download(id, requesterId, isAdmin);
        return ResponseEntity.ok().body(content);
    }
}
