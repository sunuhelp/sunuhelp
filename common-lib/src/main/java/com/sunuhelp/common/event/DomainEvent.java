package com.sunuhelp.common.event;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DomainEvent {
    UUID eventId();
    LocalDateTime occurredAt();
}
