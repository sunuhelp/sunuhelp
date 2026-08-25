package com.sunuhelp.auth.security.jwt;

import com.sunuhelp.auth.config.JwtProperties;
import com.sunuhelp.auth.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * Emission et validation des JWT (access token). Le refresh token n'est
 * volontairement PAS un JWT : c'est une valeur opaque aleatoire, dont seul
 * le hash est stocke en base (meme logique que l'OTP).
 */
@Component
public class JwtTokenProvider {

    private static final String CLAIM_ROLE = "role";

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /** Genere le token d'acces, court duree, transporte l'id du compte et son role. */
    public String generateAccessToken(UUID accountId, Role role) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.getAccessTokenExpirationMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(accountId.toString())
                .claim(CLAIM_ROLE, role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(signingKey)
                .compact();
    }

    /** Valeur aleatoire opaque, jamais un JWT - le contenu ne stocke aucune info metier. */
    public String generateOpaqueRefreshToken() {
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /** Retourne l'id du compte porte par le token, si valide. */
    public UUID getAccountId(String token) {
        return UUID.fromString(parseClaims(token).getSubject());
    }

    public Role getRole(String token) {
        return Role.valueOf(parseClaims(token).get(CLAIM_ROLE, String.class));
    }

    /** Verifie signature et expiration - toute exception jjwt = token invalide. */
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
