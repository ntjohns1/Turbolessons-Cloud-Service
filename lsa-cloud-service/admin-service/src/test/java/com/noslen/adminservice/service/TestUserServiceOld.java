package com.noslen.adminservice.service;//package com.noslen.adminservice.service;
//
//import com.okta.sdk.resource.user.UserBuilder;
//import org.mockito.Mockito;
//import org.openapitools.client.api.UserApi;
//import org.openapitools.client.model.UpdateUserRequest;
//import org.openapitools.client.model.User;
//import org.openapitools.client.model.UserProfile;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//public class TestUserService extends UserService {
//
//    @MockBean
//    private UserApi userApi;
//    public TestUserService(UserApi userApi) {
//        super(userApi);
//    }
//    public User createTestUser() {
//        User user = userBuilder()
//                .setEmail("willie@nelson.com")
//                .setFirstName("willie")
//                .setLastName("nelson")
//                .setGroups(List.of("00g75cbo2dVoDm1wv5d7"))
//                .buildAndCreate(userApi);
////        String lastInitial = lastName.charAt(0) + ".";
////        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
////        UserProfile profile = new UserProfile();
////        // Apply new profile object to request object
////        profile.setDisplayName(firstName + " " + lastInitial);
////        profile.setUserType("student");
////        updateUserRequest.setProfile(profile);
////        // then update
////        userApi.updateUser(user.getId(), updateUserRequest, true);
//        return user;
//    }
////    @Override
////    public UserBuilder userBuilder() {
////        // Create a mock user
////        User mockUser = Mockito.mock(User.class);
////        UserProfile mockProfile = Mockito.mock(UserProfile.class);
////        when(mockProfile.getEmail()).thenReturn("willie@nelson.com");
////        when(mockProfile.getFirstName()).thenReturn("Nelson");
////        when(mockProfile.getLastName()).thenReturn("Willie");
////        when(mockUser.getProfile()).thenReturn(mockProfile);
////
////        // Create a mock UserBuilder
////        UserBuilder mockUserBuilder = Mockito.mock(UserBuilder.class);
////        when(mockUserBuilder.setEmail(any())).thenReturn(mockUserBuilder);
////        when(mockUserBuilder.setFirstName(any())).thenReturn(mockUserBuilder);
////        when(mockUserBuilder.setLastName(any())).thenReturn(mockUserBuilder);
////        when(mockUserBuilder.buildAndCreate(any())).thenReturn(mockUser);
////
////        return mockUserBuilder;
////    }
//@Override
//public UserBuilder userBuilder() {
////    // Create a real UserProfile object and set its properties
////    UserProfile userProfile = new UserProfile();
////    userProfile.setEmail("willie@nelson.com");
////    userProfile.setFirstName("Nelson");
////    userProfile.setLastName("Willie");
////
////    // Create a mock User object
////    User mockUser = Mockito.mock(User.class);
////    when(mockUser.getId()).thenReturn("test-id");
////    when(mockUser.getProfile()).thenReturn(userProfile);
////
////    // Create a mock UserBuilder
////    UserBuilder mockUserBuilder = Mockito.mock(UserBuilder.class);
////    when(mockUserBuilder.setEmail(any())).thenReturn(mockUserBuilder);
////    when(mockUserBuilder.setFirstName(any())).thenReturn(mockUserBuilder);
////    when(mockUserBuilder.setLastName(any())).thenReturn(mockUserBuilder);
////    when(mockUserBuilder.buildAndCreate(any())).thenReturn(mockUser);
////    return mockUserBuilder;
//    return UserBuilder.instance();
//}
//
//
//}
//
