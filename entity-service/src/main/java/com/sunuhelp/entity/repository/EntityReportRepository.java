package com.sunuhelp.entity.repository;

import com.sunuhelp.entity.entity.EntityReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EntityReportRepository extends JpaRepository<EntityReport, UUID> {
    List<EntityReport> findByEntityId(UUID entityId);
}
