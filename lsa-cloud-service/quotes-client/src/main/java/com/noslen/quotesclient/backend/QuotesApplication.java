/**
 * 
 */
package com.noslen.quotesclient.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;

import com.noslen.quotesclient.shared.KeycloakReactiveTokenInstrospector;

/**
 * @author Philippe
 *
 */
@SpringBootApplication
@EnableWebFluxSecurity
@EnableDiscoveryClient
public class QuotesApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(QuotesApplication.class);
    }
    
    
    @Bean
    public ReactiveOpaqueTokenIntrospector keycloakIntrospector(OAuth2ResourceServerProperties props) {
        
        NimbusReactiveOpaqueTokenIntrospector delegate = new NimbusReactiveOpaqueTokenIntrospector(
           props.getOpaquetoken().getIntrospectionUri(),
           props.getOpaquetoken().getClientId(),
           props.getOpaquetoken().getClientSecret());
        
        return new KeycloakReactiveTokenInstrospector(delegate);
    }
    
    
}

