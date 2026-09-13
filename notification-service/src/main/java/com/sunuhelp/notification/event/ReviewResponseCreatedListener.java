package com.sunuhelp.notification.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunuhelp.notification.enums.NotificationType;
import com.sunuhelp.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Consomme review-response-created (publie par review-service).
 * reviewerAccountId est directement fourni dans l'evenement - pas besoin
 * de lookup supplementaire ici, contrairement a ReviewCreatedListener.
 */
@Component
public class ReviewResponseCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(ReviewResponseCreatedListener.class);

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public ReviewResponseCreatedListener(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "review-response-created", groupId = "notification-service")
    public void onReviewResponseCreated(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            UUID reviewerAccountId = UUID.fromString(node.get("reviewerAccountId").asText());

            // phoneNumber/email non fournis par l'evenement - meme limite que ReviewCreatedListener,
            // necessiterait un appel a auth-service pour recuperer les coordonnees reelles.
            log.info("[LIMITE CONNUE] review-response-created recu pour reviewerAccountId={} - lookup coordonnees non implemente",
                    reviewerAccountId);
        } catch (Exception e) {
            log.error("Echec traitement review-response-created : {}", message, e);
        }
    }
}
