package com.noslen.videoservice.model;

import org.springframework.web.multipart.MultipartFile;

public class VideoUploadRequest {

    private String blobName;
    private MultipartFile fileData;

    public String getBlobName() {
        return blobName;
    }

    public void setBlobName(String blobName) {
        this.blobName = blobName;
    }

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }
}
