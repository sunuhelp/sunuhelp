package com.sunuhelp.entity.mapper;

import com.sunuhelp.entity.dto.response.OfferResponse;
import com.sunuhelp.entity.entity.Offer;
import com.sunuhelp.entity.entity.OfferTranslation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    @Mapping(target = "id", source = "offer.id")
    @Mapping(target = "title", source = "translation.title")
    @Mapping(target = "description", source = "translation.description")
    OfferResponse toResponse(Offer offer, OfferTranslation translation);
}
