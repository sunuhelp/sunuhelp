package com.sunuhelp.entity.event;

import com.sunuhelp.common.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/** Consomme par search-service pour indexer la nouvelle fiche. */
public record EntityCreatedEvent(UUID eventId, LocalDateTime occurredAt, UUID entityId) implements DomainEvent {
    public static EntityCreatedEvent of(UUID entityId) {
        return new EntityCreatedEvent(UUID.randomUUID(), LocalDateTime.now(), entityId);
    }
}
