package com.sunuhelp.category.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * name/description sont deja RESOLUS dans la langue demandee (avec repli
 * automatique) - le client ne voit jamais la table de traduction brute.
 */
@Getter
@Builder
public class CategoryResponse {
    private UUID id;
    private String slug;
    private UUID parentId;
    private String icon;
    private int displayOrder;
    private boolean requiresValidation;
    private int depth;
    private String name;
    private String description;
}
