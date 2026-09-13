package com.sunuhelp.notification.repository;

import com.sunuhelp.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findByAccountIdOrderByCreatedAtDesc(UUID accountId, Pageable pageable);

    List<Notification> findByStatusAndAttemptCountLessThan(
            com.sunuhelp.notification.enums.NotificationStatus status, int maxAttempts);
}
