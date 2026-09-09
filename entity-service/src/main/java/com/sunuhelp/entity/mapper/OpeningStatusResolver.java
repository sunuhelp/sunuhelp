package com.sunuhelp.entity.mapper;

import com.sunuhelp.entity.entity.OpeningHours;
import com.sunuhelp.entity.entity.ServicePoint;
import com.sunuhelp.entity.enums.TemporaryStatus;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Calcule si un point de service est ouvert MAINTENANT. Vit ici, pas dans
 * ServicePoint, car elle croise plusieurs lignes de OpeningHours - une
 * methode d'entite ne peut pas faire ca seule sans acces au repository
 * (meme raisonnement que pour la resolution i18n).
 */
@Component
public class OpeningStatusResolver {

    public boolean isCurrentlyOpen(ServicePoint servicePoint, List<OpeningHours> hours) {
        if (servicePoint.getTemporaryStatus() == TemporaryStatus.TEMPORARILY_CLOSED) {
            return false;
        }
        if (servicePoint.isOpen247()) {
            return true;
        }

        DayOfWeek today = DayOfWeek.from(java.time.LocalDate.now());
        LocalTime now = LocalTime.now();

        return hours.stream()
                .filter(h -> h.getDayOfWeek() == today)
                .findFirst()
                .map(h -> h.isOpenAt(now))
                .orElse(false);
    }
}
