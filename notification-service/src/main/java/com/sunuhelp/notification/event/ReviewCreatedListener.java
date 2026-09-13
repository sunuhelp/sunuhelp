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
 * Consomme review-created (publie par review-service). Simplification
 * volontaire ici : notifie le compte "entityId" comme s'il etait le
 * proprietaire - un vrai lookup vers entity-service (proprietaire reel
 * de la fiche) serait necessaire en production, note comme limite connue.
 */
@Component
public class ReviewCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(ReviewCreatedListener.class);

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public ReviewCreatedListener(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "review-created", groupId = "notification-service")
    public void onReviewCreated(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            // TODO : recuperer le vrai owner_account_id via un appel a entity-service -
            // limite connue de la V1, non bloquante pour valider le mecanisme d'evenement.
            log.info("[LIMITE CONNUE] review-created recu pour entityId={} - lookup proprietaire non implemente",
                    node.get("entityId").asText());
        } catch (Exception e) {
            log.error("Echec traitement review-created : {}", message, e);
        }
    }
}
