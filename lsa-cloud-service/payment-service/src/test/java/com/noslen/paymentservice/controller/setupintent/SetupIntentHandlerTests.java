package com.noslen.paymentservice.controller.setupintent;


import com.noslen.paymentservice.service.setupintent.SetupIntentService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@WebFluxTest
@Import(SetupIntentHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class SetupIntentHandlerTests {

    @MockBean
    private SetupIntentService setupIntentService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SetupIntentHandler setupIntentHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new SetupIntentEndpointConfig(setupIntentHandler).routes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    void shouldListAllSetupIntents(ServerRequest r) {

    }

    //    Retrieve a SetupIntent
    @Test
    void shouldRetrieveSetupIntent(ServerRequest r) {

    }

    //    Create a SetupIntent
    @Test
    void shouldCreateSetupIntent(ServerRequest r) {

    }

    //    Confirm a SetupIntent
    @Test
    void shouldConfirmSetupIntent(ServerRequest r) {

    }

    //    Update a SetupIntent
    @Test
    void shouldUpdateSetupIntent(ServerRequest r) {

    }

    //    Cancel a SetupIntent
    @Test
    void shouldCancelSetupIntent(ServerRequest r) {

    }
}
