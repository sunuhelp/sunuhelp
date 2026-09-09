package com.sunuhelp.entity.service;

import com.sunuhelp.entity.dto.request.CreateReportRequest;
import com.sunuhelp.entity.dto.response.EntityReportResponse;

import java.util.UUID;

public interface EntityReportService {

    /** reporterAccountId nullable - un Visiteur non connecte peut signaler. */
    EntityReportResponse create(UUID entityId, CreateReportRequest request, UUID reporterAccountId);
}
