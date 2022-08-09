package com.noslen.eventservice.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/api/event")
public class EventController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!!!";
    }
}
