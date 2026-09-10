package com.sunuhelp.user.entity;

import com.sunuhelp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Historique de recherche - suppression PHYSIQUE comme Favorite, meme
 * raisonnement. Capture aussi les filtres et la position au moment de la
 * recherche (ajout valide en audit, necessaire pour UC23 "zones actives").
 */
@Entity
@Table(name = "search_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchHistory extends BaseEntity {

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    /** Recherche par mot-cle libre - au moins un des deux (queryText/categoryId) doit etre renseigne. */
    @Column(name = "query_text")
    private String queryText;

    /** Reference logique vers Category.id (category-service). */
    @Column(name = "category_id")
    private UUID categoryId;

    @Column(name = "radius_km")
    private Integer radiusKm;

    @Column(name = "open_now_filter")
    private Boolean openNowFilter;

    @Column(name = "search_latitude")
    private BigDecimal searchLatitude;

    @Column(name = "search_longitude")
    private BigDecimal searchLongitude;

    @Column(name = "searched_at", nullable = false)
    private LocalDateTime searchedAt;

    public static SearchHistory create(UUID accountId, String queryText, UUID categoryId,
                                        Integer radiusKm, Boolean openNowFilter,
                                        BigDecimal latitude, BigDecimal longitude) {
        SearchHistory history = new SearchHistory();
        history.accountId = accountId;
        history.queryText = queryText;
        history.categoryId = categoryId;
        history.radiusKm = radiusKm;
        history.openNowFilter = openNowFilter;
        history.searchLatitude = latitude;
        history.searchLongitude = longitude;
        history.searchedAt = LocalDateTime.now();
        return history;
    }

    public boolean isKeywordSearch() {
        return queryText != null && !queryText.isBlank();
    }

    public boolean isCategorySearch() {
        return categoryId != null;
    }
}
