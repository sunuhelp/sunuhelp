package com.sunuhelp.user.mapper;

import com.sunuhelp.user.dto.response.FavoriteResponse;
import com.sunuhelp.user.entity.Favorite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    FavoriteResponse toResponse(Favorite favorite);
}
