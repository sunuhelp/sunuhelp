package com.sunuhelp.user.service;

import com.sunuhelp.user.dto.request.UpdateProfileRequest;
import com.sunuhelp.user.dto.response.UserProfileResponse;

import java.util.UUID;

public interface UserProfileService {

    /** Cree le profil s'il n'existe pas encore, ou le retourne s'il existe deja - appelee au premier acces. */
    UserProfileResponse getOrCreate(UUID accountId);

    UserProfileResponse update(UUID accountId, UpdateProfileRequest request);

    void enableHistoryTracking(UUID accountId);

    void disableHistoryTracking(UUID accountId);
}
