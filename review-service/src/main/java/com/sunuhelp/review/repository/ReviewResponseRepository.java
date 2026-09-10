package com.sunuhelp.review.repository;

import com.sunuhelp.review.entity.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewResponseRepository extends JpaRepository<ReviewResponse, UUID> {
    Optional<ReviewResponse> findByReviewId(UUID reviewId);
    boolean existsByReviewId(UUID reviewId);
}
