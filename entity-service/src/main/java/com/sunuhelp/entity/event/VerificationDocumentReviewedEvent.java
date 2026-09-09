package com.sunuhelp.entity.event;

import com.sunuhelp.common.event.DomainEvent;
import com.sunuhelp.entity.enums.DocumentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/** Consomme par notification-service pour informer l'Entite du resultat. */
public record VerificationDocumentReviewedEvent(
        UUID eventId, LocalDateTime occurredAt, UUID documentId, UUID entityId, DocumentStatus status
) implements DomainEvent {
    public static VerificationDocumentReviewedEvent of(UUID documentId, UUID entityId, DocumentStatus status) {
        return new VerificationDocumentReviewedEvent(UUID.randomUUID(), LocalDateTime.now(), documentId, entityId, status);
    }
}
