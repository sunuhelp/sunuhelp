package com.sunuhelp.discovery.config;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.Jersey3DiscoveryClientOptionalArgs;
import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import com.netflix.discovery.shared.transport.jersey3.Jersey3TransportClientFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Depuis la version 2.x du client Eureka (Netflix), aucune implementation
 * HTTP par defaut n'est plus fournie automatiquement pour le module Eureka
 * Server. On la declare nous-memes ici avec Jersey3 (deja present sur le
 * classpath via eureka-client-jersey3). Necessaire sur toute version actuelle
 * de Spring Cloud, ce n'est pas un contournement temporaire.
 */
@Configuration
public class EurekaClientTransportConfig {

    @Bean
    public AbstractDiscoveryClientOptionalArgs<?> discoveryClientOptionalArgs() {
        return new Jersey3DiscoveryClientOptionalArgs();
    }

    @Bean
    public TransportClientFactories<?> transportClientFactories() {
        return Jersey3TransportClientFactories.getInstance();
    }
}
