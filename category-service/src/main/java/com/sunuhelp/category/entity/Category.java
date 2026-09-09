package com.sunuhelp.category.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Categorie ou sous-categorie - une seule table auto-referencee plutot
 * que deux tables distinctes, car les deux ont exactement les memes
 * attributs (seule la position dans la hierarchie change).
 */
@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String slug;

    /** Reference logique vers une autre Category - null si racine. */
    @Column(name = "parent_id")
    private UUID parentId;

    @Column
    private String icon;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "requires_validation", nullable = false)
    private boolean requiresValidation;

    /** 0 = racine, 1 = sous-categorie - calcule, jamais saisi par le client. */
    @Column(nullable = false)
    private int depth;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    public static Category createRoot(String slug, String icon, int displayOrder,
                                       boolean requiresValidation, UUID createdBy) {
        Category category = new Category();
        category.slug = slug;
        category.parentId = null;
        category.icon = icon;
        category.displayOrder = displayOrder;
        category.requiresValidation = requiresValidation;
        category.depth = 0;
        category.createdBy = createdBy;
        return category;
    }

    /** parentDepth vient du parent deja charge par le service - jamais recalcule ici. */
    public static Category createChild(String slug, UUID parentId, int parentDepth, String icon,
                                        int displayOrder, boolean requiresValidation, UUID createdBy) {
        Category category = new Category();
        category.slug = slug;
        category.parentId = parentId;
        category.icon = icon;
        category.displayOrder = displayOrder;
        category.requiresValidation = requiresValidation;
        category.depth = parentDepth + 1;
        category.createdBy = createdBy;
        return category;
    }

    public boolean isRoot() {
        return parentId == null;
    }

    /** Categorie sensible (Sante, Finance...) : conditionne le badge "Verifiee" cote entity-service. */
    public boolean isSensitive() {
        return requiresValidation;
    }

    public void moveTo(UUID newParentId, int newParentDepth, UUID updatedBy) {
        this.parentId = newParentId;
        this.depth = newParentDepth + 1;
        this.updatedBy = updatedBy;
    }

    public void reorder(int newOrder, UUID updatedBy) {
        this.displayOrder = newOrder;
        this.updatedBy = updatedBy;
    }

    /**
     * Marque cette categorie comme fusionnee (desactivee). La reassignation
     * reelle des entites qui la referencaient se fait dans entity-service,
     * via l'evenement category-merged publie par le service applicatif.
     */
    public void markMerged(UUID updatedBy) {
        this.softDelete();
        this.updatedBy = updatedBy;
    }
}
