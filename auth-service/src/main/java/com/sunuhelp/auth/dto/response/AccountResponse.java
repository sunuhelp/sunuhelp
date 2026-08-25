package com.sunuhelp.auth.dto.response;

import com.sunuhelp.auth.enums.AccountStatus;
import com.sunuhelp.auth.enums.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/** Jamais de passwordHash ici - seul l'essentiel a exposer au client. */
@Getter
@Builder
public class AccountResponse {
    private UUID id;
    private String phoneNumber;
    private String email;
    private Role role;
    private boolean phoneVerified;
    private boolean emailVerified;
    private AccountStatus accountStatus;
    private LocalDateTime createdAt;
}
