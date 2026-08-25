package com.sunuhelp.auth.dto.request;

import com.sunuhelp.auth.enums.OtpType;
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
public class VerifyOtpRequest {

    @NotBlank(message = "{validation.phone.required}")
    private String phoneNumber;

    @NotNull
    private OtpType otpType;

    @NotBlank
    private String code;
}
