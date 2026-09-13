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
 * Consomme otp-requested (publie par auth-service). Meme pattern que
 * search-service : message consomme en texte brut, jamais desserialise
 * vers la classe Java d'origine (OtpRequestedEvent, inexistante ici).
 */
@Component
public class OtpRequestedListener {

    private static final Logger log = LoggerFactory.getLogger(OtpRequestedListener.class);

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public OtpRequestedListener(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "otp-requested", groupId = "notification-service")
    public void onOtpRequested(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            UUID accountId = UUID.fromString(node.get("accountId").asText());
            String phoneNumber = node.get("phoneNumber").asText();
            String rawCode = node.get("rawCode").asText();

            notificationService.send(accountId, NotificationType.OTP, phoneNumber, null, "fr", rawCode);
        } catch (Exception e) {
            log.error("Echec traitement otp-requested : {}", message, e);
        }
    }
}
