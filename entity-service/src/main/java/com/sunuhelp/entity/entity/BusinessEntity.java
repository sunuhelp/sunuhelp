package com.sunuhelp.entity.entity;

import com.sunuhelp.common.entity.BaseEntity;
import com.sunuhelp.entity.enums.PersonType;
import com.sunuhelp.entity.enums.TrustLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Fiche d'une Entite (commerce, etablissement, service...). Nommee
 * BusinessEntity plutot que "Entity" pour eviter le conflit avec
 * l'annotation @Entity de JPA (jakarta.persistence.Entity).
 * Ne contient que les donnees langue-neutres - nom et description
 * vivent dans EntityTranslation.
 */
@Entity
@Table(name = "entities")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessEntity extends BaseEntity {

    /** Reference logique vers Account.id (auth-service) - jamais de relation JPA directe. */
    @Column(name = "owner_account_id", nullable = false)
    private UUID ownerAccountId;

    /** Personne physique ou morale - determine les pieces justificatives demandees. */
    @Enumerated(EnumType.STRING)
    @Column(name = "person_type", nullable = false)
    private PersonType personType;

    /** Reference vers media-service - jamais le fichier lui-meme stocke ici. */
    @Column(name = "logo_url")
    private String logoUrl;

    /** Badge de confiance affiche en tete de fiche - VERIFIED uniquement apres validation Admin. */
    @Enumerated(EnumType.STRING)
    @Column(name = "trust_level", nullable = false)
    private TrustLevel trustLevel;

    /** Nombre de consultations de la fiche - alimente les statistiques simples (UC17). */
    @Column(name = "view_count", nullable = false)
    private long viewCount;

    /** Nombre d'apparitions dans des resultats de recherche - incremente par search-service. */
    @Column(name = "search_appearance_count", nullable = false)
    private long searchAppearanceCount;

    /**
     * Fabrique unique de creation - impose l'etat initial correct
     * (non verifiee, compteurs a zero) des la creation.
     */
    public static BusinessEntity create(UUID ownerAccountId, PersonType personType, String logoUrl) {
        BusinessEntity entity = new BusinessEntity();
        entity.ownerAccountId = ownerAccountId;
        entity.personType = personType;
        entity.logoUrl = logoUrl;
        entity.trustLevel = TrustLevel.UNVERIFIED;
        entity.viewCount = 0;
        entity.searchAppearanceCount = 0;
        return entity;
    }

    /** Accorde le badge Verifiee - appelee uniquement apres approbation d'un document par un Admin. */
    public void verify() {
        this.trustLevel = TrustLevel.VERIFIED;
    }

    /**
     * Retire le badge Verifiee - declenchee automatiquement quand un champ
     * critique (nom, adresse) change sur une fiche deja verifiee, pour
     * eviter qu'un badge reste affiche apres une modification non controlee.
     */
    public void revertToUnverified() {
        this.trustLevel = TrustLevel.UNVERIFIED;
    }

    /** Incremente le compteur de vues - appelee a chaque consultation de la fiche. */
    public void incrementViewCount() {
        this.viewCount++;
    }

    /** Incremente le compteur d'apparitions en recherche - appelee par evenement depuis search-service. */
    public void incrementSearchAppearanceCount() {
        this.searchAppearanceCount++;
    }

    /** Desactivation logique de toute la fiche (et, en cascade logique, de ses points de service). */
    /** Met a jour le logo - reference vers media-service. */
    public void updateLogo(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void deactivate() {
        this.softDelete();
    }
}
