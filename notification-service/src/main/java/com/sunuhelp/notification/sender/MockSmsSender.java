package com.sunuhelp.notification.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/** Logge le SMS en console au lieu de l'envoyer reellement - cout zero en developpement. */
@Component
public class MockSmsSender implements SmsSender {

    private static final Logger log = LoggerFactory.getLogger(MockSmsSender.class);

    @Override
    public String send(String phoneNumber, String content) {
        String externalId = "mock-sms-" + UUID.randomUUID();
        log.info("[SMS MOCK] Destinataire={} | Contenu=\"{}\" | id={}", phoneNumber, content, externalId);
        return externalId;
    }
}
