package com.sunuhelp.entity.repository;

import com.sunuhelp.entity.entity.OpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OpeningHoursRepository extends JpaRepository<OpeningHours, UUID> {
    List<OpeningHours> findByServicePointId(UUID servicePointId);
}
