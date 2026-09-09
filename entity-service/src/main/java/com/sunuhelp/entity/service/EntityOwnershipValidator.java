package com.sunuhelp.entity.service;

import com.sunuhelp.entity.entity.BusinessEntity;
import com.sunuhelp.entity.exception.EntityNotFoundException;
import com.sunuhelp.entity.exception.NotOwnerException;
import com.sunuhelp.entity.repository.BusinessEntityRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Verification de propriete reutilisee par tous les services qui agissent
 * sur des ressources rattachees a une fiche (points de service, offres,
 * documents) - evite de dupliquer cette regle 4 fois.
 */
@Component
public class EntityOwnershipValidator {

    private final BusinessEntityRepository entityRepository;

    public EntityOwnershipValidator(BusinessEntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    /** Charge la fiche et verifie que requesterAccountId en est bien le proprietaire. */
    public BusinessEntity assertOwner(UUID entityId, UUID requesterAccountId) {
        BusinessEntity entity = entityRepository.findById(entityId)
                .orElseThrow(EntityNotFoundException::new);

        if (!entity.getOwnerAccountId().equals(requesterAccountId)) {
            throw new NotOwnerException();
        }
        return entity;
    }
}
