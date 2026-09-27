package com.sunuhelp.entity.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * KafkaTemplate.send() est asynchrone - sans callback explicite, un echec
 * d'envoi ne se logue jamais nulle part, ni ne remonte a l'appelant.
 * whenComplete() rend chaque envoi observable, pour ne plus jamais
 * decouvrir un echec silencieux en production.
 */
@Component
public class EventProducer {

    private static final Logger log = LoggerFactory.getLogger(EventProducer.class);

    private static final String TOPIC_ENTITY_CREATED = "entity-created";
    private static final String TOPIC_ENTITY_UPDATED = "entity-updated";
    private static final String TOPIC_DOCUMENT_REVIEWED = "verification-document-reviewed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(EntityCreatedEvent event) {
        send(TOPIC_ENTITY_CREATED, event.entityId().toString(), event);
    }

    public void publish(EntityUpdatedEvent event) {
        send(TOPIC_ENTITY_UPDATED, event.entityId().toString(), event);
    }

    public void publish(VerificationDocumentReviewedEvent event) {
        send(TOPIC_DOCUMENT_REVIEWED, event.entityId().toString(), event);
    }

    private void send(String topic, String key, Object event) {
        kafkaTemplate.send(topic, key, event).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Echec envoi Kafka topic={} key={} : {}", topic, key, ex.getMessage(), ex);
            } else {
                log.info("Evenement publie topic={} key={} offset={}", topic, key,
                        result.getRecordMetadata().offset());
            }
        });
    }
}
