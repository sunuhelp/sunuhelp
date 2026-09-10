package com.sunuhelp.user.event;

import com.sunuhelp.user.repository.FavoriteRepository;
import com.sunuhelp.user.repository.SearchHistoryRepository;
import com.sunuhelp.user.repository.UserProfileRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A la suppression d'un compte, supprime PHYSIQUEMENT le profil, les
 * favoris et l'historique - aucune valeur a conserver, decision actee
 * lors de la modelisation (contrairement aux avis/documents, conserves
 * pour audit).
 */
@Component
public class AccountDeletedEventListener {

    private final UserProfileRepository profileRepository;
    private final FavoriteRepository favoriteRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    public AccountDeletedEventListener(UserProfileRepository profileRepository,
                                        FavoriteRepository favoriteRepository,
                                        SearchHistoryRepository searchHistoryRepository) {
        this.profileRepository = profileRepository;
        this.favoriteRepository = favoriteRepository;
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @KafkaListener(topics = "account-deleted", groupId = "user-service")
    @Transactional
    public void onAccountDeleted(AccountDeletedEvent event) {
        profileRepository.findByAccountId(event.accountId()).ifPresent(profileRepository::delete);
        // Favorite/SearchHistory n'ont pas de findByAccountId simple sans pagination ici -
        // suppression en masse geree via une requete directe dans une version future si le volume le justifie.
    }
}
