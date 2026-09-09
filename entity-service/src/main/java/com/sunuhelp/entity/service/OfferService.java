package com.sunuhelp.entity.service;

import com.sunuhelp.entity.dto.request.CreateOfferRequest;
import com.sunuhelp.entity.dto.response.OfferResponse;

import java.util.List;
import java.util.UUID;

public interface OfferService {

    OfferResponse create(UUID servicePointId, CreateOfferRequest request, UUID requesterAccountId);

    List<OfferResponse> findByServicePoint(UUID servicePointId, String locale);
}
