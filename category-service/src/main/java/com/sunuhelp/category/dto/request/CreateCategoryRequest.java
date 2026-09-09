package com.sunuhelp.category.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {

    @NotBlank(message = "{validation.slug.required}")
    private String slug;

    /** Null = categorie racine. */
    private UUID parentId;

    private String icon;

    private int displayOrder;

    private boolean requiresValidation;

    @NotEmpty
    @Valid
    private List<TranslationRequest> translations;
}
