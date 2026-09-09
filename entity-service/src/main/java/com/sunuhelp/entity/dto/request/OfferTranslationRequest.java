package com.sunuhelp.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferTranslationRequest {

    @NotBlank
    private String locale;

    @NotBlank
    private String title;

    private String description;
}
