package com.sunuhelp.entity.mapper;

import com.sunuhelp.entity.dto.response.EntityReportResponse;
import com.sunuhelp.entity.entity.EntityReport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityReportMapper {
    EntityReportResponse toResponse(EntityReport report);
}
