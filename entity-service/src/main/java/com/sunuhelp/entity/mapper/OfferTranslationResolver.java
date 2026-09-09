package com.sunuhelp.entity.mapper;

import com.sunuhelp.entity.entity.OfferTranslation;
import com.sunuhelp.entity.repository.OfferTranslationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OfferTranslationResolver {

    private static final String DEFAULT_LOCALE = "fr";

    private final OfferTranslationRepository translationRepository;

    public OfferTranslationResolver(OfferTranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    public OfferTranslation resolve(UUID offerId, String requestedLocale) {
        List<OfferTranslation> translations = translationRepository.findByOfferId(offerId);

        return translations.stream()
                .filter(t -> t.getLocale().equals(requestedLocale))
                .findFirst()
                .or(() -> translations.stream()
                        .filter(t -> DEFAULT_LOCALE.equals(t.getLocale()))
                        .findFirst())
                .or(() -> translations.stream().findFirst())
                .orElseThrow();
    }
}
