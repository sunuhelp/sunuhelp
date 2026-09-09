package com.sunuhelp.entity.event;

import com.sunuhelp.common.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/** Consomme par search-service pour reindexer la fiche modifiee. */
public record EntityUpdatedEvent(UUID eventId, LocalDateTime occurredAt, UUID entityId) implements DomainEvent {
    public static EntityUpdatedEvent of(UUID entityId) {
        return new EntityUpdatedEvent(UUID.randomUUID(), LocalDateTime.now(), entityId);
    }
}
