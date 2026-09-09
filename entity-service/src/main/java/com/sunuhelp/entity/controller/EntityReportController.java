package com.sunuhelp.entity.controller;

import com.sunuhelp.entity.dto.request.CreateReportRequest;
import com.sunuhelp.entity.dto.response.EntityReportResponse;
import com.sunuhelp.entity.service.EntityReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Signalements")
public class EntityReportController {

    private final EntityReportService reportService;

    public EntityReportController(EntityReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/api/v1/entities/{entityId}/reports")
    @Operation(summary = "Signale une fiche erronee - accessible sans compte (Visiteur inclus)")
    public ResponseEntity<EntityReportResponse> create(@PathVariable UUID entityId,
                                                          @Valid @RequestBody CreateReportRequest request,
                                                          Authentication authentication) {
        // authentication peut etre anonyme (visiteur non connecte) - principal alors non UUID exploitable.
        UUID reporterId = (authentication != null && authentication.getPrincipal() instanceof UUID uuid) ? uuid : null;
        EntityReportResponse response = reportService.create(entityId, request, reporterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
