package com.sunuhelp.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Exclusions JPA/DataSource necessaires : common-lib depend de
 * spring-boot-starter-data-jpa (pour BaseEntity, utilise par les 6 autres
 * services), mais search-service ne s'en sert jamais lui-meme - sans ces
 * exclusions, Spring Boot tente quand meme de configurer un DataSource
 * inexistant (aucune base relationnelle ici, uniquement Elasticsearch).
 */
@SpringBootApplication(
        scanBasePackages = {"com.sunuhelp.search", "com.sunuhelp.common"},
        exclude = {
                DataSourceAutoConfiguration.class,
                SqlInitializationAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
@EnableFeignClients
public class SearchServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchServiceApplication.class, args);
    }
}
