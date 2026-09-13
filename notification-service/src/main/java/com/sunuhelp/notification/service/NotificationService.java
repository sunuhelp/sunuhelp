package com.sunuhelp.notification.service;

import com.sunuhelp.notification.enums.NotificationType;

import java.util.UUID;

public interface NotificationService {

    /**
     * Envoie une notification si les preferences l'autorisent - sauf si
     * isCritical (OTP, suspension), toujours envoyees quoi qu'il arrive.
     * messageArgs sert a remplir le template MessageFormat correspondant.
     */
    void send(UUID accountId, NotificationType type, String phoneNumber, String email,
              String locale, Object... messageArgs);
}
