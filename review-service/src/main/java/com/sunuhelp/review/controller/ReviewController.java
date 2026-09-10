package com.sunuhelp.review.controller;

import com.sunuhelp.review.dto.request.CreateResponseRequest;
import com.sunuhelp.review.dto.request.CreateReviewRequest;
import com.sunuhelp.review.dto.response.ReviewResponseDto;
import com.sunuhelp.review.service.ReviewService;
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
@Tag(name = "Avis")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/api/v1/entities/{entityId}/reviews")
    @Operation(summary = "Laisse un avis - un seul par compte et par fiche")
    public ResponseEntity<ReviewResponseDto> create(@PathVariable UUID entityId,
                                                       @Valid @RequestBody CreateReviewRequest request,
                                                       Authentication authentication) {
        UUID reviewerId = (UUID) authentication.getPrincipal();
        ReviewResponseDto response = reviewService.create(entityId, request, reviewerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/v1/entities/{entityId}/reviews")
    @Operation(summary = "Liste les avis d'une fiche - public")
    public ResponseEntity<Page<ReviewResponseDto>> findByEntity(@PathVariable UUID entityId, Pageable pageable) {
        return ResponseEntity.ok(reviewService.findByEntity(entityId, pageable));
    }

    @PostMapping("/api/v1/reviews/{reviewId}/response")
    @Operation(summary = "Repond a un avis - proprietaire de la fiche, une seule fois, jamais modifiable")
    public ResponseEntity<ReviewResponseDto> respond(@PathVariable UUID reviewId,
                                                        @Valid @RequestBody CreateResponseRequest request,
                                                        Authentication authentication) {
        UUID responderId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(reviewService.respond(reviewId, request, responderId));
    }
}
