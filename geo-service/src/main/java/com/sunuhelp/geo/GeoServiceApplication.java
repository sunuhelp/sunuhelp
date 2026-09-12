package com.sunuhelp.geo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.sunuhelp.geo", "com.sunuhelp.common"})
@EnableJpaAuditing
public class GeoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeoServiceApplication.class, args);
    }
}
