package com.sunuhelp.entity.service;

import com.sunuhelp.entity.dto.request.CreateServicePointRequest;
import com.sunuhelp.entity.dto.request.SetOpeningHoursRequest;
import com.sunuhelp.entity.dto.request.SetTemporaryStatusRequest;
import com.sunuhelp.entity.dto.response.OpeningHoursResponse;
import com.sunuhelp.entity.dto.response.ServicePointResponse;

import java.util.List;
import java.util.UUID;

public interface ServicePointService {

    ServicePointResponse create(UUID entityId, CreateServicePointRequest request, UUID requesterAccountId);

    List<ServicePointResponse> findByEntity(UUID entityId);

    void setTemporaryStatus(UUID servicePointId, SetTemporaryStatusRequest request, UUID requesterAccountId);

    /** Remplace la semaine complete d'horaires en une seule operation. */
    List<OpeningHoursResponse> setOpeningHours(UUID servicePointId, SetOpeningHoursRequest request, UUID requesterAccountId);

    /** Lecture seule - necessaire pour que search-service puisse calculer "ouvert maintenant" a la recherche. */
    List<OpeningHoursResponse> findOpeningHours(UUID servicePointId);
}
