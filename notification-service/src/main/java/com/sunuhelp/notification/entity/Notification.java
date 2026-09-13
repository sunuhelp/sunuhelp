package com.sunuhelp.notification.entity;

import com.sunuhelp.common.entity.BaseEntity;
import com.sunuhelp.notification.enums.Channel;
import com.sunuhelp.notification.enums.NotificationStatus;
import com.sunuhelp.notification.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Journal d'audit des envois - jamais supprime ni modifie apres envoi,
 * meme si le compte associe est supprime (contrairement au profil/favoris
 * de user-service). content fige au moment de l'envoi, jamais recalcule.
 */
@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Channel channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    /** Numero/email au moment PRECIS de l'envoi - jamais recalcule si le compte change ensuite. */
    @Column(name = "recipient_snapshot", nullable = false)
    private String recipientSnapshot;

    @Column(nullable = false)
    private String locale;

    @Column
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    @Column(nullable = false)
    private String provider;

    @Column(name = "external_message_id")
    private String externalMessageId;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "next_retry_at")
    private LocalDateTime nextRetryAt;

    public static Notification create(UUID accountId, Channel channel, NotificationType type,
                                       String recipientSnapshot, String locale, String subject, String content) {
        Notification notification = new Notification();
        notification.accountId = accountId;
        notification.channel = channel;
        notification.notificationType = type;
        notification.recipientSnapshot = recipientSnapshot;
        notification.locale = locale;
        notification.subject = subject;
        notification.content = content;
        notification.status = NotificationStatus.PENDING;
        notification.attemptCount = 0;
        return notification;
    }

    public void markSent(String provider, String externalMessageId) {
        this.status = NotificationStatus.SENT;
        this.provider = provider;
        this.externalMessageId = externalMessageId;
        this.attemptCount++;
    }

    public void markFailed(String errorMessage) {
        this.status = NotificationStatus.FAILED;
        this.errorMessage = errorMessage;
        this.attemptCount++;
    }

    /** Planifie une reprise avec backoff exponentiel simple - seulement si moins de 3 tentatives deja faites. */
    public void scheduleRetry() {
        long delayMinutes = (long) Math.pow(5, attemptCount);
        this.nextRetryAt = LocalDateTime.now().plusMinutes(delayMinutes);
    }

    public boolean canRetry() {
        return attemptCount < 3;
    }

    /** OTP et suspension de compte ignorent toujours les preferences - jamais desactivables. */
    public boolean isCritical() {
        return notificationType == NotificationType.OTP || notificationType == NotificationType.ACCOUNT_SUSPENDED;
    }
}
