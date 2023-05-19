package com.noslen.videoservice.controller;

import com.noslen.videoservice.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;

public class VideoHandler {

    @Autowired
    DefaultDataBufferFactory bufferFactory;

    VideoService service;

    Mono<ServerResponse> getVideo(ServerRequest r) {
        String videoId = r.pathVariable("videoId");

        return Mono.fromCallable(() -> service.getVideoInputStreamFromGoogleCloud(videoId))
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
                .onErrorResume(e -> {
                    // handle error here, for example log and return a status 500
                    return ServerResponse.status(500).build();
                });
    }
}

