package com.sunuhelp.user.service;

import com.sunuhelp.user.dto.request.CreateSearchHistoryRequest;
import com.sunuhelp.user.dto.response.SearchHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SearchHistoryService {

    /** N'enregistre rien si historyTrackingEnabled = false sur le profil - respecte le consentement. */
    void record(UUID accountId, CreateSearchHistoryRequest request);

    Page<SearchHistoryResponse> findByAccount(UUID accountId, Pageable pageable);
}
