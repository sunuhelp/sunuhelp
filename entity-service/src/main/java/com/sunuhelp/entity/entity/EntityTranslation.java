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
 * Traduction d'une fiche (nom, description) - meme pattern que
 * CategoryTranslation. Le nom d'une fiche est saisi par l'Entite dans
 * la langue de son choix, qui devient la langue de reference de cette
 * fiche precise.
 */
@Entity
@Table(name = "entity_translations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntityTranslation extends BaseEntity {

    /** Reference logique vers BusinessEntity.id. */
    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(nullable = false)
    private String locale;

    /** Nom de l'enseigne - seul champ strictement obligatoire pour publier une fiche. */
    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    public static EntityTranslation of(UUID entityId, String locale, String name, String description) {
        EntityTranslation translation = new EntityTranslation();
        translation.entityId = entityId;
        translation.locale = locale;
        translation.name = name;
        translation.description = description;
        return translation;
    }

    /** Modifie le nom/description existants - declenche potentiellement revertToUnverified() cote service. */
    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
