package com.sunuhelp.review.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Reponse publique de l'Entite a un avis. UNIQUE(review_id) applique au
 * niveau base - une seule reponse possible, jamais modifiable ni
 * supprimable une fois publiee (regle explicite du cahier des charges).
 * Aucune methode metier ici, coherent avec LoginAttempt : ecrite une fois,
 * jamais modifiee.
 */
@Entity
@Table(name = "review_responses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewResponse extends BaseEntity {

    @Column(name = "review_id", nullable = false, unique = true)
    private UUID reviewId;

    @Column(nullable = false)
    private String content;

    public static ReviewResponse create(UUID reviewId, String content) {
        ReviewResponse response = new ReviewResponse();
        response.reviewId = reviewId;
        response.content = content;
        return response;
    }
}
