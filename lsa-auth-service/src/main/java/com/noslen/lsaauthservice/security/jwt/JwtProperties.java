package com.noslen.lsaauthservice.security.jwt;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Getter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }
}
