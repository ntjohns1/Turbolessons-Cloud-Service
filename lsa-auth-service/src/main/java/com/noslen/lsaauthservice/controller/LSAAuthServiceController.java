package com.noslen.lsaauthservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noslen.lsaauthservice.util.feign.AdminClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class LSAAuthServiceController {

    @Autowired
    private final AdminClient adminClient;

    LSAAuthServiceController(AdminClient adminClient) {this.adminClient = adminClient; }

    //GET USER BY USERNAME
    @GetMapping("/{username}")
    public Object getUserByUsername(@PathVariable String username) throws JsonProcessingException {
        Object obj = adminClient.getUserByUsername(username);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(obj);
        System.out.println(s);
        JSONObject jsonObject = new JSONObject(s);
        System.out.println(jsonObject.get("password"));
        return obj;
    }

}
