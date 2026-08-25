package com.sunuhelp.auth.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Permet d'obtenir un nouveau JWT sans redemander le mot de passe. Duree
 * de vie plus longue que le token d'acces, mais pas infinie ; peut etre
 * revoque a distance (ex. "deconnecter tous mes appareils").
 */
@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    @Column(name = "device_info")
    private String deviceInfo;

    public static RefreshToken issue(UUID accountId, String tokenHash,
                                      LocalDateTime expiresAt, String deviceInfo) {
        RefreshToken token = new RefreshToken();
        token.accountId = accountId;
        token.tokenHash = tokenHash;
        token.expiresAt = expiresAt;
        token.deviceInfo = deviceInfo;
        token.revoked = false;
        return token;
    }

    public boolean isValid() {
        return !revoked && LocalDateTime.now().isBefore(expiresAt);
    }

    public void revoke() {
        this.revoked = true;
    }
}
