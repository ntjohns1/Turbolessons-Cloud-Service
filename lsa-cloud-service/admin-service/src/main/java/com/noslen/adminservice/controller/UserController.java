package com.noslen.adminservice.controller;

import com.okta.sdk.client.Clients;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.UserApi;
import org.openapitools.client.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final ApiClient client = Clients.builder().build();

    @GetMapping("/api/users")
    private List<User> listAllUsers() {
        UserApi userApi = new UserApi(client);
        return userApi.listUsers(null, null, 5, null, null, null, null);
    }


}
