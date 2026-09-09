package com.sunuhelp.entity.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** Traduction d'une offre (intitule, description) - meme pattern que les autres traductions. */
@Entity
@Table(name = "offer_translations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfferTranslation extends BaseEntity {

    @Column(name = "offer_id", nullable = false)
    private UUID offerId;

    @Column(nullable = false)
    private String locale;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    public static OfferTranslation of(UUID offerId, String locale, String title, String description) {
        OfferTranslation translation = new OfferTranslation();
        translation.offerId = offerId;
        translation.locale = locale;
        translation.title = title;
        translation.description = description;
        return translation;
    }
}
