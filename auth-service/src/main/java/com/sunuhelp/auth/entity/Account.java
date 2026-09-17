package com.sunuhelp.auth.entity;

import com.sunuhelp.auth.enums.AccountStatus;
import com.sunuhelp.auth.enums.Role;
import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Compte d'authentification. Ne connait que ce qui concerne
 * l'authentification (numero, mot de passe, role, statut) - jamais le nom,
 * la ville ou l'avatar, qui vivent dans user-service.
 */
@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "phone_verified", nullable = false)
    private boolean phoneVerified;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus;

    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "email_verification_expires_at")
    private LocalDateTime emailVerificationExpiresAt;

    /**
     * Fabrique unique de creation, impose les valeurs de depart correctes
     * (role USER, statut en attente de verification) - jamais de
     * constructeur public pour eviter un compte cree dans un etat
     * incoherent.
     */
    public static Account register(String phoneNumber, String email, String passwordHash) {
        Account account = new Account();
        account.phoneNumber = phoneNumber;
        account.email = email;
        account.passwordHash = passwordHash;
        account.role = Role.USER;
        account.phoneVerified = false;
        account.emailVerified = false;
        account.accountStatus = AccountStatus.PENDING_VERIFICATION;
        return account;
    }

    /** Bascule le compte en actif, apres validation reussie de l'OTP. */
    /** Reservee au DevDataInitializer (donnees de demarrage) - jamais exposee via un endpoint public. */
    public void promoteToAdmin() {
        this.role = Role.ADMIN;
    }

    public void activate() {
        this.phoneVerified = true;
        this.accountStatus = AccountStatus.ACTIVE;
    }

    /** Blocage reversible par un Administrateur. */
    public void suspend() {
        this.accountStatus = AccountStatus.SUSPENDED;
    }

    /** Remplace le hash du mot de passe (deja hache en amont, jamais ici). */
    public void changePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    /** Un email valide est le seul prerequis pour creer une premiere entite. */
    public boolean isEligibleForEntityCreation() {
        return email != null && !email.isBlank();
    }
}
