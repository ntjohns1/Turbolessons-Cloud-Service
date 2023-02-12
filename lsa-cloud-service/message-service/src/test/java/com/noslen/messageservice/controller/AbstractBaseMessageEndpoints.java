package com.noslen.messageservice.controller;

import com.noslen.messageservice.model.Msg;
import com.noslen.messageservice.repository.MsgRepo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Log4j2
@WebFluxTest
public abstract class AbstractBaseMessageEndpoints {

    private final WebTestClient client;

    @MockBean
    private MsgRepo repository;

    public AbstractBaseMessageEndpoints(WebTestClient client) {
        this.client = client;
    }

    final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());

    @Test
    public void getAll() {

        log.info("running  " + this.getClass().getName());


        Mockito
                .when(this.repository.findAll())
                .thenReturn(Flux.just(new Msg("1", "A","A","A",time), new Msg("2", "B","B","B",time)));


        this.client
                .get()
                .uri("/api/messages")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo("1")
                .jsonPath("$.[0].sender").isEqualTo("A")
                .jsonPath("$.[0].senderId").isEqualTo("A")
                .jsonPath("$.[0].msg").isEqualTo("A")
                .jsonPath("$.[0].timestamp").isEqualTo(time)
                .jsonPath("$.[1].id").isEqualTo("2")
                .jsonPath("$.[1].sender").isEqualTo("B")
                .jsonPath("$.[1].senderId").isEqualTo("B")
                .jsonPath("$.[1].msg").isEqualTo("B")
                .jsonPath("$.[1].timestamp").isEqualTo(time);
    }

    @Test
    public void save() {
        Msg data = new Msg(UUID.randomUUID().toString(),"Jess",UUID.randomUUID().toString(),"hello",time);
        Mockito
                .when(this.repository.save(Mockito.any(Msg.class)))
                .thenReturn(Mono.just(data));
        MediaType jsonUtf8 = MediaType.APPLICATION_JSON;
        this
                .client
                .post()
                .uri("/api/messages")
                .contentType(jsonUtf8)
                .body(Mono.just(data), Msg.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(jsonUtf8);
    }

    @Test
    public void delete() {
        Msg data = new Msg(UUID.randomUUID().toString(),"Jess",UUID.randomUUID().toString(),"hello",time);
        Mockito
                .when(this.repository.findById(data.getId()))
                .thenReturn(Mono.just(data));
        Mockito
                .when(this.repository.deleteById(data.getId()))
                .thenReturn(Mono.empty());
        this
                .client
                .delete()
                .uri("/api/messages/" + data.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void getById() {

        Msg data = new Msg(UUID.randomUUID().toString(),"Jess",UUID.randomUUID().toString(),"hello",time);

        Mockito
                .when(this.repository.findById(data.getId()))
                .thenReturn(Mono.just(data));

        this.client
                .get()
                .uri("/api/messages/" + data.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(data.getId())
                .jsonPath("$.sender").isEqualTo(data.getSender())
                .jsonPath("$.senderId").isEqualTo(data.getSenderId())
                .jsonPath("$.msg").isEqualTo(data.getMsg())
                .jsonPath("$.timestamp").isEqualTo(data.getTimestamp());
    }
}
