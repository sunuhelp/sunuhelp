package com.sunuhelp.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Donnees d'inscription - le strict minimum, coherent avec la friction minimale voulue. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "{validation.phone.required}")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "{validation.phone.invalid-format}")
    private String phoneNumber;

    /** Facultatif a l'inscription - obligatoire seulement pour creer une entite plus tard. */
    private String email;

    @NotBlank(message = "{validation.password.required}")
    @jakarta.validation.constraints.Size(min = 8, message = "{validation.password.too-short}")
    private String password;
}
