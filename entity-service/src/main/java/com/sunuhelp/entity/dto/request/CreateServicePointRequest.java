package com.sunuhelp.entity.dto.request;

import com.sunuhelp.entity.enums.ServicePointType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * address et coverageZone sont tous deux facultatifs au niveau validation
 * de base - la coherence avec "type" (l'un des deux est obligatoire selon
 * le cas) est verifiee cote service, pas via une annotation seule.
 */
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
}
