package com.sunuhelp.entity.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Association Entite-Categorie, en relation N-N des le depart bien que
 * la V1 n'utilise qu'une seule ligne par Entite (une categorie
 * principale) - permet d'absorber le multi-categorie futur (ex. a la
 * fois Restauration rapide ET Traiteur) sans migration.
 */
@Entity
@Table(name = "entity_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntityCategory extends BaseEntity {

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    /** Reference logique vers Category.id (category-service) - toujours la sous-categorie (feuille), jamais le parent. */
    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    /** true pour l'unique ligne en V1 - distinguera la categorie principale si le multi-categorie est active un jour. */
    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    public static EntityCategory createPrimary(UUID entityId, UUID categoryId) {
        EntityCategory entityCategory = new EntityCategory();
        entityCategory.entityId = entityId;
        entityCategory.categoryId = categoryId;
        entityCategory.isPrimary = true;
        return entityCategory;
    }

    /** Marque cette association comme categorie principale de la fiche. */
    public void markAsPrimary() {
        this.isPrimary = true;
    }
}
