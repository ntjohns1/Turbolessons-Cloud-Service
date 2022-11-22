package com.noslen.adminservice.controller;

import com.noslen.adminservice.dto.UserDTO;
import com.noslen.adminservice.dto.UserProfileDTO;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.UserBuilder;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.UserApi;
import org.openapitools.client.model.User;
import org.openapitools.client.model.UserProfile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final ApiClient client = Clients.builder().build();
    private final UserApi userApi = new UserApi(client);

    //    Get One User
    @GetMapping("/api/users/{id}")
    public User getUser(@PathVariable String id) {
        System.out.println(userApi.getUser(id).getProfile());
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
        System.out.println(userApi.listUsers(null, null, 150, null, null, null, null));
        return userApi.listUsers(null, null, 150, null, null, null, null);
    }

    //    Create User
    @PostMapping("/api/users")
    public User createUser(@RequestBody UserDTO userDTO) {

        User user = UserBuilder.instance().setEmail(userDTO.getEmail()).setFirstName(userDTO.getFirstName()).setLastName(userDTO.getLastName()).setGroups(List.of("00g75cbo2dVoDm1wv5d7")).buildAndCreate(userApi);
        UserProfile profile = user.getProfile();
        String lastInitial = userDTO.getLastName().charAt(0) + ".";
        assert profile != null;
        profile.setDisplayName(userDTO.getFirstName() + " " + lastInitial);
        return user;
    }

    //    TODO: Update Client Scope to okta.users.manage

////    TODO: Figure this one out; set up UserProfileDTO, ObjectMapper and use userProfile.setAdditionalProperties(map);
//    @PutMapping("/api/users/{id}")
//    public void updateUser(@PathVariable String id, @RequestBody UserProfileDTO dto) {
//
//        ObjectMapper mapper = new ObjectMapper();
//        Map map = mapper.convertValue(dto, Map.class);
//        System.out.println(map);
//        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
//        UserProfile userProfile = userApi.getUser(id).getProfile();
//        assert userProfile != null;
//        userProfile.setAdditionalProperties(map);
//        updateUserRequest.setProfile(userProfile);
//
//        userApi.updateUser(id, updateUserRequest, true);
//    }


    @DeleteMapping("/api/users/{id}")
    public void deleteUser(@PathVariable String id) {

        // deactivate first
        userApi.deactivateUser(id, false);
        // then delete
        userApi.deleteUser(id, false);
    }


}
