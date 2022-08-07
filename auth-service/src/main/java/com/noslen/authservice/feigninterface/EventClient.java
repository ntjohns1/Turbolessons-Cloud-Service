package com.noslen.authservice.feigninterface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "event-service")
public interface EventClient {
    @GetMapping("api/event/hello")
    public String hello();
}
