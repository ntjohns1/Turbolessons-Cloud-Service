package com.noslen.auth;

import com.netflix.appinfo.AmazonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import com.noslen.auth.config.KeycloakServerProperties;
import org.springframework.context.annotation.Profile;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
@EnableConfigurationProperties({KeycloakServerProperties.class})
public class AuthorizationServerApp {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationServerApp.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AuthorizationServerApp.class, args);
    }

    @Bean
    @Profile("!default")
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
        EurekaInstanceConfigBean b = new EurekaInstanceConfigBean(inetUtils);
        AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
        b.setDataCenterInfo(info);
        return b;
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> onApplicationReadyEventListener(ServerProperties serverProperties,
                                                                               KeycloakServerProperties keycloakServerProperties) {

        return (evt) -> {

            try {
                String hostname = "Unknown";
                InetAddress addr = serverProperties.getAddress();
                addr = InetAddress.getLocalHost();
                hostname = addr.getHostName();
                Integer port = serverProperties.getPort();
                String keycloakContextPath = keycloakServerProperties.getContextPath();
                LOG.info("Embedded Keycloak started: http://{}:{}{} to use keycloak", hostname, port, keycloakContextPath);
                LOG.info(hostname);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

        };
    }

}
