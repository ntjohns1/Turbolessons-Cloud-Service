//package com.noslen.messageservice.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//
//@Configuration
//public class JwtConfig {
//
//    @Value("${okta.oauth2.issuer}")
//    private String issuer;
//
//    @Bean
//    public JwtDecoder jwtDecoder() throws MalformedURLException {
//        URL jwkSetUri = new URL(new URL(issuer), "/v1/keys");
//        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri.toString()).build();
//    }
//}
