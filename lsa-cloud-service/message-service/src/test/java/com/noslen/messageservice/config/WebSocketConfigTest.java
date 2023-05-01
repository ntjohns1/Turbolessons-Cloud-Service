package com.noslen.messageservice.config;

import com.noslen.messageservice.model.Msg;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WebSocketConfigTest {


    private final WebSocketClient socketClient = new ReactorNettyWebSocketClient();


    private final WebClient webClient = WebClient.builder().build();

    final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());


    private Msg generateRandomMsg() {
        return new Msg(UUID.randomUUID().toString(), "Jess",UUID.randomUUID().toString(),"Hello",time);
    }

    @Test
    public void testNotificationsOnUpdates() throws Exception {

        int count = 10;
        AtomicLong counter = new AtomicLong();
        URI uri = URI.create("ws://localhost:5005/ws/messages/testid");


        socketClient.execute(uri, (WebSocketSession session) -> {


            Mono<WebSocketMessage> out = Mono.just(session.textMessage("test"));


            Flux<String> in = session
                    .receive()
                    .map(WebSocketMessage::getPayloadAsText);


            return session
                    .send(out)
                    .thenMany(in)
                    .doOnNext(str -> counter.incrementAndGet())
                    .then();

        }).subscribe();


        Flux
                .<Msg>generate(sink -> sink.next(generateRandomMsg()))
                .take(count)
                .flatMap(this::write)
                .blockLast();

        Thread.sleep(1000);

        Assertions.assertThat(counter.get()).isEqualTo(count);
    }

    private Publisher<Msg> write(Msg p) {
        return
                this.webClient
                        .post()
                        .uri("http://localhost:5005/api/messages/testid")
                        .body(BodyInserters.fromValue(p))
                        .retrieve()
                        .bodyToMono(String.class)
                        .thenReturn(p);
    }
}
