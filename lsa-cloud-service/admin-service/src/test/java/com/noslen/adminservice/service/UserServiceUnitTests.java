package com.noslen.adminservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.api.UserApi;
import org.openapitools.client.model.User;
import org.openapitools.client.model.UserProfile;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    @Mock
    private UserApi userApi;
    @InjectMocks
    private UserService userService;
    private User mockedUser;

    @BeforeEach
    public void setUp() {
        UserProfile profile = new UserProfile();
        profile.setEmail("willie@nelson.com");
        profile.setFirstName("Willie");
        profile.setLastName("Nelson");
        profile.setDisplayName("Willie N.");
        profile.setUserType("student");
        mockedUser = new User();
        mockedUser.setProfile(profile);
    }

    @Test
    public void testListAllUsers() {
        when(userApi.listUsers(null, null, 150, null, null, null, null))
                .thenReturn(List.of(mockedUser));
        List<User> users = userService.listAllUsers();
        assertEquals(1, users.size());
        assertEquals(mockedUser, users.get(0));
    }

    @Test
    public void testGetUser() {
        when(userApi.getUser("testId")).thenReturn(mockedUser);
        assertEquals(mockedUser, userService.getUser("testId"));
    }

    @Test
    public void testGetUserProfile() {
        when(userApi.getUser("testId")).thenReturn(mockedUser);
        String expected = Objects.requireNonNull(mockedUser.getProfile()).getFirstName();
        String actual = userService.getUserProfile("testId").getFirstName();
        assertEquals(expected, actual);
    }

    // createUser, updateUser, and deleteUser will be considered out of scope for unit testing
    // since they rely on direct interactions with the Okta server. See Integration tests
}

