package com.sunuhelp.auth.service;

import com.sunuhelp.auth.dto.request.LoginRequest;
import com.sunuhelp.auth.dto.request.RefreshTokenRequest;
import com.sunuhelp.auth.dto.request.RegisterRequest;
import com.sunuhelp.auth.dto.request.VerifyOtpRequest;
import com.sunuhelp.auth.dto.response.AccountResponse;
import com.sunuhelp.auth.dto.response.TokenResponse;

public interface AuthService {

    AccountResponse register(RegisterRequest request);

    /** Active le compte si le code correspond a une inscription. */
    void verifyOtp(VerifyOtpRequest request);

    TokenResponse login(LoginRequest request, String ipAddress, String userAgent);

    TokenResponse refreshToken(RefreshTokenRequest request);

    /** Revoque un refresh token precis - ne deconnecte qu'un seul appareil. */
    void logout(String refreshToken);
}
