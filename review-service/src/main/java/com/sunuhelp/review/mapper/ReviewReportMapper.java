package com.sunuhelp.review.mapper;

import com.sunuhelp.review.dto.response.ReviewReportResponse;
import com.sunuhelp.review.entity.ReviewReport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewReportMapper {
    ReviewReportResponse toResponse(ReviewReport report);
}
