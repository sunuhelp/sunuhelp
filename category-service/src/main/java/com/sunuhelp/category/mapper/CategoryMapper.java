package com.sunuhelp.category.mapper;

import com.sunuhelp.category.dto.response.CategoryResponse;
import com.sunuhelp.category.entity.Category;
import com.sunuhelp.category.entity.CategoryTranslation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Combine une Category (donnees langue-neutres) et sa CategoryTranslation
 * deja resolue (nom/description dans la bonne langue) en un seul DTO -
 * le client ne voit jamais la separation entre les deux tables.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", source = "category.id")
    @Mapping(target = "name", source = "translation.name")
    @Mapping(target = "description", source = "translation.description")
    CategoryResponse toResponse(Category category, CategoryTranslation translation);
}
