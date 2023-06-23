package com.noslen.videoservice.model;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;

public class FluxMultipartFile implements MultipartFile {

    private final Flux<DataBuffer> content;
    private final String name;
    private final String originalFilename;
    private final long size;
    private final String contentType;

    public FluxMultipartFile(Flux<DataBuffer> content, String name, String originalFilename, long size, String contentType) {
        Assert.notNull(content, "Content Flux must not be null");
        Assert.notNull(name, "Name must not be null");
        this.content = content;
        this.name = name;
        this.originalFilename = originalFilename;
        this.size = size;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public byte[] getBytes() throws IOException {
        throw new UnsupportedOperationException("Getting byte[] is not supported, use the Flux<DataBuffer> content");
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException("Getting InputStream is not supported, use the Flux<DataBuffer> content");
    }

    public Flux<DataBuffer> getContent() {
        return this.content;
    }

    // The methods below are not supported and only included to complete the MultipartFile interface
    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("Transfer to Path is not supported.");
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("Transfer to File is not supported.");
    }
}

