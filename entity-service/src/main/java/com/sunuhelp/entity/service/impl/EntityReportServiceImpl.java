package com.sunuhelp.entity.service.impl;

import com.sunuhelp.entity.dto.request.CreateReportRequest;
import com.sunuhelp.entity.dto.response.EntityReportResponse;
import com.sunuhelp.entity.entity.EntityReport;
import com.sunuhelp.entity.exception.EntityNotFoundException;
import com.sunuhelp.entity.mapper.EntityReportMapper;
import com.sunuhelp.entity.repository.BusinessEntityRepository;
import com.sunuhelp.entity.repository.EntityReportRepository;
import com.sunuhelp.entity.service.EntityReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EntityReportServiceImpl implements EntityReportService {

    private final EntityReportRepository reportRepository;
    private final BusinessEntityRepository entityRepository;
    private final EntityReportMapper reportMapper;

    public EntityReportServiceImpl(EntityReportRepository reportRepository,
                                    BusinessEntityRepository entityRepository,
                                    EntityReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.entityRepository = entityRepository;
        this.reportMapper = reportMapper;
    }

    @Override
    @Transactional
    public EntityReportResponse create(UUID entityId, CreateReportRequest request, UUID reporterAccountId) {
        entityRepository.findById(entityId).orElseThrow(EntityNotFoundException::new);

        EntityReport report = EntityReport.create(entityId, reporterAccountId, request.getReason(), request.getComment());
        reportRepository.save(report);

        return reportMapper.toResponse(report);
    }
}
