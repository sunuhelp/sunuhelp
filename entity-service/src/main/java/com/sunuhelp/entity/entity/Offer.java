package com.sunuhelp.entity.entity;

import com.sunuhelp.common.entity.BaseEntity;
import com.sunuhelp.entity.enums.Availability;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Produit ou service propose par un point de service. Entierement
 * facultative - une Entite peut publier sa fiche sans jamais creer
 * d'offre.
 */
@Entity
@Table(name = "offers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Offer extends BaseEntity {

    @Column(name = "service_point_id", nullable = false)
    private UUID servicePointId;

    /** Facultatif - certains services (plombier, consultation) n'ont pas de prix fixe affichable. */
    @Column
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column
    private Availability availability;

    /** Indicateur rapide pour l'affichage en liste - l'URL reelle vit dans media-service. */
    @Column(name = "has_photo", nullable = false)
    private boolean hasPhoto;

    public static Offer create(UUID servicePointId, BigDecimal price, Availability availability) {
        Offer offer = new Offer();
        offer.servicePointId = servicePointId;
        offer.price = price;
        offer.availability = availability;
        offer.hasPhoto = false;
        return offer;
    }

    /** Indique si cette offre a un prix fixe renseigne, par opposition a "sur devis". */
    public boolean hasFixedPrice() {
        return price != null;
    }

    /** Met a jour la disponibilite de l'offre. */
    public void updateAvailability(Availability availability) {
        this.availability = availability;
    }

    /** Marque qu'une photo a ete ajoutee via media-service. */
    public void markHasPhoto() {
        this.hasPhoto = true;
    }
}
