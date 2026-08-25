package com.sunuhelp.auth.event;

import com.sunuhelp.auth.enums.OtpType;
import com.sunuhelp.common.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Publie vers notification-service pour l'envoi effectif du SMS. Porte le
 * code EN CLAIR de facon ephemere (jamais persiste ainsi) - notification-service
 * en a besoin pour composer le message, auth-service ne stocke que le hash.
 */
public record OtpRequestedEvent(
        UUID eventId,
        LocalDateTime occurredAt,
        UUID accountId,
        String phoneNumber,
        OtpType otpType,
        String rawCode
) implements DomainEvent {

    public static OtpRequestedEvent of(UUID accountId, String phoneNumber,
                                        OtpType otpType, String rawCode) {
        return new OtpRequestedEvent(
                UUID.randomUUID(), LocalDateTime.now(), accountId, phoneNumber, otpType, rawCode);
    }
}
