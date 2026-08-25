package com.sunuhelp.auth.security.jwt;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Hachage DETERMINISTE (SHA-256, sans sel) pour le refresh token - a la
 * difference de BCrypt (mot de passe, OTP), on doit pouvoir retrouver le
 * token par une recherche exacte en base sur son hash. BCrypt genere un
 * sel different a chaque appel, rendant toute recherche par hash impossible.
 */
@Component
public class TokenHasher {

    public String hash(String rawValue) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(rawValue.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponible", e);
        }
    }
}
