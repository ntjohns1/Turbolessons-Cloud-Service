package com.noslen.videoservice.controller;

import com.google.cloud.storage.BlobId;
import com.noslen.videoservice.model.VideoUploadRequest;
import com.noslen.videoservice.service.VideoStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VideoHandlerTest {

    @Mock
    private VideoStorageClient videoService;

    @Mock
    private DefaultDataBufferFactory bufferFactory;

    private WebTestClient webTestClient;

    @Mock
    VideoHandler videoHandler;
    @Mock
    private ServerRequest serverRequest;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        videoHandler = new VideoHandler(bufferFactory, videoService);
        VideoEndpointConfig config = new VideoEndpointConfig(videoHandler);
        RouterFunction<ServerResponse> routerFunction = config.routes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    public void shouldGetVideoById() {
        String videoId = "video1";
        when(serverRequest.pathVariable("videoId")).thenReturn(videoId);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        when(videoService.getVideo(videoId)).thenReturn(inputStream);

        DefaultDataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(new byte[0]);
        when(bufferFactory.wrap(any(byte[].class))).thenReturn(dataBuffer);

        Mono<ServerResponse> response = videoHandler.handleGetVideo(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> Objects.equals(serverResponse.headers().getContentType(), MediaType.parseMediaType("video/mp4")))
                .verifyComplete();

    }

    @Test
    public void shouldSaveVideo() throws IOException {

        VideoUploadRequest req = new VideoUploadRequest();
        req.setBlobName("testBlob");
        req.setFilePath("./Users/noslen/test.mp4");
        BlobId blobId = BlobId.of("bucketName", "testBlob");
        when(videoService.saveVideo(req.getBlobName(), req.getFilePath())).thenReturn(blobId);

        WebTestClient.ResponseSpec responseSpec = webTestClient.post()
                .uri("/api/video")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange();

        responseSpec.expectStatus().isOk()
                .expectBody(String.class).isEqualTo(blobId.toString());

        // Print the whole response in case of unexpected status
        HttpStatus status = responseSpec.returnResult(Object.class).getStatus();
        if (status != HttpStatus.OK) {
            log.error("Unexpected status: " + status);
            log.error("Response: " + responseSpec.returnResult(Object.class).getResponseBody());
        }
    }

}
