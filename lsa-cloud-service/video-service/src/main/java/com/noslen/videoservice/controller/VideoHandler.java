package com.noslen.videoservice.controller;

import com.noslen.videoservice.model.FluxMultipartFile;
import com.noslen.videoservice.model.VideoUploadRequest;
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
        return r.multipartData()
                .flatMap(parts -> {
                    Map<String, Part> partMap = parts.toSingleValueMap();
                    FilePart filePart = (FilePart) partMap.get("file");  // get the file
                    log.debug("All parts: {}", partMap.keySet()); // <-- add this
                    Part blobNamePart = partMap.get("blobName");  // get the blobName
                    log.debug("Blob name part: {}", blobNamePart); // <-- and this

                    Mono<String> blobNameMono = DataBufferUtils.join(blobNamePart.content())
                            .map(dataBuffer -> {
                                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(bytes);
                                DataBufferUtils.release(dataBuffer);
                                return new String(bytes, StandardCharsets.UTF_8);
                            });

                    log.debug("Received file part: {}", filePart); // <-- add this
                    log.debug("Received blob name: {}", blobNameMono.block()); // <-- and this

                    return Mono.zip(filePart.content().collectList(), blobNameMono);
                })
                .flatMap(tuple -> {
                    List<DataBuffer> buffers = tuple.getT1();
                    String blobName = tuple.getT2();
                    // Concatenate all buffers into a single Flux<DataBuffer>
                    DataBufferFactory factory = new DefaultDataBufferFactory();
                    Flux<DataBuffer> fileDataBuffer = Flux.just(factory.join(buffers));

                    // Convert Flux<DataBuffer> to MultipartFile
                    MultipartFile multipartFile = new FluxMultipartFile(fileDataBuffer, "file", blobName, -1, "video/mp4");

                    return Mono.fromCallable(() -> service.saveVideo(blobName, multipartFile));
                })
                .flatMap(blobId -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(BodyInserters.fromValue(blobId.toString())))
                .onErrorResume(e -> ServerResponse.status(500).build());
    }

}



