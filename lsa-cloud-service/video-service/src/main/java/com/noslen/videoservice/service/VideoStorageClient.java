package com.noslen.videoservice.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;

@Service
public class VideoStorageClient {

    Credentials credentials = GoogleCredentials
            .fromStream(new FileInputStream("/Users/noslen/DevProjects/google-cloud/lesson-schedule-assistant-cfe4cf3aebad.json"));
    Storage storage = StorageOptions.newBuilder().setCredentials(credentials)
            .setProjectId("lesson-schedule-assistant").build().getService();
    Bucket bucket;
    String bucketName = "noslen-test-bucket";

    public VideoStorageClient() throws IOException {
    }

    public InputStream getVideo(String videoId) {
        bucketName = "noslen-test-bucket";
        Blob blob = storage.get(BlobId.of(bucketName, videoId));
        return Channels.newInputStream(blob.reader());
    }

    public Bucket getBucket(String bucketName) {
        bucket = storage.get(bucketName);
        if (bucket == null) {
            System.out.println("Creating new bucket.");
            bucket = storage.create(BucketInfo.of(bucketName));
        }
        return bucket;
    }

    public BlobId saveVideo(String blobName, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        bucket = getBucket(bucketName);
        Blob blob = bucket.create(blobName, bytes, "video/mp4");
        return blob.getBlobId();
    }
}