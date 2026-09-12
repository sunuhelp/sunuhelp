package com.sunuhelp.geo.client;

import org.springframework.stereotype.Component;

/**
 * Impose un delai minimum d'1.1 seconde entre deux appels sortants vers
 * Nominatim - marge de securite au-dessus de la limite stricte d'1
 * requete/seconde imposee par sa politique d'usage. Synchronise pour
 * garantir le respect du delai meme en cas d'appels concurrents.
 */
@Component
public class NominatimRateLimiter {

    private static final long MIN_INTERVAL_MS = 1100;

    private long lastCallTimestamp = 0;

    public synchronized void waitForNextSlot() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastCallTimestamp;
        if (elapsed < MIN_INTERVAL_MS) {
            try {
                Thread.sleep(MIN_INTERVAL_MS - elapsed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        lastCallTimestamp = System.currentTimeMillis();
    }
}
