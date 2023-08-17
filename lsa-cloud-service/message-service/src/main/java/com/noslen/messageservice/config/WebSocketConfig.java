package com.noslen.messageservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noslen.messageservice.service.MsgCreatedEvent;
import com.noslen.messageservice.service.MsgCreatedEventPublisher;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.HttpCookie;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Log4j2
@Configuration
public
class WebSocketConfig {

    @Bean
    Executor executor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    HandlerMapping handlerMapping(WebSocketHandler wsh) {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(10);
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/messages", wsh);
        map.put("/ws/messages/{userId}", wsh);
        mapping.setUrlMap(map);
        mapping.setCorsConfigurations(Collections.singletonMap("*", new CorsConfiguration().applyPermitDefaultValues()));
        return mapping;
    }

    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    WebSocketHandler handle(ObjectMapper objectMapper, MsgCreatedEventPublisher eventPublisher) {
        Supplier<Flux<MsgCreatedEvent>> supplier = () -> Flux.create(eventPublisher).share();
        Flux<MsgCreatedEvent> publish = Flux.defer(supplier).cache(1);
        return session -> {
            String userId = parseUserId(session.getHandshakeInfo().getUri().toString());
            List<String> cookies = session.getHandshakeInfo().getHeaders().get("Cookie");
            String sessionId = null;
            assert cookies != null;
            for (String cookie : cookies) {
                if (cookie.startsWith("sessionId=")) {
                    sessionId = cookie.split("=")[1];
                    break;
                }
            }
            log.info("SessionId: " + sessionId);
            log.info("WebSocket session opened for user: " + userId);

            return session.receive()
                    .next()
                    .flatMap(message -> {
                        // Here we assume that the first message contains the token
                        String token = message.getPayloadAsText();
                        System.out.println("token: " + token);
                        try {


                            session.receive().doOnNext(msg -> {
                                log.info("Received message from user " + userId + ": " + msg.getPayloadAsText());
                            }).doOnComplete(() -> {
                                log.info("WebSocket session closed for user: " + userId);
                            }).subscribe();

                            Flux<WebSocketMessage> messageFlux = publish
                                    .filter(evt -> evt.getSource().getRecipient().equals(userId))
                                    .map(evt -> {
                                        try {
                                            return objectMapper.writeValueAsString(evt.getSource());
                                        } catch (JsonProcessingException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .map(str -> {
                                        log.info("sending " + str);
                                        return session.textMessage(str);
                                    });

                            return session.send(messageFlux);

                        } catch (JwtValidationException e) {
                            // Token validation failed, we close the session
                            return session.close(CloseStatus.POLICY_VIOLATION.withReason("Authentication failed"));
                        }
                    });
        };
    }


    private String parseUserId(String uri) {
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();
        return queryParams.getFirst("userId");
    }
}
