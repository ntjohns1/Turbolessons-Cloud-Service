package com.noslen.adminservice.controller;

import com.noslen.adminservice.service.TestUserService;
import com.noslen.adminservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


//@Import(TestServiceConfig.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserApi userApi;

    @BeforeEach
    public void setUp() {
        TestUserService testUserService = new TestUserService(userApi);
        User u = testUserService.createUser("test@test.com", "Willie", "Nelson");
        System.out.println(u.getProfile());
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail("test@test.com");
        userProfile.setFirstName("Willie");
        userProfile.setLastName("Nelson");

        UserStub willie = new UserStub("fake-id", userProfile);

        when(userApi.createUser(any(), any())).thenReturn(willie);
        when(userApi.listUsers(null, null, 20, null, null, null, null))
                .thenReturn(Collections.singletonList(willie));
    }


    @Test
    void checkWillie() {
        System.out.println("hi");
    }

}
