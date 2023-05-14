package com.noslen.messageservice.config;

import com.noslen.messageservice.model.Msg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.net.URI;
import java.time.Duration;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class WebSocketConfigTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;


    @Test
    @WithMockUser
    void webSocketHandlerTest() throws InterruptedException {
        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

        // Prepare sample message
        Msg sampleMessage = new Msg("1", "sender", "recipient", "Hello, World!", "2023-05-01 10:30:00.000");

        // Set up WebSocket connection URI
        URI uri = URI.create("ws://localhost:" + port + "/ws/messages?userId=recipient");

        // Connect and send test WebSocket message
        Mono<Void> sessionMono = client.execute(uri, session -> {
            // Log when the session starts
            System.out.println("WebSocket session started");

            // Verify received WebSocket message
            Flux<String> in = session.receive().map(WebSocketMessage::getPayloadAsText);

            // Complete the session
            return session.send(Mono.empty())
                    .thenMany(in)
                    .doOnNext(payload -> {
                        System.out.println("Received payload: " + payload); // Log the received payload
                        assertThat(payload).contains(sampleMessage.getMsg());
                    })
                    .doOnComplete(() -> {
                        session.close().subscribe(); // Close the session explicitly on completion
                        System.out.println("WebSocket session completed"); // Log when the session completes
                    })
                    .then();
        });

        // Send the sample message to the API endpoint
        webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/messages/recipient")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleMessage)
                .exchange()
                .expectStatus().isCreated();

        StepVerifier.create(sessionMono).expectTimeout(Duration.ofSeconds(2)).verify(Duration.ofSeconds(2));
    }
}
