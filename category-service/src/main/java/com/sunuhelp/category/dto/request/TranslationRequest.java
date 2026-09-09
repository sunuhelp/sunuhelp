package com.sunuhelp.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranslationRequest {

    @NotBlank(message = "{validation.locale.required}")
    private String locale;

    @NotBlank(message = "{validation.name.required}")
    private String name;

    private String description;
}
