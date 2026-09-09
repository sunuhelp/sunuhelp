package com.sunuhelp.entity.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    private static final String TOPIC_ENTITY_CREATED = "entity-created";
    private static final String TOPIC_ENTITY_UPDATED = "entity-updated";
    private static final String TOPIC_DOCUMENT_REVIEWED = "verification-document-reviewed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(EntityCreatedEvent event) {
        kafkaTemplate.send(TOPIC_ENTITY_CREATED, event.entityId().toString(), event);
    }

    public void publish(EntityUpdatedEvent event) {
        kafkaTemplate.send(TOPIC_ENTITY_UPDATED, event.entityId().toString(), event);
    }

    public void publish(VerificationDocumentReviewedEvent event) {
        kafkaTemplate.send(TOPIC_DOCUMENT_REVIEWED, event.entityId().toString(), event);
    }
}
