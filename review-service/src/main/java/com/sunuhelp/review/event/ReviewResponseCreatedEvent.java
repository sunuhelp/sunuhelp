package com.sunuhelp.review.event;

import com.sunuhelp.common.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/** Consomme par notification-service pour prevenir l'auteur de l'avis. */
public record ReviewResponseCreatedEvent(
        UUID eventId, LocalDateTime occurredAt, UUID reviewId, UUID reviewerAccountId
) implements DomainEvent {
    public static ReviewResponseCreatedEvent of(UUID reviewId, UUID reviewerAccountId) {
        return new ReviewResponseCreatedEvent(UUID.randomUUID(), LocalDateTime.now(), reviewId, reviewerAccountId);
    }
}
