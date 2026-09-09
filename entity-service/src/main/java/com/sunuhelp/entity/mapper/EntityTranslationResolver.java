package com.sunuhelp.entity.mapper;

import com.sunuhelp.entity.entity.EntityTranslation;
import com.sunuhelp.entity.repository.EntityTranslationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/** Meme logique de repli que CategoryTranslationResolver : langue demandee, sinon francais, sinon la premiere trouvee. */
@Component
public class EntityTranslationResolver {

    private static final String DEFAULT_LOCALE = "fr";

    private final EntityTranslationRepository translationRepository;

    public EntityTranslationResolver(EntityTranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    public EntityTranslation resolve(UUID entityId, String requestedLocale) {
        List<EntityTranslation> translations = translationRepository.findByEntityId(entityId);

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
