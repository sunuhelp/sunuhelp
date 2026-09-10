package com.sunuhelp.review.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Avis d'un Usager sur une fiche. Contrainte UNIQUE(entity_id, reviewer_account_id)
 * appliquee au niveau base - un compte ne peut laisser qu'un seul avis par
 * fiche, decision prise lors de la modelisation pour eviter le spam d'avis.
 * Jamais de traduction ici : le contenu genere par l'utilisateur n'est
 * jamais traduit, contrairement au contenu structurel de la plateforme.
 */
@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    /** Reference logique vers BusinessEntity.id (entity-service). */
    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    /** Reference logique vers Account.id (auth-service) - toujours un Usager connecte. */
    @Column(name = "reviewer_account_id", nullable = false)
    private UUID reviewerAccountId;

    @Column(nullable = false)
    private int rating;

    /** Obligatoire, jamais de note sans commentaire - exige explicitement dans le cahier des charges. */
    @Column(nullable = false)
    private String comment;

    public static Review create(UUID entityId, UUID reviewerAccountId, int rating, String comment) {
        Review review = new Review();
        review.entityId = entityId;
        review.reviewerAccountId = reviewerAccountId;
        review.rating = rating;
        review.comment = comment;
        return review;
    }

    /** Masque l'avis suite a moderation - reutilise le soft delete de BaseEntity, pas de champ status dedie. */
    public void hide() {
        this.softDelete();
    }
}
