package com.sunuhelp.entity.repository;

import com.sunuhelp.entity.entity.ServicePoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServicePointRepository extends JpaRepository<ServicePoint, UUID> {
    List<ServicePoint> findByEntityIdAndActiveTrue(UUID entityId);
}
