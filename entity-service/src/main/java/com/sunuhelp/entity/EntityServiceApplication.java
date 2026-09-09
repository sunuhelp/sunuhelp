package com.sunuhelp.entity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.sunuhelp.entity", "com.sunuhelp.common"})
@EnableJpaAuditing
public class EntityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntityServiceApplication.class, args);
    }
}
