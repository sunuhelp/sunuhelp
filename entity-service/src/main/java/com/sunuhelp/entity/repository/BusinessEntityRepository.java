package com.sunuhelp.entity.repository;

import com.sunuhelp.entity.entity.BusinessEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessEntityRepository extends JpaRepository<BusinessEntity, UUID> {

    /** Compte les fiches non verifiees d'un compte - alimente la limite de 3 fiches non verifiees max. */
    long countByOwnerAccountIdAndTrustLevel(UUID ownerAccountId, com.sunuhelp.entity.enums.TrustLevel trustLevel);

    Page<BusinessEntity> findByOwnerAccountIdAndActiveTrue(UUID ownerAccountId, Pageable pageable);
}
