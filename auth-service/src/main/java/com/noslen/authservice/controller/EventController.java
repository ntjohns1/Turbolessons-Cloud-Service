package com.noslen.authservice.controller;

import com.noslen.authservice.feigninterface.EventClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class EventController {

    @Autowired
    private final EventClient client;


    public EventController(EventClient client) {
        this.client = client;
    }

    @GetMapping("api/event/hello")
    public String hello() {
        return client.hello();
    }
}
