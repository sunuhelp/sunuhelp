package com.sunuhelp.search.service;

import com.sunuhelp.search.document.EntitySearchDocument;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Calcule "ouvert maintenant" a partir des donnees BRUTES repliquees dans
 * le document (jamais un champ stocke) - le statut change chaque minute,
 * le stocker introduirait une donnee perimee sans aucun benefice reel.
 * Version autonome de search-service, equivalent fonctionnel de
 * OpeningStatusResolver dans entity-service, mais sans dependance directe
 * entre les deux services (chacun a sa propre copie de cette logique,
 * volontairement - un appel reseau supplementaire par resultat de
 * recherche serait trop couteux).
 */
@Component
public class OpeningStatusCalculator {

    public boolean isCurrentlyOpen(EntitySearchDocument document) {
        if ("TEMPORARILY_CLOSED".equals(document.getTemporaryStatus())) {
            return false;
        }
        if (document.isOpen247()) {
            return true;
        }
        if (document.getOpeningHours() == null || document.getOpeningHours().isEmpty()) {
            return false;
        }

        DayOfWeek today = DayOfWeek.from(LocalDate.now());
        LocalTime now = LocalTime.now();

        return document.getOpeningHours().stream()
                .filter(h -> h.getDayOfWeek() == today)
                .findFirst()
                .map(h -> isOpenAt(h, now))
                .orElse(false);
    }

    private boolean isOpenAt(EntitySearchDocument.OpeningHourEntry hour, LocalTime time) {
        if (hour.isClosed() || hour.getOpeningTime() == null || hour.getClosingTime() == null) {
            return false;
        }
        return !time.isBefore(hour.getOpeningTime()) && !time.isAfter(hour.getClosingTime());
    }
}
