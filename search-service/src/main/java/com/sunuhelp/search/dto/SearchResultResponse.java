package com.sunuhelp.search.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResultResponse {
    private String servicePointId;
    private String entityId;
    private String name;
    private String description;
    private String categorySlug;
    private String trustLevel;
    private double latitude;
    private double longitude;
    /** Calcule au moment de la requete, jamais stocke - toujours exact a la seconde. */
    private boolean openNow;
    private double averageRating;
    private int reviewCount;
}
