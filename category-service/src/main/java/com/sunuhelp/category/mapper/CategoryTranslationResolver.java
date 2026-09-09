package com.sunuhelp.category.mapper;

import com.sunuhelp.category.entity.CategoryTranslation;
import com.sunuhelp.category.repository.CategoryTranslationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Resout la meilleure traduction disponible pour une categorie : la langue
 * demandee si elle existe, sinon le francais, sinon la premiere traduction
 * trouvee (jamais de champ vide renvoye au client). C'est ici, pas dans
 * l'entite, que vit cette regle - elle a besoin d'interroger plusieurs
 * lignes de CategoryTranslation, ce qu'une methode d'entite ne peut pas
 * faire seule sans acces au repository.
 */
@Component
public class CategoryTranslationResolver {

    private static final String DEFAULT_LOCALE = "fr";

    private final CategoryTranslationRepository translationRepository;

    public CategoryTranslationResolver(CategoryTranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    public CategoryTranslation resolve(UUID categoryId, String requestedLocale) {
        List<CategoryTranslation> translations = translationRepository.findByCategoryId(categoryId);

        return translations.stream()
                .filter(t -> t.getLocale().equals(requestedLocale))
                .findFirst()
                .or(() -> translations.stream()
                        .filter(CategoryTranslation::isDefaultLocale)
                        .findFirst())
                .or(() -> translations.stream().findFirst())
                .orElseThrow(); // ne devrait jamais arriver : au moins 1 traduction obligatoire a la creation
    }
}
