package com.sunuhelp.user.mapper;

import com.sunuhelp.user.dto.response.UserProfileResponse;
import com.sunuhelp.user.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponse toResponse(UserProfile profile);
}
