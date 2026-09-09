package com.sunuhelp.entity.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Horaire d'un jour de la semaine pour un point de service. Le statut
 * "Ouvert/Ferme" affiche en tete de fiche est calcule a la volee cote
 * service, jamais stocke ici.
 */
@Entity
@Table(name = "opening_hours")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpeningHours extends BaseEntity {

    @Column(name = "service_point_id", nullable = false)
    private UUID servicePointId;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "is_closed", nullable = false)
    private boolean isClosed;

    /** Requis si isClosed = false. */
    @Column(name = "opening_time")
    private LocalTime openingTime;

    /** Requis si isClosed = false. */
    @Column(name = "closing_time")
    private LocalTime closingTime;

    public static OpeningHours create(UUID servicePointId, DayOfWeek dayOfWeek, boolean isClosed,
                                       LocalTime openingTime, LocalTime closingTime) {
        OpeningHours hours = new OpeningHours();
        hours.servicePointId = servicePointId;
        hours.dayOfWeek = dayOfWeek;
        hours.isClosed = isClosed;
        hours.openingTime = openingTime;
        hours.closingTime = closingTime;
        return hours;
    }

    /** Indique si ce jour est ouvert a l'heure donnee - utilise par la logique de calcul du statut affiche. */
    public boolean isOpenAt(LocalTime time) {
        if (isClosed || openingTime == null || closingTime == null) {
            return false;
        }
        return !time.isBefore(openingTime) && !time.isAfter(closingTime);
    }
}
