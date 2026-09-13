package com.sunuhelp.notification.entity;

import com.sunuhelp.common.entity.BaseEntity;
import com.sunuhelp.notification.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "notification_preferences")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationPreference extends BaseEntity {

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(name = "sms_enabled", nullable = false)
    private boolean smsEnabled;

    @Column(name = "email_enabled", nullable = false)
    private boolean emailEnabled;

    public static NotificationPreference createDefault(UUID accountId, NotificationType type) {
        NotificationPreference pref = new NotificationPreference();
        pref.accountId = accountId;
        pref.notificationType = type;
        pref.smsEnabled = true;
        pref.emailEnabled = true;
        return pref;
    }

    public void updatePreferences(boolean smsEnabled, boolean emailEnabled) {
        this.smsEnabled = smsEnabled;
        this.emailEnabled = emailEnabled;
    }
}
