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
 * avoir plusieurs points de service (ex. plusieurs agences), chacun avec
 * son propre numero de contact - plus precis qu'un numero unique au
 * niveau de la fiche entiere.
 */
@Entity
@Table(name = "service_points")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServicePoint extends BaseEntity {

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServicePointType type;

    @Column
    private String address;

    @Column(name = "coverage_zone")
    private String coverageZone;

    @Column
    private BigDecimal latitude;

    @Column
    private BigDecimal longitude;

    @Column(name = "is_open_247", nullable = false)
    private boolean isOpen247;

    @Enumerated(EnumType.STRING)
    @Column(name = "temporary_status", nullable = false)
    private TemporaryStatus temporaryStatus;

    @Column(name = "temporary_status_until")
    private LocalDateTime temporaryStatusUntil;

    @Enumerated(EnumType.STRING)
    @Column(name = "geocoding_status", nullable = false)
    private GeocodingStatus geocodingStatus;

    /** Facultatif, comme le reste des champs de contact - jamais bloquant pour publier une fiche. */
    @Column(name = "phone_number")
    private String phoneNumber;

    public static ServicePoint create(UUID entityId, String name, ServicePointType type,
                                       String address, String coverageZone, String phoneNumber) {
        ServicePoint point = new ServicePoint();
        point.entityId = entityId;
        point.name = name;
        point.type = type;
        point.address = address;
        point.coverageZone = coverageZone;
        point.phoneNumber = phoneNumber;
        point.isOpen247 = false;
        point.temporaryStatus = TemporaryStatus.NONE;
        point.geocodingStatus = GeocodingStatus.PENDING;
        return point;
    }

    public void setTemporaryStatus(TemporaryStatus status, LocalDateTime until) {
        this.temporaryStatus = status;
        this.temporaryStatusUntil = until;
    }

    public void clearExpiredTemporaryStatus() {
        this.temporaryStatus = TemporaryStatus.NONE;
        this.temporaryStatusUntil = null;
    }

    public void markGeocoded(BigDecimal lat, BigDecimal lon) {
        this.latitude = lat;
        this.longitude = lon;
        this.geocodingStatus = GeocodingStatus.SUCCESS;
    }

    public void markGeocodingFailed() {
        this.geocodingStatus = GeocodingStatus.FAILED;
    }

    /** Permet de renseigner ou corriger le numero apres la creation initiale. */
    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
