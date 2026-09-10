package com.sunuhelp.review.mapper;

import com.sunuhelp.review.dto.response.ReviewResponseDto;
import com.sunuhelp.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** responseContent est passe separement car il vient d'une autre table (ReviewResponse), facultative. */
@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", source = "review.id")
    @Mapping(target = "createdAt", source = "review.createdAt")
    @Mapping(target = "responseContent", source = "responseContent")
    ReviewResponseDto toResponse(Review review, String responseContent);
}
