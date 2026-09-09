package com.sunuhelp.entity.repository;

import com.sunuhelp.entity.entity.EntityTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EntityTranslationRepository extends JpaRepository<EntityTranslation, UUID> {
    List<EntityTranslation> findByEntityId(UUID entityId);
}
