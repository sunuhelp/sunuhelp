package com.sunuhelp.entity.repository;

import com.sunuhelp.entity.entity.EntityCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EntityCategoryRepository extends JpaRepository<EntityCategory, UUID> {
    List<EntityCategory> findByEntityId(UUID entityId);
}
