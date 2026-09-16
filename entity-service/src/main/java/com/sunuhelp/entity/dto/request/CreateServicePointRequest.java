package com.sunuhelp.entity.dto.request;

import com.sunuhelp.entity.enums.ServicePointType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateServicePointRequest {

    @NotBlank(message = "{validation.name.required}")
    private String name;

    @NotNull
    private ServicePointType type;

    private String address;

    private String coverageZone;

    /** Facultatif - permet d'afficher un bouton "Appeler" cote frontend quand renseigne. */
    private String phoneNumber;
}
