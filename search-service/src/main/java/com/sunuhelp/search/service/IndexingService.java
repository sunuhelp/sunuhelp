package com.sunuhelp.search.service;

import com.sunuhelp.search.client.CategoryDetailsResponse;
import com.sunuhelp.search.client.CategoryServiceClient;
import com.sunuhelp.search.client.EntityDetailsResponse;
import com.sunuhelp.search.client.EntityServiceClient;
import com.sunuhelp.search.client.OpeningHourDetailsResponse;
import com.sunuhelp.search.client.ServicePointDetailsResponse;
import com.sunuhelp.search.document.EntitySearchDocument;
import com.sunuhelp.search.repository.EntitySearchRepository;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Reconstruit entierement un ou plusieurs documents Elasticsearch a
 * partir des donnees actuelles d'entity-service et category-service -
 * jamais un patch partiel. Si l'index est perdu, une reindexation complete
 * depuis ces deux services suffit a tout reconstruire (aucune donnee
 * n'appartient reellement a search-service).
 */
@Service
public class IndexingService {

    private final EntityServiceClient entityServiceClient;
    private final CategoryServiceClient categoryServiceClient;
    private final EntitySearchRepository searchRepository;

    public IndexingService(EntityServiceClient entityServiceClient,
                            CategoryServiceClient categoryServiceClient,
                            EntitySearchRepository searchRepository) {
        this.entityServiceClient = entityServiceClient;
        this.categoryServiceClient = categoryServiceClient;
        this.searchRepository = searchRepository;
    }

    /** Reindexe tous les points de service d'une fiche - appelee sur entity-created/entity-updated. */
    public void indexEntity(UUID entityId) {
        EntityDetailsResponse entity = entityServiceClient.getEntity(entityId);
        CategoryDetailsResponse category = categoryServiceClient.getCategory(entity.getCategoryId());
        CategoryDetailsResponse parentCategory = category.getParentId() != null
                ? categoryServiceClient.getCategory(category.getParentId())
                : null;

        List<ServicePointDetailsResponse> servicePoints = entityServiceClient.getServicePoints(entityId);

        for (ServicePointDetailsResponse point : servicePoints) {
            indexServicePoint(entity, category, parentCategory, point);
        }
    }

    private void indexServicePoint(EntityDetailsResponse entity, CategoryDetailsResponse category,
                                    CategoryDetailsResponse parentCategory, ServicePointDetailsResponse point) {

        // Uniquement les points geocodes avec succes - sans coordonnees, aucune recherche geo possible.
        if (point.getLatitude() == null || point.getLongitude() == null) {
            return;
        }

        List<OpeningHourDetailsResponse> hours = entityServiceClient.getOpeningHours(point.getId());

        List<EntitySearchDocument.OpeningHourEntry> openingHours = hours.stream()
                .map(h -> EntitySearchDocument.OpeningHourEntry.builder()
                        .dayOfWeek(h.getDayOfWeek())
                        .closed(h.isClosed())
                        .openingTime(h.getOpeningTime())
                        .closingTime(h.getClosingTime())
                        .build())
                .toList();

        EntitySearchDocument document = EntitySearchDocument.builder()
                .servicePointId(point.getId().toString())
                .entityId(entity.getId().toString())
                .name(entity.getName())
                .description(entity.getDescription())
                .categorySlug(category.getSlug())
                .parentCategorySlug(parentCategory != null ? parentCategory.getSlug() : category.getSlug())
                .trustLevel(entity.getTrustLevel())
                .location(new GeoPoint(point.getLatitude().doubleValue(), point.getLongitude().doubleValue()))
                .open247(point.isOpen247())
                .temporaryStatus(point.getTemporaryStatus())
                .openingHours(openingHours)
                .viewCount(entity.getViewCount())
                .averageRating(0.0) // A brancher plus tard via un appel a review-service ou un evenement dedie.
                .reviewCount(0)
                .build();

        searchRepository.save(document);
    }

    public void deleteEntity(UUID entityId) {
        // Suppression de tous les documents lies a cette fiche - a affiner avec une requete
        // "deleteByEntityId" si le besoin se confirme (pas encore critique pour la V1).
    }
}
