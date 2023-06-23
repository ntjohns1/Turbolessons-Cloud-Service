package com.noslen.videoservice.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

@Slf4j
@Service
public class VideoStorageClient {

    Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(System.getenv("credentialsPath")));
    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(System.getenv("projectId")).build().getService();
    Bucket bucket;
    String bucketName = System.getenv("bucketName");

    public VideoStorageClient() throws IOException {
    }

    public InputStream getVideo(String videoId) {
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

    public Mono<Void> saveVideo(FilePart filePart) {
        String blobName = filePart.filename();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, blobName)).build();
        WritableByteChannel channel = storage.writer(blobInfo);
        Flux<DataBuffer> content = filePart.content();
        return content.collectList().flatMap(dataBufferList -> Mono.create(sink -> {
            try {
                for (DataBuffer dataBuffer : dataBufferList) {
                    ByteBuffer byteBuffer = dataBuffer.asByteBuffer();
                    while (byteBuffer.hasRemaining()) {
                        channel.write(byteBuffer);
                    }
                }
                channel.close();
                sink.success();
            } catch (IOException e) {
                sink.error(new RuntimeException("Failed to write to the Google Cloud Storage", e));
            }
        }));
    }

}
