package com.sunuhelp.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Lie les valeurs "app.jwt.*" d'application.yaml a des champs types,
 * pour ne jamais coder en dur la duree de vie d'un token ou le secret
 * dans une classe Java - modifiable sans recompiler.
 */
@Component
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtProperties {
    private String secret;
    private long accessTokenExpirationMinutes;
    private long refreshTokenExpirationDays;
}
