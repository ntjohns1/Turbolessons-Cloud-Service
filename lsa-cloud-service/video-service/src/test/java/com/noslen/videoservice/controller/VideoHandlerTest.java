package com.noslen.videoservice.controller;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.testing.RemoteStorageHelper;
import com.noslen.videoservice.model.SimpleBlobInfo;
import com.noslen.videoservice.service.VideoStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VideoHandlerTest {

    @Mock
    private VideoStorageClient videoService;

    @Mock
    private DefaultDataBufferFactory bufferFactory;

    private WebTestClient webTestClient;

    VideoHandler videoHandler;

    @Mock
    private ServerRequest serverRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bufferFactory = new DefaultDataBufferFactory();
        videoHandler = new VideoHandler(bufferFactory, videoService);
        VideoEndpointConfig config = new VideoEndpointConfig(videoHandler);
        RouterFunction<ServerResponse> routerFunction = config.routes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
        byte[] dummyData = "dummy data".getBytes();
        when(videoService.getVideo("Good_Boy.mp4")).thenReturn(new ByteArrayInputStream(dummyData));
    }


    @Test
    public void shouldGetVideoById() throws IOException {
//        String videoName = "video1";
//        when(serverRequest.pathVariable("videoName")).thenReturn(videoId);
//
//        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
//        when(videoService.getVideo(videoId)).thenReturn(inputStream);
//
//        DefaultDataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(new byte[0]);
//        when(bufferFactory.wrap(any(byte[].class))).thenReturn(dataBuffer);
//
//        Mono<ServerResponse> response = videoHandler.handleGetVideo(serverRequest);
//
//        StepVerifier.create(response)
//                .expectNextMatches(serverResponse -> Objects.equals(serverResponse.headers().getContentType(), MediaType.parseMediaType("video/mp4")))
//                .verifyComplete();

//        Path videoPath = Paths.get("/Users/noslen/Movies/Good_Boy.mp4");
//        String filename = videoPath.getFileName().toString();
//        InputStream inputStream = new FileInputStream(videoPath.toFile());
//        when(this.videoService.getVideo("Good_Boy.mp4")).thenReturn(inputStream);

        this.webTestClient.mutateWith(mockJwt()).get().uri("/api/video/Good_Boy.mp4").exchange().expectStatus().isOk().expectBody().consumeWith(result -> assertThat(result.getResponseBody()).isNotNull());

    }

    @Test
    public void shouldGetAllVideosInBucket() {
        SimpleBlobInfo blobInfo1 = new SimpleBlobInfo("video1", "id1");
        SimpleBlobInfo blobInfo2 = new SimpleBlobInfo("video2", "id2");

        when(videoService.getAllVideos()).thenReturn(Flux.just(blobInfo1, blobInfo2));

        Mono<ServerResponse> responseFlux = videoHandler.handleGetAllVideos(null);
        StepVerifier.create(responseFlux).expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful()).verifyComplete();

        webTestClient.mutateWith(mockJwt())
                .get()
                .uri("/api/video")
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(SimpleBlobInfo.class).hasSize(2)
                .contains(blobInfo1, blobInfo2);
    }

    @Test
    public void shouldSaveVideo() throws IOException {
        Path videoPath = Paths.get("/Users/noslen/Movies/Good_Boy.mp4");
        byte[] videoBytes = Files.readAllBytes(videoPath);
        String filename = videoPath.getFileName().toString();

        // Mock the videoStorageClient
        when(videoService.saveVideo(any())).thenReturn(Mono.empty());

        // Build the multipart request body
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new ByteArrayResource(videoBytes), MediaType.APPLICATION_OCTET_STREAM).filename(filename);

        MultiValueMap<String, HttpEntity<?>> body = builder.build();

        // Perform the POST request
        WebTestClient.ResponseSpec responseSpec = webTestClient.post().uri("/api/video").contentType(MediaType.MULTIPART_FORM_DATA).body(BodyInserters.fromMultipartData(body)).exchange();

        // Assert the response
        responseSpec.expectStatus().isOk();

        // Verify that the saveVideo method was called with a FilePart
        verify(videoService).saveVideo(any());
    }


}
