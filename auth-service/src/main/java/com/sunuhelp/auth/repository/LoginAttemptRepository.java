package com.sunuhelp.auth.repository;

import com.sunuhelp.auth.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, UUID> {

    /** Nombre d'echecs recents sur un numero - base du verrouillage anti brute-force. */
    long countByPhoneNumberAndSuccessFalseAndCreatedAtAfter(
            String phoneNumber, LocalDateTime since);
}
