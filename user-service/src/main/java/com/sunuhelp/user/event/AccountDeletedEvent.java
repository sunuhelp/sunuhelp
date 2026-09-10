package com.sunuhelp.user.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Recu depuis auth-service. Contrairement a nos evenements habituels
 * publies via record + DomainEvent, celui-ci n'implemente pas l'interface
 * car il est CONSOMME, jamais publie par ce service - juste un DTO
 * de deserialisation.
 */
public record AccountDeletedEvent(UUID eventId, LocalDateTime occurredAt, UUID accountId) {
}
