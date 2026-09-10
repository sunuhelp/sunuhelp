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
 * Fiche mise en favori par un Usager. Suppression PHYSIQUE, contrairement
 * a la majorite des entites du projet - aucune valeur d'audit a conserver
 * pour un favori retire (decision actee lors de la modelisation).
 */
@Entity
@Table(name = "favorites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite extends BaseEntity {

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    /** Reference logique vers BusinessEntity.id (entity-service). */
    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    public static Favorite create(UUID accountId, UUID entityId) {
        Favorite favorite = new Favorite();
        favorite.accountId = accountId;
        favorite.entityId = entityId;
        return favorite;
    }
}
