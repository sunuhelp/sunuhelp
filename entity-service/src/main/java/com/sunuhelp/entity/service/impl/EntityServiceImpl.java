package com.sunuhelp.entity.service.impl;

import com.sunuhelp.entity.dto.request.CreateEntityRequest;
import com.sunuhelp.entity.dto.request.EntityTranslationRequest;
import com.sunuhelp.entity.dto.request.UpdateEntityRequest;
import com.sunuhelp.entity.dto.response.EntityResponse;
import com.sunuhelp.entity.entity.BusinessEntity;
import com.sunuhelp.entity.entity.EntityCategory;
import com.sunuhelp.entity.entity.EntityTranslation;
import com.sunuhelp.entity.event.EntityCreatedEvent;
import com.sunuhelp.entity.event.EntityUpdatedEvent;
import com.sunuhelp.entity.event.EventProducer;
import com.sunuhelp.entity.exception.EntityNotFoundException;
import com.sunuhelp.entity.exception.NotOwnerException;
import com.sunuhelp.entity.mapper.EntityMapper;
import com.sunuhelp.entity.mapper.EntityTranslationResolver;
import com.sunuhelp.entity.repository.BusinessEntityRepository;
import com.sunuhelp.entity.repository.EntityCategoryRepository;
import com.sunuhelp.entity.repository.EntityTranslationRepository;
import com.sunuhelp.entity.service.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EntityServiceImpl implements EntityService {

    private final BusinessEntityRepository entityRepository;
    private final EntityTranslationRepository translationRepository;
    private final EntityCategoryRepository entityCategoryRepository;
    private final EntityTranslationResolver translationResolver;
    private final EntityMapper entityMapper;
    private final EventProducer eventProducer;

    public EntityServiceImpl(BusinessEntityRepository entityRepository,
                              EntityTranslationRepository translationRepository,
                              EntityCategoryRepository entityCategoryRepository,
                              EntityTranslationResolver translationResolver,
                              EntityMapper entityMapper,
                              EventProducer eventProducer) {
        this.entityRepository = entityRepository;
        this.translationRepository = translationRepository;
        this.entityCategoryRepository = entityCategoryRepository;
        this.translationResolver = translationResolver;
        this.entityMapper = entityMapper;
        this.eventProducer = eventProducer;
    }

    @Override
    @Transactional
    public EntityResponse create(CreateEntityRequest request, UUID ownerAccountId, String locale) {
        // TODO : verifier ici que le compte a un email renseigne (auth-service ne
        // possede pas encore d'endpoint expose pour lire un compte depuis un autre
        // service - a ajouter avant la mise en production).
        BusinessEntity entity = BusinessEntity.create(ownerAccountId, request.getPersonType(), null);
        entityRepository.save(entity);

        for (EntityTranslationRequest t : request.getTranslations()) {
            translationRepository.save(EntityTranslation.of(entity.getId(), t.getLocale(), t.getName(), t.getDescription()));
        }

        entityCategoryRepository.save(EntityCategory.createPrimary(entity.getId(), request.getCategoryId()));

        eventProducer.publish(EntityCreatedEvent.of(entity.getId()));

        return toResponse(entity, request.getCategoryId(), locale);
    }

    @Override
    @Transactional
    public EntityResponse update(UUID entityId, UpdateEntityRequest request, UUID requesterAccountId, String locale) {
        BusinessEntity entity = entityRepository.findById(entityId).orElseThrow(EntityNotFoundException::new);

        if (!entity.getOwnerAccountId().equals(requesterAccountId)) {
            throw new NotOwnerException();
        }

        // Un champ critique (nom) change : retire le badge Verifiee s'il etait accorde.
        entity.revertToUnverified();
        if (request.getLogoUrl() != null) {
            entity.updateLogo(request.getLogoUrl());
        }
        entityRepository.save(entity);

        translationRepository.deleteAll(translationRepository.findByEntityId(entityId));
        for (EntityTranslationRequest t : request.getTranslations()) {
            translationRepository.save(EntityTranslation.of(entityId, t.getLocale(), t.getName(), t.getDescription()));
        }

        eventProducer.publish(EntityUpdatedEvent.of(entityId));

        UUID categoryId = primaryCategoryId(entityId);
        return toResponse(entity, categoryId, locale);
    }

    @Override
    public EntityResponse findById(UUID entityId, String locale) {
        BusinessEntity entity = entityRepository.findById(entityId).orElseThrow(EntityNotFoundException::new);
        UUID categoryId = primaryCategoryId(entityId);
        return toResponse(entity, categoryId, locale);
    }

    @Override
    @Transactional
    public void recordView(UUID entityId) {
        BusinessEntity entity = entityRepository.findById(entityId).orElseThrow(EntityNotFoundException::new);
        entity.incrementViewCount();
        entityRepository.save(entity);
    }

    private UUID primaryCategoryId(UUID entityId) {
        return entityCategoryRepository.findByEntityId(entityId).stream()
                .filter(EntityCategory::isPrimary)
                .map(EntityCategory::getCategoryId)
                .findFirst()
                .orElse(null);
    }

    private EntityResponse toResponse(BusinessEntity entity, UUID categoryId, String locale) {
        EntityTranslation translation = translationResolver.resolve(entity.getId(), locale);
        return entityMapper.toResponse(entity, translation, categoryId);
    }
}
