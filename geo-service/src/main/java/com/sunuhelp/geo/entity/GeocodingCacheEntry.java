package com.sunuhelp.geo.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Cache des resultats de geocodage - indispensable ici, pas juste pour la
 * performance : Nominatim (OpenStreetMap) impose 1 requete/seconde max,
 * le cache evite de re-interroger l'API externe pour une adresse deja
 * connue.
 */
@Entity
@Table(name = "geocoding_cache")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeocodingCacheEntry extends BaseEntity {

    @Column(name = "address_query", nullable = false, unique = true)
    private String addressQuery;

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @Column(nullable = false)
    private String provider;

    @Column(name = "confidence_score")
    private BigDecimal confidenceScore;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public static GeocodingCacheEntry create(String addressQuery, BigDecimal latitude, BigDecimal longitude,
                                              String provider, BigDecimal confidenceScore) {
        GeocodingCacheEntry entry = new GeocodingCacheEntry();
        entry.addressQuery = addressQuery;
        entry.latitude = latitude;
        entry.longitude = longitude;
        entry.provider = provider;
        entry.confidenceScore = confidenceScore;
        entry.expiresAt = LocalDateTime.now().plusMonths(6);
        return entry;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    /** Seuil de confiance a 0.85 : sous ce seuil, le resultat n'est jamais mis en cache (decide en modelisation). */
    public boolean isReliable() {
        return confidenceScore != null && confidenceScore.compareTo(new BigDecimal("0.85")) >= 0;
    }
}
