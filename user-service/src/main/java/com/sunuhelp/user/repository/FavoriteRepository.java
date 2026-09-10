package com.sunuhelp.user.repository;

import com.sunuhelp.user.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    Page<Favorite> findByAccountId(UUID accountId, Pageable pageable);

    Optional<Favorite> findByAccountIdAndEntityId(UUID accountId, UUID entityId);

    boolean existsByAccountIdAndEntityId(UUID accountId, UUID entityId);
}
