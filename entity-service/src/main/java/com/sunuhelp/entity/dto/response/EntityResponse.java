package com.sunuhelp.entity.dto.response;

import com.sunuhelp.entity.enums.PersonType;
import com.sunuhelp.entity.enums.TrustLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class EntityResponse {
    private UUID id;
    private UUID ownerAccountId;
    private PersonType personType;
    private String logoUrl;
    private TrustLevel trustLevel;
    private long viewCount;
    private long searchAppearanceCount;
    private UUID categoryId;
    /** Deja resolus dans la langue demandee - jamais la table de traduction brute. */
    private String name;
    private String description;
}
