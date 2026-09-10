package com.sunuhelp.review.event;

import com.sunuhelp.common.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/** Consomme par notification-service pour prevenir le proprietaire de la fiche. */
public record ReviewCreatedEvent(
        UUID eventId, LocalDateTime occurredAt, UUID reviewId, UUID entityId, int rating
) implements DomainEvent {
    public static ReviewCreatedEvent of(UUID reviewId, UUID entityId, int rating) {
        return new ReviewCreatedEvent(UUID.randomUUID(), LocalDateTime.now(), reviewId, entityId, rating);
    }
}
