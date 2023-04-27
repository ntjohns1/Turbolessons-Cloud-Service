package com.noslen.adminservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noslen.adminservice.dto.UserDTO;
import com.noslen.adminservice.dto.UserProfileDTO;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.UserBuilder;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.UserApi;
import org.openapitools.client.model.UpdateUserRequest;
import org.openapitools.client.model.User;
import org.openapitools.client.model.UserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    @Value("${okta.client.token}")
    private String oktaClientToken;

    private final ApiClient client = Clients.builder()
            .setOrgUrl(System.getenv("ORG_URL"))
            .setClientCredentials(new TokenClientCredentials(System.getenv("API_TOKEN")))
            .build();

    private final UserApi userApi = new UserApi(client);
    @GetMapping("/api/users/hello")
    public String hello() {
        System.out.println("PING");
        return "Hello World";
    }
    //    self
    @GetMapping("/api/users/self")
    public Map<String, Object> getUserDetails(JwtAuthenticationToken authentication) {
        System.out.println(authentication.getToken().getTokenValue());
        return authentication.getTokenAttributes();
    }

    //    Get One User
    @GetMapping("/api/users/{id}")
    public User getUser(@PathVariable String id) {
        return userApi.getUser(id);
    }

    @GetMapping("/api/users/profile/{id}")
    public UserProfileDTO getUserProfile(@PathVariable String id) {
        UserProfileDTO dto = new UserProfileDTO();
        UserProfile userProfile = userApi.getUser(id).getProfile();
        assert userProfile != null;
        dto.setLogin(userProfile.getLogin());
        dto.setDisplayName(userProfile.getDisplayName());
        dto.setFirstName(userProfile.getFirstName());
        dto.setMiddleName(userProfile.getMiddleName());
        dto.setLastName(userProfile.getLastName());
        dto.setEmail(userProfile.getEmail());
        dto.setMobilePhone(userProfile.getMobilePhone());
        dto.setPrimaryPhone(userProfile.getPrimaryPhone());
        dto.setStreetAddress(userProfile.getStreetAddress());
        dto.setCity(userProfile.getCity());
        dto.setState(userProfile.getState());
        dto.setZipCode(userProfile.getZipCode());
        dto.setUserType(userProfile.getUserType());
        return dto;
    }

    //    Get All Users
    @GetMapping("/api/users")
    public List<User> listAllUsers() {
        System.out.println(oktaClientToken);
        //        users.removeIf(u -> !Objects.equals(Objects.requireNonNull(u.getProfile()).getUserType(), "student"));
        return userApi.listUsers(null, null, 150, null, null, null, null);
    }

    //    Create User
    @PostMapping("/api/users")
    public User createUser(@RequestBody UserDTO userDTO) {
        User user = UserBuilder.instance().
                setEmail(userDTO.getEmail())
                .setFirstName(userDTO.getFirstName())
                .setLastName(userDTO.getLastName())
                .setGroups(List.of("00g75cbo2dVoDm1wv5d7"))
                .buildAndCreate(userApi);
        String lastInitial = userDTO.getLastName().charAt(0) + ".";
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        UserProfile profile = new UserProfile();
        // Apply new profile object to request object
        profile.setDisplayName(userDTO.getFirstName() + " " + lastInitial);
        profile.setUserType("student");
        updateUserRequest.setProfile(profile);
        // then update
        userApi.updateUser(user.getId(), updateUserRequest, true);
        return user;
    }

    @PutMapping("/api/users/{id}")
    public void updateUser(@PathVariable String id, @RequestBody UserProfileDTO dto) {

        // Convert UserProfileDTO to Map, TypeReference makes a checked assignment
        // to correctly typed map instead of a raw Map.
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.convertValue(dto, new TypeReference<>() {});
        // Create request object and UserProfile object
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        UserProfile userProfile = new UserProfile();
        // Apply properties from mapped dto to new profile object
        userProfile.setAdditionalProperties(map);
        // Apply new profile object to request object
        updateUserRequest.setProfile(userProfile);
        // then update
        userApi.updateUser(id, updateUserRequest, true);
    }


    @DeleteMapping("/api/users/{id}")
    public void deleteUser(@PathVariable String id) {

        // deactivate first
        userApi.deactivateUser(id, false);
        // then delete
        userApi.deleteUser(id, false);
    }

}
