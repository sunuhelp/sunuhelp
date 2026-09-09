package com.sunuhelp.category.event;

import com.sunuhelp.common.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryDeletedEvent(
        UUID eventId,
        LocalDateTime occurredAt,
        UUID categoryId
) implements DomainEvent {

    public static CategoryDeletedEvent of(UUID categoryId) {
        return new CategoryDeletedEvent(UUID.randomUUID(), LocalDateTime.now(), categoryId);
    }
}
