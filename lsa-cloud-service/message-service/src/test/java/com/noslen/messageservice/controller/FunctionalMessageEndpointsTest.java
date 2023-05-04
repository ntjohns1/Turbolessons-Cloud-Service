package com.noslen.messageservice.controller;

import com.noslen.messageservice.service.MsgService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@Log4j2
@ActiveProfiles("default")
@Import({MessageEndpointConfig.class,
        MessageHandler.class, MsgService.class})
public class FunctionalMessageEndpointsTest extends AbstractBaseMessageEndpoints {

    @BeforeAll
    static void before() {
        log.info("running default " + MessageHandler.class.getName() + " tests");
    }

    FunctionalMessageEndpointsTest(@Autowired WebTestClient client) {
        super(client);
    }
}