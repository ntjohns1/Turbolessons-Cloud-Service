package com.noslen.emailservice.dao;

import com.noslen.emailservice.dto.MailObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataMongoTest
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-test.yml",
        properties = { "spring.config.name=application-test" }
)
public class EmailObjectRepoTest {

    private final EmailObjectRepo repository;

    @Autowired
    public EmailObjectRepoTest(EmailObjectRepo repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @Test
    void shouldGetAllEmailData() {
        String testText = "Greetings, This is a test from TURBOLESSONS Love, TURBOLESSONS Admin";
        MailObject email1 = new MailObject(UUID.randomUUID().toString(),"dorflundgren@email.com","Dorf Lundgren","Lesson Reminder",testText,"TURBOLESSONS");
        MailObject email2 = new MailObject(UUID.randomUUID().toString(),"bhardbarrel@email.com","Biff Hardbarrel","Lesson Reminder",testText,"TURBOLESSONS");
        MailObject email3 = new MailObject(UUID.randomUUID().toString(),"braffhanesley@email.com","Braff Hanesley","Lesson Reminder",testText,"TURBOLESSONS");

        Flux<MailObject> mailObjectFlux = Flux.just(email1,email2,email3);

        Flux<MailObject> savedEmails = repository.saveAll(mailObjectFlux).thenMany(repository.findAll());

        StepVerifier.create(savedEmails)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void shouldAddGetDeleteEmailObject() {

        String testText = "Greetings, This is a test from TURBOLESSONS Love, TURBOLESSONS Admin";
        MailObject email = new MailObject(UUID.randomUUID().toString(),"dorflundgren@email.com","Dorf Lundgren","Lesson Reminder",testText,"TURBOLESSONS");

        Mono<MailObject> savedEmailMono = repository.save(email);
        Mono<MailObject> retrievedEmailMono = savedEmailMono.flatMap(savedEmail-> repository.findById(savedEmail.getId()));
        Mono<Void> deletedEmailMono = retrievedEmailMono.flatMap(retrievedEmail -> repository.deleteById(retrievedEmail.getId()));

        StepVerifier.create(savedEmailMono)
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(retrievedEmailMono)
                .expectNextMatches(retrievedEmail -> retrievedEmail.getId().equals(email.getId()))
                .verifyComplete();

        StepVerifier.create(deletedEmailMono)
                .verifyComplete();

        Mono<MailObject> notFoundEmailMono = repository.findById(email.getId());

        StepVerifier.create(notFoundEmailMono)
                .expectNextCount(0)
                .verifyComplete();

//        email = repository.save(email);
//
//        Optional<MailObject> email1 = repository.findById(email.getId());
//
//        Assertions.assertEquals(email1.get(),email);
//
//        repository.deleteById(email.getId());
//
//        email1 = repository.findById(email.getId());
//
//        Assertions.assertFalse(email1.isPresent());
    }


}
