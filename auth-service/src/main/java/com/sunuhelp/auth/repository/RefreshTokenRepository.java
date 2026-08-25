package com.sunuhelp.auth.repository;

import com.sunuhelp.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    /** Retrouve un token a partir de son hash, pour le renouvellement de JWT. */
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    /** Liste des sessions actives d'un compte - utile pour "deconnecter tous mes appareils". */
    List<RefreshToken> findAllByAccountIdAndRevokedFalse(UUID accountId);
}
