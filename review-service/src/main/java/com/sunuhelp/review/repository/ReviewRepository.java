package com.sunuhelp.review.repository;

import com.sunuhelp.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Page<Review> findByEntityIdAndActiveTrue(UUID entityId, Pageable pageable);

    /** Verifie la contrainte metier "un avis par compte et par fiche" avant insertion. */
    boolean existsByEntityIdAndReviewerAccountId(UUID entityId, UUID reviewerAccountId);
}
