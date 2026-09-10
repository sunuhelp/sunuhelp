package com.sunuhelp.review.service;

import com.sunuhelp.review.dto.request.CreateResponseRequest;
import com.sunuhelp.review.dto.request.CreateReviewRequest;
import com.sunuhelp.review.dto.response.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReviewService {

    ReviewResponseDto create(UUID entityId, CreateReviewRequest request, UUID reviewerAccountId);

    Page<ReviewResponseDto> findByEntity(UUID entityId, Pageable pageable);

    /** Reserve au proprietaire de la fiche - reponse unique, jamais modifiable. */
    ReviewResponseDto respond(UUID reviewId, CreateResponseRequest request, UUID responderAccountId);
}
