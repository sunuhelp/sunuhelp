package com.sunuhelp.entity.entity;

import com.sunuhelp.common.entity.BaseEntity;
import com.sunuhelp.entity.enums.GeocodingStatus;
import com.sunuhelp.entity.enums.ServicePointType;
import com.sunuhelp.entity.enums.TemporaryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Point de service physique ou en ligne d'une Entite. Une Entite peut
 * avoir plusieurs points de service (ex. plusieurs agences).
 */
@Entity
@Table(name = "service_points")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServicePoint extends BaseEntity {

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    /** Nom du point - obligatoire meme pour une Entite a point unique, pour permettre l'ajout futur d'autres points sans confusion. */
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServicePointType type;

    /** Obligatoire si type = PHYSICAL. */
    @Column
    private String address;

    /** Obligatoire si type = ONLINE (ex. "Dakar et banlieue" pour un service de livraison). */
    @Column(name = "coverage_zone")
    private String coverageZone;

    @Column
    private BigDecimal latitude;

    @Column
    private BigDecimal longitude;

    /** Bascule "Ouvert 24h/24, 7j/7" - dispense de renseigner des horaires detailles. */
    @Column(name = "is_open_247", nullable = false)
    private boolean isOpen247;

    /** Statut temporaire (fermeture exceptionnelle, disponibilite partielle) - plus robuste qu'un simple booleen. */
    @Enumerated(EnumType.STRING)
    @Column(name = "temporary_status", nullable = false)
    private TemporaryStatus temporaryStatus;

    /** Date d'expiration du statut temporaire - evite qu'il reste actif indefiniment par oubli. */
    @Column(name = "temporary_status_until")
    private LocalDateTime temporaryStatusUntil;

    /** Resultat du dernier geocodage tente par geo-service. */
    @Enumerated(EnumType.STRING)
    @Column(name = "geocoding_status", nullable = false)
    private GeocodingStatus geocodingStatus;

    public static ServicePoint create(UUID entityId, String name, ServicePointType type,
                                       String address, String coverageZone) {
        ServicePoint point = new ServicePoint();
        point.entityId = entityId;
        point.name = name;
        point.type = type;
        point.address = address;
        point.coverageZone = coverageZone;
        point.isOpen247 = false;
        point.temporaryStatus = TemporaryStatus.NONE;
        point.geocodingStatus = GeocodingStatus.PENDING;
        return point;
    }

    /** Definit un statut temporaire avec sa date d'expiration. */
    public void setTemporaryStatus(TemporaryStatus status, LocalDateTime until) {
        this.temporaryStatus = status;
        this.temporaryStatusUntil = until;
    }

    /** Revient a l'etat normal une fois la date d'expiration du statut temporaire depassee. */
    public void clearExpiredTemporaryStatus() {
        this.temporaryStatus = TemporaryStatus.NONE;
        this.temporaryStatusUntil = null;
    }

    /** Enregistre le resultat reussi d'un geocodage par geo-service. */
    public void markGeocoded(BigDecimal lat, BigDecimal lon) {
        this.latitude = lat;
        this.longitude = lon;
        this.geocodingStatus = GeocodingStatus.SUCCESS;
    }

    /** Enregistre l'echec du geocodage - la fiche reste publiable mais invisible en recherche par proximite. */
    public void markGeocodingFailed() {
        this.geocodingStatus = GeocodingStatus.FAILED;
    }
}
