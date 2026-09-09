package com.sunuhelp.entity.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** Modification du nom/description - declenche revertToUnverified() si la fiche etait deja verifiee. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEntityRequest {

    @NotEmpty
    @Valid
    private List<EntityTranslationRequest> translations;

    private String logoUrl;
}
