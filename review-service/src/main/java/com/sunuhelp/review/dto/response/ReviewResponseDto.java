package com.sunuhelp.review.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/** Nommee ReviewResponseDto (pas ReviewResponse) pour eviter la collision avec l'entite ReviewResponse. */
@Getter
@Builder
public class ReviewResponseDto {
    private UUID id;
    private UUID entityId;
    private UUID reviewerAccountId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    /** Null si l'Entite n'a pas encore repondu - reponse toujours facultative. */
    private String responseContent;
}
