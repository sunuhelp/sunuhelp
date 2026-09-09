package com.sunuhelp.entity.controller;

import com.sunuhelp.entity.dto.request.ReviewDocumentRequest;
import com.sunuhelp.entity.dto.request.SubmitDocumentRequest;
import com.sunuhelp.entity.dto.response.VerificationDocumentResponse;
import com.sunuhelp.entity.service.VerificationDocumentService;
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
@Tag(name = "Documents de verification")
public class VerificationDocumentController {

    private final VerificationDocumentService documentService;

    public VerificationDocumentController(VerificationDocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/api/v1/entities/{entityId}/documents")
    @Operation(summary = "Soumet une piece justificative - proprietaire uniquement, facultatif")
    public ResponseEntity<VerificationDocumentResponse> submit(@PathVariable UUID entityId,
                                                                  @Valid @RequestBody SubmitDocumentRequest request,
                                                                  Authentication authentication) {
        UUID requesterId = (UUID) authentication.getPrincipal();
        VerificationDocumentResponse response = documentService.submit(entityId, request, requesterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/v1/entities/{entityId}/documents")
    @Operation(summary = "Liste les documents d'une fiche - proprietaire uniquement")
    public ResponseEntity<List<VerificationDocumentResponse>> findByEntity(@PathVariable UUID entityId,
                                                                              Authentication authentication) {
        UUID requesterId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(documentService.findByEntity(entityId, requesterId));
    }

    @PostMapping("/api/v1/documents/{documentId}/review")
    @Operation(summary = "Approuve ou rejette un document - Administrateur uniquement")
    public ResponseEntity<VerificationDocumentResponse> review(@PathVariable UUID documentId,
                                                                   @Valid @RequestBody ReviewDocumentRequest request,
                                                                   Authentication authentication) {
        UUID reviewerId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(documentService.review(documentId, request, reviewerId));
    }
}
