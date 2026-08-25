package com.sunuhelp.auth.entity;

import com.sunuhelp.auth.enums.OtpType;
import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Code de verification a usage unique (inscription, connexion,
 * reinitialisation, changement de numero). Table separee de Account : un
 * compte genere plusieurs codes dans le temps, l'historique sert a
 * detecter les abus (ex. demandes repetees en rafale).
 */
@Entity
@Table(name = "otp_codes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OtpCode extends BaseEntity {

    /** Reference logique vers Account.id - jamais de relation JPA directe. */
    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "code_hash", nullable = false)
    private String codeHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "otp_type", nullable = false)
    private OtpType otpType;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false)
    private int attempts;

    public static OtpCode generate(UUID accountId, String codeHash, OtpType otpType,
                                    LocalDateTime expiresAt) {
        OtpCode otp = new OtpCode();
        otp.accountId = accountId;
        otp.codeHash = codeHash;
        otp.otpType = otpType;
        otp.expiresAt = expiresAt;
        otp.used = false;
        otp.attempts = 0;
        return otp;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    /** Compare le code saisi au hash stocke - le hachage reste hors de l'entite. */
    public boolean isValid(String rawCode, PasswordEncoder encoder) {
        return !used && !isExpired() && encoder.matches(rawCode, codeHash);
    }

    public void markUsed() {
        this.used = true;
    }

    public void incrementAttempts() {
        this.attempts++;
    }
}
