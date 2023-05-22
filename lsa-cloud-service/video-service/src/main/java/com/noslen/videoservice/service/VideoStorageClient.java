package com.noslen.videoservice.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class VideoStorageClient {

    Storage storage = StorageOptions.getDefaultInstance().getService();
    Page<Bucket> buckets = storage.list();

    Bucket bucket;
    String bucketName = "noslen-test-bucket";
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

    public BlobId saveVideo(String blobName, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        bucket = getBucket(bucketName);
        Blob blob = bucket.create(blobName, bytes, "video/mp4");

        return blob.getBlobId();
    }
}