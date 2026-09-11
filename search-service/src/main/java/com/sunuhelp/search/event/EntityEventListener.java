package com.sunuhelp.search.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunuhelp.search.service.IndexingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Consomme le message Kafka comme texte brut, extrait juste "entityId" -
 * ne tente jamais de deserialiser vers la classe Java d'origine
 * (EntityCreatedEvent d'entity-service), qui n'existe pas dans ce module.
 */
@Component
public class EntityEventListener {

    private static final Logger log = LoggerFactory.getLogger(EntityEventListener.class);

    private final IndexingService indexingService;
    private final ObjectMapper objectMapper;

    public EntityEventListener(IndexingService indexingService, ObjectMapper objectMapper) {
        this.indexingService = indexingService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "entity-created", groupId = "search-service")
    public void onEntityCreated(String message) {
        indexFromMessage(message);
    }

    @KafkaListener(topics = "entity-updated", groupId = "search-service")
    public void onEntityUpdated(String message) {
        indexFromMessage(message);
    }

    private void indexFromMessage(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            UUID entityId = UUID.fromString(node.get("entityId").asText());
            indexingService.indexEntity(entityId);
        } catch (Exception e) {
            log.error("Echec de l'indexation depuis le message Kafka : {}", message, e);
        }
    }
}
