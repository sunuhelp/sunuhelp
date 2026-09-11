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
import org.springframework.data.geo.Point;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Copie de lecture d'une fiche, jamais la source de verite (qui reste
 * entity-service). Reconstruit entierement a chaque evenement
 * entity-created/entity-updated - si l'index est perdu, rien n'est perdu
 * definitivement, on peut tout reindexer depuis entity-service.
 *
 * openingHours est copie en donnees BRUTES : le statut "ouvert maintenant"
 * n'est jamais stocke ici, toujours calcule au moment de la recherche
 * (voir OpeningStatusCalculator) - evite toute donnee perimee sur un champ
 * qui change a chaque minute.
 */
@Document(indexName = "entities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntitySearchDocument {

    @Id
    private String entityId;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    /** Utilise pour le filtre "categorie" - recherche exacte, pas de tolerance aux fautes ici. */
    @Field(type = FieldType.Keyword)
    private String categorySlug;

    /** Utilise pour le filtre "categorie large" (ex. toute la branche Sante) - voir modelisation initiale. */
    @Field(type = FieldType.Keyword)
    private String parentCategorySlug;

    @Field(type = FieldType.Keyword)
    private String trustLevel;

    @GeoPointField
    private Point location;

    @Field(type = FieldType.Boolean)
    private boolean open247;

    /** Statuts temporaires actifs - une entree par point de service, pour gerer le cas multi-points. */
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
