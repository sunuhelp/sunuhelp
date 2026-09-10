package com.sunuhelp.user.service.impl;

import com.sunuhelp.user.dto.request.CreateSearchHistoryRequest;
import com.sunuhelp.user.dto.response.SearchHistoryResponse;
import com.sunuhelp.user.entity.SearchHistory;
import com.sunuhelp.user.entity.UserProfile;
import com.sunuhelp.user.exception.SearchCriteriaRequiredException;
import com.sunuhelp.user.mapper.SearchHistoryMapper;
import com.sunuhelp.user.repository.SearchHistoryRepository;
import com.sunuhelp.user.repository.UserProfileRepository;
import com.sunuhelp.user.service.SearchHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final UserProfileRepository profileRepository;
    private final SearchHistoryMapper searchHistoryMapper;

    public SearchHistoryServiceImpl(SearchHistoryRepository searchHistoryRepository,
                                     UserProfileRepository profileRepository,
                                     SearchHistoryMapper searchHistoryMapper) {
        this.searchHistoryRepository = searchHistoryRepository;
        this.profileRepository = profileRepository;
        this.searchHistoryMapper = searchHistoryMapper;
    }

    @Override
    @Transactional
    public void record(UUID accountId, CreateSearchHistoryRequest request) {
        boolean hasKeyword = request.getQueryText() != null && !request.getQueryText().isBlank();
        boolean hasCategory = request.getCategoryId() != null;
        if (!hasKeyword && !hasCategory) {
            throw new SearchCriteriaRequiredException();
        }

        boolean trackingEnabled = profileRepository.findByAccountId(accountId)
                .map(UserProfile::isHistoryTrackingEnabled)
                .orElse(true); // pas de profil = comportement par defaut (true), coherent avec create().

        if (!trackingEnabled) {
            return; // Respecte le consentement - aucune ligne enregistree.
        }

        SearchHistory history = SearchHistory.create(accountId, request.getQueryText(), request.getCategoryId(),
                request.getRadiusKm(), request.getOpenNowFilter(),
                request.getSearchLatitude(), request.getSearchLongitude());
        searchHistoryRepository.save(history);
    }

    @Override
    public Page<SearchHistoryResponse> findByAccount(UUID accountId, Pageable pageable) {
        return searchHistoryRepository.findByAccountIdOrderBySearchedAtDesc(accountId, pageable)
                .map(searchHistoryMapper::toResponse);
    }
}
