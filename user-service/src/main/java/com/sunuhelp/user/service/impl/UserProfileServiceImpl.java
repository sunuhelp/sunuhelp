package com.sunuhelp.user.service.impl;

import com.sunuhelp.user.dto.request.UpdateProfileRequest;
import com.sunuhelp.user.dto.response.UserProfileResponse;
import com.sunuhelp.user.entity.UserProfile;
import com.sunuhelp.user.mapper.UserProfileMapper;
import com.sunuhelp.user.repository.UserProfileRepository;
import com.sunuhelp.user.service.UserProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository profileRepository;
    private final UserProfileMapper profileMapper;

    public UserProfileServiceImpl(UserProfileRepository profileRepository, UserProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    @Override
    @Transactional
    public UserProfileResponse getOrCreate(UUID accountId) {
        UserProfile profile = profileRepository.findByAccountId(accountId)
                .orElseGet(() -> profileRepository.saveAndFlush(UserProfile.create(accountId)));
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public UserProfileResponse update(UUID accountId, UpdateProfileRequest request) {
        UserProfile profile = profileRepository.findByAccountId(accountId)
                .orElseGet(() -> UserProfile.create(accountId));

        profile.updateProfile(request.getFirstName(), request.getLastName(), request.getCity());
        profileRepository.save(profile);

        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public void enableHistoryTracking(UUID accountId) {
        UserProfile profile = profileRepository.findByAccountId(accountId)
                .orElseGet(() -> UserProfile.create(accountId));
        profile.enableHistoryTracking();
        profileRepository.save(profile);
    }

    @Override
    @Transactional
    public void disableHistoryTracking(UUID accountId) {
        UserProfile profile = profileRepository.findByAccountId(accountId)
                .orElseGet(() -> UserProfile.create(accountId));
        profile.disableHistoryTracking();
        profileRepository.save(profile);
    }
}
