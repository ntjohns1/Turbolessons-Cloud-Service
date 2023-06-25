package com.noslen.videoservice.controller;

import com.noslen.videoservice.model.FluxMultipartFile;
import com.noslen.videoservice.service.VideoStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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
        return Mono.fromCallable(() -> service.getVideo(videoId)).subscribeOn(Schedulers.boundedElastic()).flatMap(videoInputStream -> {
            Flux<DataBuffer> videoDataBuffer = DataBufferUtils.readInputStream(() -> videoInputStream, this.bufferFactory, 1024);
            return ServerResponse.ok().contentType(new MediaType("video", "mp4")).body(videoDataBuffer, DataBuffer.class);
        }).onErrorContinue((throwable, o) -> log.error("Error: ", throwable)).onErrorResume(e -> ServerResponse.status(500).build());
    }

    Mono<ServerResponse> handleGetAllVideos(ServerRequest r) {
        return service.getAllVideos()
                .collectList()
                .flatMap(blobInfo -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(blobInfo)))
                .onErrorResume(e -> ServerResponse.status(500).build());
    }


    public Mono<ServerResponse> handleSaveVideo(ServerRequest request) {
        return request.multipartData().flatMap(parts -> {
                    Map<String, Part> partMap = parts.toSingleValueMap();
                    FilePart filePart = (FilePart) partMap.get("file");
                    return service.saveVideo(filePart);
                })
                .then(ServerResponse.ok().build())
                .onErrorResume(e -> ServerResponse.status(500).body(BodyInserters.fromValue(e.getMessage())));
    }
}



