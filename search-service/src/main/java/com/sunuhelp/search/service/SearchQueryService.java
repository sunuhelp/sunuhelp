package com.sunuhelp.search.service;

import com.sunuhelp.search.document.EntitySearchDocument;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Construit les requetes Elasticsearch complexes (texte + filtres +
 * geo-distance) via ElasticsearchOperations directement - au-dela de ce
 * que EntitySearchRepository (CRUD simple) peut exprimer.
 */
@Service
public class SearchQueryService {

    private final ElasticsearchOperations elasticsearchOperations;

    public SearchQueryService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<EntitySearchDocument> search(String queryText, String categorySlug,
                                              Double latitude, Double longitude, double radiusKm) {

        List<Query> filters = new ArrayList<>();

        if (queryText != null && !queryText.isBlank()) {
            filters.add(Query.of(q -> q.match(m -> m.field("name").query(queryText).fuzziness("AUTO"))));
        }
        if (categorySlug != null && !categorySlug.isBlank()) {
            filters.add(Query.of(q -> q.term(t -> t.field("categorySlug").value(categorySlug))));
        }
        if (latitude != null && longitude != null) {
            filters.add(Query.of(q -> q.geoDistance(g -> g
                    .field("location")
                    .distance(radiusKm + "km")
                    .location(l -> l.latlon(ll -> ll.lat(latitude).lon(longitude))))));
        }

        Query boolQuery = Query.of(q -> q.bool(b -> b.must(filters)));

        NativeQuery nativeQuery = new NativeQueryBuilder().withQuery(boolQuery).build();

        SearchHits<EntitySearchDocument> hits = elasticsearchOperations.search(nativeQuery, EntitySearchDocument.class);

        return hits.stream().map(hit -> hit.getContent()).toList();
    }
}
