package com.noslen.videoservice.config;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.noslen.videoservice.service.VideoStorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class StorageConfig {

    Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(System.getenv("credentialsPath")));
    Storage storage = StorageOptions
            .newBuilder()
            .setCredentials(credentials)
            .setProjectId(System.getenv("projectId"))
            .build()
            .getService();
    String bucketName = System.getenv("bucketName");

    public StorageConfig() throws IOException {
    }

    @Bean
    public Storage storage() {
        return StorageOptions.getDefaultInstance().getService();
    }

    @Bean
    public VideoStorageClient videoStorageClient() throws IOException {
        return new VideoStorageClient(storage, bucketName);
    }
}

