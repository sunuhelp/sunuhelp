package com.sunuhelp.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "{validation.phone.required}")
    private String phoneNumber;

    @NotBlank(message = "{validation.password.required}")
    private String password;
}
