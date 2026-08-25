package com.sunuhelp.auth.controller;

import com.sunuhelp.auth.dto.request.LoginRequest;
import com.sunuhelp.auth.dto.request.RefreshTokenRequest;
import com.sunuhelp.auth.dto.request.RegisterRequest;
import com.sunuhelp.auth.dto.request.VerifyOtpRequest;
import com.sunuhelp.auth.dto.response.AccountResponse;
import com.sunuhelp.auth.dto.response.TokenResponse;
import com.sunuhelp.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Inscription, verification OTP, connexion, rafraichissement de token. */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentification")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Creer un compte (Usager ou futur proprietaire d'Entite)")
    public ResponseEntity<AccountResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Valider le code recu par SMS")
    public ResponseEntity<Void> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        authService.verifyOtp(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Se connecter et obtenir un couple access/refresh token")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request,
                                                HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");
        return ResponseEntity.ok(authService.login(request, ipAddress, userAgent));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Obtenir un nouveau access token sans redemander le mot de passe")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "Revoquer un refresh token precis (un seul appareil)")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}
