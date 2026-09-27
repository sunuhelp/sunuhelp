package com.sunuhelp.entity.service;

import com.sunuhelp.common.dto.PageResponse;
import com.sunuhelp.entity.dto.request.CreateEntityRequest;
import com.sunuhelp.entity.dto.request.UpdateEntityRequest;
import com.sunuhelp.entity.dto.response.EntityResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EntityService {

    EntityResponse create(CreateEntityRequest request, UUID ownerAccountId, String locale);

    EntityResponse update(UUID entityId, UpdateEntityRequest request, UUID requesterAccountId, String locale);

    EntityResponse findById(UUID entityId, String locale);

    /** Liste les fiches actives appartenant au compte connecte - utilise pour "Mes commerces". */
    PageResponse<EntityResponse> findByOwner(UUID ownerAccountId, String locale, Pageable pageable);

    /** Incremente le compteur de vues - appelee a chaque consultation. */
    void recordView(UUID entityId);
}
