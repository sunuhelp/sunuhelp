package com.sunuhelp.search.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Pas de securisation JWT ici - toutes les routes sont publiques, pas besoin du bouton Authorize. */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI searchServiceOpenApi() {
        return new OpenAPI().info(new Info().title("SunuHelp - search-service").version("v1"));
    }
}
