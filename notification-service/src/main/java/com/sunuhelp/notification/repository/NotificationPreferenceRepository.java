package com.sunuhelp.notification.repository;

import com.sunuhelp.notification.entity.NotificationPreference;
import com.sunuhelp.notification.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, UUID> {
    Optional<NotificationPreference> findByAccountIdAndNotificationType(UUID accountId, NotificationType type);
}
