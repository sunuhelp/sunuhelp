package com.sunuhelp.entity.dto.request;

import com.sunuhelp.entity.enums.PersonType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Creation minimale d'une fiche - logoUrl est volontairement absent ici,
 * ajoute plus tard via media-service. Le controle "email requis" se fait
 * cote service, pas via une annotation, car il depend d'un appel a
 * auth-service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEntityRequest {

    @NotNull
    private PersonType personType;

    @NotNull(message = "{validation.category.required}")
    private UUID categoryId;

    @NotEmpty
    @Valid
    private List<EntityTranslationRequest> translations;
}
