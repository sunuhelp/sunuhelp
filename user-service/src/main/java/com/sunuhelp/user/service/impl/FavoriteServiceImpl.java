package com.sunuhelp.user.service.impl;

import com.sunuhelp.user.dto.request.CreateFavoriteRequest;
import com.sunuhelp.user.dto.response.FavoriteResponse;
import com.sunuhelp.user.entity.Favorite;
import com.sunuhelp.user.exception.FavoriteAlreadyExistsException;
import com.sunuhelp.user.exception.FavoriteNotFoundException;
import com.sunuhelp.user.mapper.FavoriteMapper;
import com.sunuhelp.user.repository.FavoriteRepository;
import com.sunuhelp.user.service.FavoriteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, FavoriteMapper favoriteMapper) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    @Transactional
    public FavoriteResponse create(UUID accountId, CreateFavoriteRequest request) {
        if (favoriteRepository.existsByAccountIdAndEntityId(accountId, request.getEntityId())) {
            throw new FavoriteAlreadyExistsException();
        }

        Favorite favorite = Favorite.create(accountId, request.getEntityId());
        favoriteRepository.save(favorite);

        return favoriteMapper.toResponse(favorite);
    }

    @Override
    public Page<FavoriteResponse> findByAccount(UUID accountId, Pageable pageable) {
        return favoriteRepository.findByAccountId(accountId, pageable).map(favoriteMapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(UUID accountId, UUID entityId) {
        Favorite favorite = favoriteRepository.findByAccountIdAndEntityId(accountId, entityId)
                .orElseThrow(FavoriteNotFoundException::new);
        // Suppression physique volontaire, pas softDelete() - aucune valeur d'audit pour un favori retire.
        favoriteRepository.delete(favorite);
    }
}
