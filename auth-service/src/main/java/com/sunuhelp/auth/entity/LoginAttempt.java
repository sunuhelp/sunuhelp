package com.sunuhelp.auth.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Journal d'audit des tentatives de connexion, utilise pour detecter et
 * limiter le brute-force. Jamais modifiee ni supprimee apres creation -
 * aucune methode metier ici, contrairement aux autres entites.
 */
@Entity
@Table(name = "login_attempts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginAttempt extends BaseEntity {

    /** Nullable : une tentative peut viser un numero qui n'existe pas. */
    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    public static LoginAttempt record(UUID accountId, String phoneNumber, boolean success,
                                       String ipAddress, String userAgent) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.accountId = accountId;
        attempt.phoneNumber = phoneNumber;
        attempt.success = success;
        attempt.ipAddress = ipAddress;
        attempt.userAgent = userAgent;
        return attempt;
    }
}
