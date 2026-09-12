package com.sunuhelp.geo.service;

import com.sunuhelp.geo.dto.response.GeocodeResponse;

public interface GeocodingService {

    /**
     * Verifie le cache d'abord ; si absent ou expire, interroge Nominatim.
     * Ne met en cache QUE les resultats fiables (confidenceScore >= 0.85) -
     * un resultat peu fiable est renvoye au client mais jamais reutilise
     * pour une autre adresse similaire (evite de propager une erreur).
     */
    GeocodeResponse geocode(String address);
}
