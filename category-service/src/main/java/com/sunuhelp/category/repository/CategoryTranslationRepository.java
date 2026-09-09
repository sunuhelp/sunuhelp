package com.sunuhelp.category.repository;

import com.sunuhelp.category.entity.CategoryTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryTranslationRepository extends JpaRepository<CategoryTranslation, UUID> {

    List<CategoryTranslation> findByCategoryId(UUID categoryId);

    Optional<CategoryTranslation> findByCategoryIdAndLocale(UUID categoryId, String locale);

    void deleteByCategoryId(UUID categoryId);
}
