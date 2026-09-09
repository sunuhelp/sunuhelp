package com.sunuhelp.entity.mapper;

import com.sunuhelp.entity.dto.response.OpeningHoursResponse;
import com.sunuhelp.entity.entity.OpeningHours;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OpeningHoursMapper {
    OpeningHoursResponse toResponse(OpeningHours openingHours);
}
