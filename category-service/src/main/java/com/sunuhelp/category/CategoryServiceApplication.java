package com.sunuhelp.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * scanBasePackages inclut com.sunuhelp.common des le depart (lecon tiree
 * d'auth-service) : les composants partages (BaseEntity, JwtValidator,
 * JwtAuthenticationFilter...) vivent dans ce package, hors du scan par
 * defaut de @SpringBootApplication.
 */
@SpringBootApplication(scanBasePackages = {"com.sunuhelp.category", "com.sunuhelp.common"})
@EnableJpaAuditing
public class CategoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CategoryServiceApplication.class, args);
    }
}
