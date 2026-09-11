package com.sunuhelp.search.client;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/** DTO de reception minimal - seuls les champs necessaires a l'indexation. */
@Getter
@Setter
public class CategoryDetailsResponse {
    private UUID id;
    private String slug;
    private UUID parentId;
}
