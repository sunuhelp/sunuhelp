package com.sunuhelp.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

/** Reponse renvoyee apres une connexion ou un rafraichissement de token reussi. */
@Getter
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tokenType = "Bearer";
    /** Duree de validite du access token, en secondes. */
    private long expiresIn;
}
