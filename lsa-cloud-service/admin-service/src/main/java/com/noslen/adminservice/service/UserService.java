package com.noslen.adminservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noslen.adminservice.dto.UserProfileDTO;
import com.okta.sdk.resource.user.UserBuilder;
import org.openapitools.client.api.UserApi;
import org.openapitools.client.model.UpdateUserRequest;
import org.openapitools.client.model.User;
import org.openapitools.client.model.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserApi userApi;

    public UserService(UserApi userApi) {
        this.userApi = userApi;
    }

    //    Get All Users
    public List<User> listAllUsers() {
        //        users.removeIf(u -> !Objects.equals(Objects.requireNonNull(u.getProfile()).getUserType(), "student"));
        return userApi.listUsers(null, null, 150, null, null, null, null);
    }

    public User getUser(String id) {
        return userApi.getUser(id);
    }

    public UserProfileDTO getUserProfile(String id) {
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

    public User createUser(String email, String firstName, String lastName) {
        User user = userBuilder().setEmail(email).setFirstName(firstName).setLastName(lastName).setGroups(List.of("00g75cbo2dVoDm1wv5d7")).buildAndCreate(userApi);
        String lastInitial = lastName.charAt(0) + ".";
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        UserProfile profile = new UserProfile();
        // Apply new profile object to request object
        profile.setDisplayName(firstName + " " + lastInitial);
        profile.setUserType("student");
        updateUserRequest.setProfile(profile);
        // then update
        userApi.updateUser(user.getId(), updateUserRequest, true);
        return user;
    }

    public void updateUser(String userId, UserProfileDTO userProfileDTO) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(userProfileDTO, new TypeReference<>() {
        });
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        UserProfile userProfile = new UserProfile();
        userProfile.setAdditionalProperties(map);
        updateUserRequest.setProfile(userProfile);
        userApi.updateUser(userId, updateUserRequest, true);
    }

    public void deleteUser(String id) {
        // deactivate first
        userApi.deactivateUser(id, false);
        // then delete
        userApi.deleteUser(id, false);
    }

    // This method wraps UserBuilder.instance()
    protected UserBuilder userBuilder() {
        return UserBuilder.instance();
    }
}

