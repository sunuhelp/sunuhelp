package com.sunuhelp.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * scanBasePackages inclut com.sunuhelp.common car les composants partages
 * (BaseEntity, JwtAuthenticationFilter, JwtValidator...) vivent dans ce
 * package, en dehors du scan par defaut de @SpringBootApplication qui ne
 * couvre que com.sunuhelp.auth par defaut.
 */
@SpringBootApplication(scanBasePackages = {"com.sunuhelp.auth", "com.sunuhelp.common"})
@EnableJpaAuditing
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
