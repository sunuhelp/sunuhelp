package com.sunuhelp.review.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    private static final String TOPIC_REVIEW_CREATED = "review-created";
    private static final String TOPIC_RESPONSE_CREATED = "review-response-created";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(ReviewCreatedEvent event) {
        kafkaTemplate.send(TOPIC_REVIEW_CREATED, event.reviewId().toString(), event);
    }

    public void publish(ReviewResponseCreatedEvent event) {
        kafkaTemplate.send(TOPIC_RESPONSE_CREATED, event.reviewId().toString(), event);
    }
}
