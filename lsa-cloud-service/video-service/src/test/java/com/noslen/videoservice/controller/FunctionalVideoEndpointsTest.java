package com.noslen.videoservice.controller;

import com.noslen.videoservice.service.VideoStorageClient;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@Log4j2
@TestPropertySource(
        locations = "classpath:application-test.yml",
        properties = { "spring.config.name=application-test" }
)
@ActiveProfiles("test")
@Import({
        VideoEndpointConfig.class,
        VideoHandler.class,
        VideoStorageClient.class
})
public class FunctionalVideoEndpointsTest {

    @BeforeAll
    static void before() {
        log.info("running default " + VideoHandler.class.getName() + " tests");
    }




}