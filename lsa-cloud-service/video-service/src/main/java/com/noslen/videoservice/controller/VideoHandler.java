package com.noslen.videoservice.controller;

import com.noslen.videoservice.model.VideoUploadRequest;
import com.noslen.videoservice.service.VideoStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class VideoHandler {

        private final DefaultDataBufferFactory bufferFactory;
        private final VideoStorageClient service;

        public VideoHandler(DefaultDataBufferFactory bufferFactory, VideoStorageClient service) {
            this.bufferFactory = bufferFactory;
            this.service = service;
        }

    Mono<ServerResponse> handleGetVideo(ServerRequest r) {
        String videoId = r.pathVariable("videoId");
        return Mono.fromCallable(() -> service.getVideo(videoId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(videoInputStream -> {
            Flux<DataBuffer> videoDataBuffer = DataBufferUtils.readInputStream(
                    () -> videoInputStream,
                    this.bufferFactory,
                    1024);
            return ServerResponse.ok()
                    .contentType(new MediaType("video", "mp4"))
                    .body(videoDataBuffer, DataBuffer.class);
        })
                .onErrorContinue((throwable, o) -> log.error("Error: ", throwable))
                .onErrorResume(e -> ServerResponse.status(500).build());
    }

    Mono<ServerResponse> handleGetBucket(ServerRequest r) {
        String bucketName = r.pathVariable("bucketName");
        return Mono.fromCallable(() -> service.getBucket(bucketName))
                .flatMap(bucket -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(bucket)))
                .onErrorResume(e -> ServerResponse.status(500).build());
    }

    Mono<ServerResponse> handleSaveVideo(ServerRequest r) {
        return r.bodyToMono(VideoUploadRequest.class)
                .flatMap(req -> {
                    String blobName = req.getBlobName();
                    String filePath = req.getFilePath();
                    return Mono.fromCallable(() -> service.saveVideo(blobName, filePath));
                })
                .flatMap(blobId -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(BodyInserters.fromValue(blobId.toString())))
                .onErrorResume(e -> ServerResponse.status(500).build());
    }
}

