package com.sunuhelp.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Un document par POINT DE SERVICE, pas par fiche - une fiche avec 3
 * agences produit 3 documents distincts, chacun avec sa propre position.
 * Copie de lecture, jamais la source de verite (entity-service).
 *
 * openingHours contient les donnees BRUTES : le statut "ouvert maintenant"
 * n'est jamais stocke, toujours calcule au moment de la recherche
 * (OpeningStatusCalculator) - evite une donnee perimee sur un champ qui
 * change chaque minute.
 */
@Document(indexName = "service_points")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntitySearchDocument {

    @Id
    private String servicePointId;

    @Field(type = FieldType.Keyword)
    private String entityId;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String categorySlug;

    @Field(type = FieldType.Keyword)
    private String parentCategorySlug;

    @Field(type = FieldType.Keyword)
    private String trustLevel;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Boolean)
    private boolean open247;

    @Field(type = FieldType.Keyword)
    private String temporaryStatus;

    @Field(type = FieldType.Nested)
    private List<OpeningHourEntry> openingHours;

    @Field(type = FieldType.Long)
    private long viewCount;

    @Field(type = FieldType.Double)
    private double averageRating;

    @Field(type = FieldType.Integer)
    private int reviewCount;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OpeningHourEntry {
        private DayOfWeek dayOfWeek;
        private boolean closed;
        private LocalTime openingTime;
        private LocalTime closingTime;
    }
}
