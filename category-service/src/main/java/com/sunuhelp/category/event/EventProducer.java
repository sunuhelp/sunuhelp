package com.sunuhelp.category.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    private static final String TOPIC_CATEGORY_MERGED = "category-merged";
    private static final String TOPIC_CATEGORY_DELETED = "category-deleted";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(CategoryMergedEvent event) {
        kafkaTemplate.send(TOPIC_CATEGORY_MERGED, event.sourceCategoryId().toString(), event);
    }

    public void publish(CategoryDeletedEvent event) {
        kafkaTemplate.send(TOPIC_CATEGORY_DELETED, event.categoryId().toString(), event);
    }
}
