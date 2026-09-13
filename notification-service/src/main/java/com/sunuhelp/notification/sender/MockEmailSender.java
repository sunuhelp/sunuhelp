package com.sunuhelp.notification.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockEmailSender implements EmailSender {

    private static final Logger log = LoggerFactory.getLogger(MockEmailSender.class);

    @Override
    public String send(String email, String subject, String content) {
        String externalId = "mock-email-" + UUID.randomUUID();
        log.info("[EMAIL MOCK] Destinataire={} | Sujet=\"{}\" | Contenu=\"{}\" | id={}",
                email, subject, content, externalId);
        return externalId;
    }
}
