package com.sunuhelp.search.client;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Reproduit uniquement les champs necessaires de EntityResponse
 * (entity-service) - un DTO de reception minimal, pas besoin de dupliquer
 * toute la classe source.
 */
@Getter
@Setter
public class EntityDetailsResponse {
    private UUID id;
    private UUID ownerAccountId;
    private String trustLevel;
    private long viewCount;
    private UUID categoryId;
    private String name;
    private String description;
}
