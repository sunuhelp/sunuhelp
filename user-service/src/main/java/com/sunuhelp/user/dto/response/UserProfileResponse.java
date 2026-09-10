package com.sunuhelp.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserProfileResponse {
    private UUID id;
    private UUID accountId;
    private String firstName;
    private String lastName;
    private String city;
    private String avatarUrl;
    private String preferredLocale;
    private boolean historyTrackingEnabled;
}
