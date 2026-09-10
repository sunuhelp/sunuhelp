package com.sunuhelp.review.controller;

import com.sunuhelp.review.dto.request.CreateReviewReportRequest;
import com.sunuhelp.review.dto.response.ReviewReportResponse;
import com.sunuhelp.review.service.ReviewReportService;
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
@Tag(name = "Signalements d'avis")
public class ReviewReportController {

    private final ReviewReportService reportService;

    public ReviewReportController(ReviewReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/api/v1/reviews/{reviewId}/reports")
    @Operation(summary = "Signale un avis abusif - accessible sans compte")
    public ResponseEntity<ReviewReportResponse> create(@PathVariable UUID reviewId,
                                                          @Valid @RequestBody CreateReviewReportRequest request,
                                                          Authentication authentication) {
        UUID reporterId = (authentication != null && authentication.getPrincipal() instanceof UUID uuid) ? uuid : null;
        ReviewReportResponse response = reportService.create(reviewId, request, reporterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
