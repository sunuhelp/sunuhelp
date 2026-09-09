package com.sunuhelp.entity.mapper;

import com.sunuhelp.entity.dto.response.EntityResponse;
import com.sunuhelp.entity.entity.BusinessEntity;
import com.sunuhelp.entity.entity.EntityTranslation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/** Combine BusinessEntity, sa traduction resolue, et l'id de categorie principale (recupere separement par le service). */
@Mapper(componentModel = "spring")
public interface EntityMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "translation.name")
    @Mapping(target = "description", source = "translation.description")
    @Mapping(target = "categoryId", source = "categoryId")
    EntityResponse toResponse(BusinessEntity entity, EntityTranslation translation, UUID categoryId);
}
