package com.sunuhelp.entity.service;

import com.sunuhelp.entity.dto.request.CreateEntityRequest;
import com.sunuhelp.entity.dto.request.UpdateEntityRequest;
import com.sunuhelp.entity.dto.response.EntityResponse;

import java.util.UUID;

public interface EntityService {

    EntityResponse create(CreateEntityRequest request, UUID ownerAccountId, String locale);

    EntityResponse update(UUID entityId, UpdateEntityRequest request, UUID requesterAccountId, String locale);

    EntityResponse findById(UUID entityId, String locale);

    /** Incremente le compteur de vues - appelee a chaque consultation. */
    void recordView(UUID entityId);
}
