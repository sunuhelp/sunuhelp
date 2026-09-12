package com.sunuhelp.geo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class GeocodeResponse {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal confidenceScore;
    /** true si le resultat vient du cache - false si un vrai appel a Nominatim vient d'etre fait. */
    private boolean fromCache;
}
