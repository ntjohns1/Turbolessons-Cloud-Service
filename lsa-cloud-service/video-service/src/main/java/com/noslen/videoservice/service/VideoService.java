package com.noslen.videoservice.service;
import com.google.cloud.storage.*;
import java.io.InputStream;
import java.nio.channels.Channels;

public class VideoService {
    public InputStream getVideoInputStreamFromGoogleCloud(String videoId) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        String bucketName = "noslen-bucket";
        Blob blob = storage.get(BlobId.of(bucketName, videoId));
        return Channels.newInputStream(blob.reader());
    }
}
