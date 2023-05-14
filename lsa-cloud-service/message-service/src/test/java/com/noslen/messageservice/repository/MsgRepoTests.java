package com.noslen.messageservice.repository;

import com.noslen.messageservice.model.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.function.Predicate;

@DataMongoTest
@ActiveProfiles("test")
public class MsgRepoTests {

    private final MsgRepo msgRepo;

    @Autowired
    public MsgRepoTests(MsgRepo msgRepo) {
        this.msgRepo = msgRepo;
    }

    @BeforeEach
    public void setUp() {
        msgRepo.deleteAll().block();
    }

    @Test
    public void testFindAll() {
        final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());

        String testSender = "bholiday";
        String testRecipient = "tmiller";
        String alternateUser = "dhagel";
        Msg msg1 = new Msg(null, testSender, testRecipient, "test", time);
        Msg msg2 = new Msg(null, testSender, testRecipient, "test", time);
        Msg msg3 = new Msg(null, testSender, testRecipient, "test", time);
        Msg msg4 = new Msg(null, testSender, alternateUser, "test", time);
        Msg msg5 = new Msg(null, alternateUser, testRecipient, "test", time);
        Flux<Msg> msgFlux = Flux.just(msg1, msg2, msg3, msg4, msg5);

        Flux<Msg> savedMsgs = msgRepo.saveAll(msgFlux).thenMany(msgRepo.findAll());

        StepVerifier.create(savedMsgs)
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void testSaveFindByIdDelete() {
        final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());

        Msg msg = new Msg(UUID.randomUUID().toString(), "bholiday", "tmiller", "test", time);

        Mono<Msg> savedMsgMono = msgRepo.save(msg);
        Mono<Msg> retrievedMsgMono = savedMsgMono.flatMap(savedMsg -> msgRepo.findById(savedMsg.getId()));
        Mono<Void> deletedMsgMono = retrievedMsgMono.flatMap(retrievedMsg -> msgRepo.deleteById(retrievedMsg.getId()));

        StepVerifier.create(savedMsgMono)
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(retrievedMsgMono)
                .expectNextMatches(retrievedMsg -> retrievedMsg.getId().equals(msg.getId()))
                .verifyComplete();

        StepVerifier.create(deletedMsgMono)
                .verifyComplete();

        Mono<Msg> notFoundMsgMono = msgRepo.findById(msg.getId());

        StepVerifier.create(notFoundMsgMono)
                .expectNextCount(0)
                .verifyComplete();
    }
    @Test
    public void testFindBySender() {
        final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());

        Msg msg1 = new Msg(null, "jhilton", "mhines", "test", time);
        Msg msg2 = new Msg(null, "jhilton", "mhines", "test", time);
        Msg msg3 = new Msg(null, "jhilton", "djacobs", "test", time);
        Flux<Msg> msgFlux = Flux.just(msg1, msg2, msg3);

        Flux<Msg> composite = msgRepo.saveAll(msgFlux).thenMany(msgRepo.findBySender("jhilton"));

        Predicate<Msg> match = msg -> msg.getSender().equals("jhilton");

        StepVerifier.create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();
    }
    @Test
    public void testFindByRecipient() {
        final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());

        Msg msg1 = new Msg(null, "jhilton", "djacobs", "test", time);
        Msg msg2 = new Msg(null, "rmorris", "djacobs", "test", time);
        Msg msg3 = new Msg(null, "mharris", "djacobs", "test", time);
        Flux<Msg> msgFlux = Flux.just(msg1, msg2, msg3);

        Flux<Msg> composite = msgRepo.saveAll(msgFlux).thenMany(msgRepo.findByRecipient("djacobs"));

        Predicate<Msg> match = msg -> msg.getRecipient().equals("djacobs");

        StepVerifier.create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();
    }
    @Test
    public void testFindBySenderAndRecipient() {
        final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());

        Msg msg1 = new Msg(null, "jhilton", "djacobs", "test", time);
        Msg msg2 = new Msg(null, "jhilton", "djacobs", "test", time);
        Msg msg3 = new Msg(null, "jhilton", "djacobs", "test", time);
        Flux<Msg> msgFlux = Flux.just(msg1, msg2, msg3);

        Flux<Msg> composite = msgRepo.saveAll(msgFlux)
                .thenMany(msgRepo.findBySenderAndRecipient("jhilton","djacobs"));

        Predicate<Msg> match = msg -> msg.getSender().equals("jhilton") && msg.getRecipient().equals("djacobs");

        StepVerifier.create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();
    }
}
