package com.sunuhelp.search.repository;

import com.sunuhelp.search.document.EntitySearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Repository Spring Data Elasticsearch - meme philosophie que JpaRepository,
 * mais pour un index Elasticsearch. Les requetes complexes (recherche floue,
 * geo-distance, filtres combines) passeront par ElasticsearchOperations
 * directement dans le service, pas ici - ce repository ne couvre que le
 * CRUD simple (indexation, suppression par id).
 */
public interface EntitySearchRepository extends ElasticsearchRepository<EntitySearchDocument, String> {
}
