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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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
        // You don't need a blobName anymore because you get it from the file.
        Path videoPath = Paths.get("/Users/noslen/Movies/Good_Boy.mp4");
        byte[] videoBytes = Files.readAllBytes(videoPath);
        String filename = videoPath.getFileName().toString();

        // Mock the videoStorageClient
        when(videoService.saveVideo(any())).thenReturn(Mono.empty());

        // Build the multipart request body
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new ByteArrayResource(videoBytes), MediaType.APPLICATION_OCTET_STREAM)
                .filename(filename);  // Provide a filename, important for browsers and some APIs

        MultiValueMap<String, HttpEntity<?>> body = builder.build();

        // Perform the POST request
        WebTestClient.ResponseSpec responseSpec = webTestClient.post()
                .uri("/api/video")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchange();

        // Assert the response
        responseSpec.expectStatus().isOk();

        // Verify that the saveVideo method was called with a FilePart
        verify(videoService).saveVideo(any());
    }



}
