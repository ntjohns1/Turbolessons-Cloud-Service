package com.turbolessons.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import io.restassured.config.RestAssuredConfig;
import org.yaml.snakeyaml.Yaml;

public class AppConfig {
    private static final Map<String, Object> config;

    static {
        Yaml yaml = new Yaml();
        String configFileName = "config.yml";
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(configFileName)) {
            if (input != null) {
                config = yaml.load(input);
            } else {
                throw new RuntimeException("Could not load " + configFileName + " file.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading " + configFileName + " file.", e);
        }
    }



    public static String getBaseUrl() {
        String env = System.getProperty("env", "dev");
        // Expecting a YAML structure like:
        // base:
        //   url:
        //     dev: "http://dev.example.com"
        //     prod: "http://prod.example.com"
        @SuppressWarnings("unchecked")
        Map<String, Object> baseConfig = (Map<String, Object>) config.get("base");
        @SuppressWarnings("unchecked")
        Map<String, Object> urlConfig = (Map<String, Object>) baseConfig.get("url");
        return (String) urlConfig.get(env);
    }

    private static Map<String, Object> auth() {
        @SuppressWarnings("unchecked")
        Map<String, Object> oktaConfig =  (Map<String, Object>) config.get("okta");
        return oktaConfig;
    }
    public static String tokenUri() {
        return auth().get("org_url").toString();
    }

    public static String clientId() {
        return auth().get("client_id").toString();
    }

    public static String clientSecret() {
        return auth().get("client_secret").toString();
    }
}
