package com.sunuhelp.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Duree de vie des tokens - concerne uniquement l'EMISSION, donc reste
 * propre a auth-service (seul emetteur). Le secret de signature, lui,
 * vit dans common-lib (JwtProperties) car partage par tous les services
 * qui doivent VALIDER un token.
 */
@Component
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtIssuanceProperties {
    private long accessTokenExpirationMinutes;
    private long refreshTokenExpirationDays;
}
