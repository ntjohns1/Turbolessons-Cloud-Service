package com.noslen.messageservice.service;

import com.noslen.messageservice.model.Msg;
import com.noslen.messageservice.repository.MsgRepo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.function.Predicate;

@Log4j2
@DataMongoTest
@Import(MsgService.class)
public class MsgServiceTest {

    private final MsgService service;
    private final MsgRepo repository;

    public MsgServiceTest(@Autowired MsgService service, @Autowired MsgRepo repository) {
        this.service = service;
        this.repository = repository;
    }

    @Test
    public void getAll() {
        final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());
        Flux<Msg> saved = repository.saveAll(Flux.just(new Msg(null, "Josh", UUID.randomUUID().toString(), "Hello", time), new Msg(null, "Matt",UUID.randomUUID().toString(), "Hello", time), new Msg(null, "Jane", UUID.randomUUID().toString(), "Hello", time)));

        Flux<Msg> composite = service.all().thenMany(saved);

        Predicate<Msg> match = msg -> Boolean.TRUE.equals(saved.any(saveItem -> saveItem.equals(msg)).block());

        StepVerifier.create(composite).expectNextMatches(match).expectNextMatches(match).expectNextMatches(match).verifyComplete();
    }

    @Test
    public void save() {
        Mono<Msg> msgMono = this.service.create("Buddy",UUID.randomUUID().toString(), "Hello");
        StepVerifier.create(msgMono).expectNextMatches(saved -> StringUtils.hasText(saved.getId())).verifyComplete();
    }

    @Test
    public void delete() {
        String test = "test";
        Mono<Msg> deleted = this.service.create(test, test, test).flatMap(saved -> this.service.delete(saved.getId()));
        StepVerifier.create(deleted).expectNextMatches(msg -> msg.getSender().equalsIgnoreCase(test)).verifyComplete();
    }

    // TODO: Fix this one - only works if id is "1"
    @Test
    public void getById() {
        final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());
        repository.save(new Msg("1", "Jane", UUID.randomUUID().toString(),"Hello", time));
        Mono<Msg> composite = (Mono<Msg>) this.service.get("1");
        StepVerifier.create(composite).expectNextMatches(msg -> StringUtils.hasText(msg.getId()) && "1".equalsIgnoreCase(msg.getId())).verifyComplete();
//        String test = "Jim";
//        String test1 = "hello";
//        Mono<Msg> deleted = this.service
//                .create(test, test1)
//                .flatMap(saved -> this.service.get(saved.getId()));
//        StepVerifier
//                .create(deleted)
//                .expectNextMatches(msg -> StringUtils.hasText(msg.getId()) && test.equalsIgnoreCase(msg.getId()))
//                .verifyComplete();
//        final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());
//        Mono<Msg> saved = this.service.create("Jim", "Hello");

    }
}
