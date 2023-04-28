package com.noslen.adminservice.controller;


import com.noslen.adminservice.dto.UserDTO;
import com.okta.sdk.resource.user.UserBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openapitools.client.api.UserApi;
import org.openapitools.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserApi userApi;

    // Write test cases here
    @Test
    public void testListAllUsers() throws Exception {

        List<User> userList = new ArrayList<>();
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Steve");
        userDTO.setLastName("Stevenson");
        userDTO.setEmail("steveysteve@example.com");
        User user = UserBuilder.instance().
                setEmail(userDTO.getEmail())
                .setFirstName(userDTO.getFirstName())
                .setLastName(userDTO.getLastName())
                .setGroups(List.of("00g75cbo2dVoDm1wv5d7"))
                .buildAndCreate(userApi);
        userList.add(user);

        when(userApi.listUsers(any(), any(), anyInt(), any(), any(), any(), any())).thenReturn(userList);

        this.mockMvc.perform(get("/api/users").with(jwt())).andExpect(status().isOk());

        // Verify that the userApi.listUsers method was called with the correct parameters
        verify(userApi).listUsers(null, null, 150, null, null, null, null);

    }
}
