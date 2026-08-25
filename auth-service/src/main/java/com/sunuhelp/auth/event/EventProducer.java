package com.sunuhelp.auth.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/** Point de sortie unique de tous les evenements Kafka publies par auth-service. */
@Component
public class EventProducer {

    private static final String TOPIC_OTP_REQUESTED = "otp-requested";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(OtpRequestedEvent event) {
        kafkaTemplate.send(TOPIC_OTP_REQUESTED, event.accountId().toString(), event);
    }
}
