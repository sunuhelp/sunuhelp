package com.sunuhelp.user.service;

import com.sunuhelp.user.dto.request.CreateFavoriteRequest;
import com.sunuhelp.user.dto.response.FavoriteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FavoriteService {

    FavoriteResponse create(UUID accountId, CreateFavoriteRequest request);

    Page<FavoriteResponse> findByAccount(UUID accountId, Pageable pageable);

    /** Suppression physique, sans valeur d'audit a conserver. */
    void delete(UUID accountId, UUID entityId);
}
