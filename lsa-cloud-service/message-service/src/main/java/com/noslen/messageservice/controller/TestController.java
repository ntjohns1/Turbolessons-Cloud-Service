//package com.noslen.messageservice.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//@RestController
//public class TestController {
//
//    @Autowired
//    private JwtDecoder jwtDecoder;
//
//    @GetMapping("/api/messages/testJwtDecoder")
//    public Mono<String> testJwtDecoder() {
//        return Mono.just(jwtDecoder != null ? "JwtDecoder bean is available!" : "JwtDecoder bean is not available!");
//    }
//}
