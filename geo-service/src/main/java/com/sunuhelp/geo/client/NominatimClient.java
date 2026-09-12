package com.sunuhelp.geo.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Appelle l'API publique Nominatim (OpenStreetMap). Respecte sa politique
 * d'usage : User-Agent identifiable obligatoire, maximum 1 requete/seconde
 * (voir RateLimiter), aucun usage en masse - le cache PostgreSQL en amont
 * est le principal mecanisme qui rend ce respect possible en pratique.
 * Politique complete : https://operations.osmfoundation.org/policies/nominatim/
 */
@Component
public class NominatimClient {

    private static final String BASE_URL = "https://nominatim.openstreetmap.org";
    private static final String USER_AGENT = "SunuHelp/1.0 (projet academique ; contact: sunuhelp@gmail.com)";

    private final RestClient restClient;

    public NominatimClient() {
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("User-Agent", USER_AGENT)
                .build();
    }

    public List<NominatimResult> search(String address) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", address)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<NominatimResult>>() {});
    }
}
