package com.sunuhelp.common.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Secret partage entre TOUS les microservices - auth-service signe avec
 * cette meme cle, chaque autre service ne fait que verifier. Doit provenir
 * de la variable d'environnement JWT_SECRET, identique partout.
 */
@Component
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtProperties {
    private String secret;
}
