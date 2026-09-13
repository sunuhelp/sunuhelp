package com.sunuhelp.notification.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunuhelp.notification.enums.NotificationType;
import com.sunuhelp.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** Consomme verification-document-reviewed (publie par entity-service). Meme limite de lookup que les avis. */
@Component
public class VerificationDocumentReviewedListener {

    private static final Logger log = LoggerFactory.getLogger(VerificationDocumentReviewedListener.class);

    private final ObjectMapper objectMapper;

    public VerificationDocumentReviewedListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "verification-document-reviewed", groupId = "notification-service")
    public void onDocumentReviewed(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            log.info("[LIMITE CONNUE] verification-document-reviewed recu pour entityId={}, status={} - lookup proprietaire non implemente",
                    node.get("entityId").asText(), node.get("status").asText());
        } catch (Exception e) {
            log.error("Echec traitement verification-document-reviewed : {}", message, e);
        }
    }
}
