package com.sunuhelp.entity.mapper;

import com.sunuhelp.entity.dto.response.ServicePointResponse;
import com.sunuhelp.entity.entity.ServicePoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** currentlyOpen est calcule par OpeningStatusResolver, jamais stocke - passe explicitement ici. */
@Mapper(componentModel = "spring")
public interface ServicePointMapper {

    @Mapping(target = "isOpen247", source = "servicePoint.open247")
    @Mapping(target = "currentlyOpen", source = "currentlyOpen")
    ServicePointResponse toResponse(ServicePoint servicePoint, boolean currentlyOpen);
}
