package com.sunuhelp.notification.service.impl;

import com.sunuhelp.notification.entity.Notification;
import com.sunuhelp.notification.entity.NotificationPreference;
import com.sunuhelp.notification.enums.Channel;
import com.sunuhelp.notification.enums.NotificationType;
import com.sunuhelp.notification.i18n.MessageKeys;
import com.sunuhelp.notification.repository.NotificationPreferenceRepository;
import com.sunuhelp.notification.repository.NotificationRepository;
import com.sunuhelp.notification.sender.EmailSender;
import com.sunuhelp.notification.sender.SmsSender;
import com.sunuhelp.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final SmsSender smsSender;
    private final EmailSender emailSender;
    private final MessageSource messageSource;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                    NotificationPreferenceRepository preferenceRepository,
                                    SmsSender smsSender, EmailSender emailSender,
                                    MessageSource messageSource) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
        this.smsSender = smsSender;
        this.emailSender = emailSender;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public void send(UUID accountId, NotificationType type, String phoneNumber, String email,
                      String locale, Object... messageArgs) {

        boolean critical = type == NotificationType.OTP || type == NotificationType.ACCOUNT_SUSPENDED;
        Locale resolvedLocale = Locale.forLanguageTag(locale != null ? locale : "fr");

        String templateKey = resolveTemplateKey(type);
        String content = messageSource.getMessage(templateKey, messageArgs, resolvedLocale);

        boolean smsAllowed = critical || isChannelEnabled(accountId, type, true);
        boolean emailAllowed = critical || isChannelEnabled(accountId, type, false);

        if (phoneNumber != null && smsAllowed) {
            sendViaChannel(accountId, Channel.SMS, type, phoneNumber, null, content, locale);
        }
        if (email != null && emailAllowed) {
            sendViaChannel(accountId, Channel.EMAIL, type, email, "SunuHelp", content, locale);
        }
    }

    private void sendViaChannel(UUID accountId, Channel channel, NotificationType type,
                                 String recipient, String subject, String content, String locale) {
        Notification notification = Notification.create(
                accountId, channel, type, recipient, locale != null ? locale : "fr", subject, content);
        notificationRepository.save(notification);

        try {
            String externalId = channel == Channel.SMS
                    ? smsSender.send(recipient, content)
                    : emailSender.send(recipient, subject, content);
            notification.markSent(channel == Channel.SMS ? "mock-sms" : "mock-email", externalId);
        } catch (Exception e) {
            log.error("Echec envoi notification {} vers {} : {}", type, recipient, e.getMessage());
            notification.markFailed(e.getMessage());
            if (notification.canRetry()) {
                notification.scheduleRetry();
            }
        }
        notificationRepository.save(notification);
    }

    private boolean isChannelEnabled(UUID accountId, NotificationType type, boolean sms) {
        return preferenceRepository.findByAccountIdAndNotificationType(accountId, type)
                .map(p -> sms ? p.isSmsEnabled() : p.isEmailEnabled())
                .orElse(true); // Pas de preference enregistree = comportement par defaut actif.
    }

    private String resolveTemplateKey(NotificationType type) {
        return switch (type) {
            case OTP -> MessageKeys.TEMPLATE_OTP;
            case EMAIL_VERIFICATION -> MessageKeys.TEMPLATE_EMAIL_VERIFICATION;
            case NEW_REVIEW -> MessageKeys.TEMPLATE_NEW_REVIEW;
            case REVIEW_RESPONSE -> MessageKeys.TEMPLATE_REVIEW_RESPONSE;
            case DOCUMENT_APPROVED -> MessageKeys.TEMPLATE_DOCUMENT_APPROVED;
            case DOCUMENT_REJECTED -> MessageKeys.TEMPLATE_DOCUMENT_REJECTED;
            default -> throw new IllegalArgumentException("Pas de template pour " + type);
        };
    }
}
