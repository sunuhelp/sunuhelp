package com.sunuhelp.category.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Volontairement AUCUN champ "slug" ici - il doit rester stable apres
 * creation pour ne jamais desynchroniser search-service (deja decide en
 * modelisation).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {
    private String icon;
    private int displayOrder;
    private boolean requiresValidation;
}
