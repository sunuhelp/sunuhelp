package com.sunuhelp.search.controller;

import com.sunuhelp.search.document.EntitySearchDocument;
import com.sunuhelp.search.dto.SearchResultResponse;
import com.sunuhelp.search.service.OpeningStatusCalculator;
import com.sunuhelp.search.service.SearchQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Recherche")
public class SearchController {

    private final SearchQueryService searchQueryService;
    private final OpeningStatusCalculator openingStatusCalculator;

    public SearchController(SearchQueryService searchQueryService,
                             OpeningStatusCalculator openingStatusCalculator) {
        this.searchQueryService = searchQueryService;
        this.openingStatusCalculator = openingStatusCalculator;
    }

    @GetMapping("/api/v1/search")
    @Operation(summary = "Recherche par mot-cle, categorie, position et distance - public")
    public List<SearchResultResponse> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String categorySlug,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false, defaultValue = "5") double radiusKm,
            @RequestParam(required = false, defaultValue = "false") boolean openNowOnly) {

        List<EntitySearchDocument> documents = searchQueryService.search(
                query, categorySlug, latitude, longitude, radiusKm);

        return documents.stream()
                .map(doc -> {
                    boolean openNow = openingStatusCalculator.isCurrentlyOpen(doc);
                    return toResponse(doc, openNow);
                })
                .filter(r -> !openNowOnly || r.isOpenNow())
                .toList();
    }

    private SearchResultResponse toResponse(EntitySearchDocument doc, boolean openNow) {
        return SearchResultResponse.builder()
                .servicePointId(doc.getServicePointId())
                .entityId(doc.getEntityId())
                .name(doc.getName())
                .description(doc.getDescription())
                .categorySlug(doc.getCategorySlug())
                .trustLevel(doc.getTrustLevel())
                .latitude(doc.getLocation().getLat())
                .longitude(doc.getLocation().getLon())
                .openNow(openNow)
                .averageRating(doc.getAverageRating())
                .reviewCount(doc.getReviewCount())
                .build();
    }
}
