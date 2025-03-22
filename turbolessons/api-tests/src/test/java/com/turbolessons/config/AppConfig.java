package com.turbolessons.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Properties properties = new Properties();

    static {
        String env = System.getProperty("env", "dev");
        String propFileName = "config.properties";

        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(propFileName)) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("Could not load config.properties file.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading config.properties file.", e);
        }
    }

    public static String getBaseUrl() {
        String env = System.getProperty("env", "dev");
        return properties.getProperty("base.url." + env);
    }

}
