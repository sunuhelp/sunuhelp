package com.sunuhelp.category.event;

import com.sunuhelp.common.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Consomme par entity-service : toute entite referencant sourceCategoryId
 * doit etre reassignee vers targetCategoryId.
 */
public record CategoryMergedEvent(
        UUID eventId,
        LocalDateTime occurredAt,
        UUID sourceCategoryId,
        UUID targetCategoryId
) implements DomainEvent {

    public static CategoryMergedEvent of(UUID sourceCategoryId, UUID targetCategoryId) {
        return new CategoryMergedEvent(UUID.randomUUID(), LocalDateTime.now(), sourceCategoryId, targetCategoryId);
    }
}
