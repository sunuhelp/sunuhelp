package com.sunuhelp.user.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Profil "social" de l'Usager (prenom, ville, avatar) - separe de Account
 * (auth-service) qui ne connait que l'authentification. Un compte Entite
 * peut aussi avoir un profil complet, aucun conflit entre les deux roles.
 */
@Entity
@Table(name = "user_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile extends BaseEntity {

    /** Reference logique vers Account.id (auth-service), unique - un profil par compte. */
    @Column(name = "account_id", nullable = false, unique = true)
    private UUID accountId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private String city;

    /** Reference vers media-service - jamais le fichier ici. */
    @Column(name = "avatar_url")
    private String avatarUrl;

    /** Memorise le dernier drapeau clique cote frontend - evite de re-choisir a chaque connexion. */
    @Column(name = "preferred_locale")
    private String preferredLocale;

    /** true par defaut - conforme a "friction minimale", l'Usager peut desactiver le suivi a tout moment. */
    @Column(name = "history_tracking_enabled", nullable = false)
    private boolean historyTrackingEnabled;

    /** Cree un profil minimal - tous les champs facultatifs restent vides, conforme au parcours le plus court. */
    public static UserProfile create(UUID accountId) {
        UserProfile profile = new UserProfile();
        profile.accountId = accountId;
        profile.historyTrackingEnabled = true;
        return profile;
    }

    public void updateProfile(String firstName, String lastName, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }

    public void updateAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void updatePreferredLocale(String locale) {
        this.preferredLocale = locale;
    }

    public void enableHistoryTracking() {
        this.historyTrackingEnabled = true;
    }

    public void disableHistoryTracking() {
        this.historyTrackingEnabled = false;
    }
}
