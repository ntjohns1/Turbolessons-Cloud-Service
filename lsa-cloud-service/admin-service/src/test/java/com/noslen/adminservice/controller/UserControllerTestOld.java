//package com.noslen.adminservice.controller;
//
//import com.noslen.adminservice.config.TestServiceConfig;
//import com.noslen.adminservice.service.UserService;
//import com.okta.sdk.resource.user.UserBuilder;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.openapitools.client.api.UserApi;
//import org.openapitools.client.model.User;
//import org.openapitools.client.model.UserProfile;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.hamcrest.Matchers.not;
//import static org.hamcrest.Matchers.emptyOrNullString;
//
//
////@Import(TestServiceConfig.class)
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(UserController.class)
//@ActiveProfiles("test")
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private UserService userService;
//    @MockBean
//    private UserApi userApi;
//
////    @Test
////    public void testListAllUsers() throws Exception {
////        List<User> userList = new ArrayList<>();
////        String firstName = "Nelson";
////        String lastName = "Willie";
////        String email = "willie@nelson.com";
////
////        User mockUser = createMockUser(firstName, lastName, email);
////        userList.add(mockUser);
////        System.out.println(mockUser.getId());
////
////        when(userApi.listUsers(any(), any(), anyInt(), any(), any(), any(), any())).thenReturn(userList);
////
////        this.mockMvc.perform(get("/api/users").with(jwt())).andExpect(status().isOk());
////
////        // Verify that the userApi.listUsers method was called with the correct parameters
////        verify(userApi).listUsers(null, null, 150, null, null, null, null);
////    }
//
////    @Test
////    public void testGetUserProfile() throws Exception {
//////        String userId = "testUserId";
//////        UserDTO userDTO = new UserDTO();
//////        userDTO.setFirstName("John");
//////        userDTO.setLastName("Doe");
//////        userDTO.setEmail("john.doe@example.com");
//////
//////        User user = UserBuilder.instance()
//////                .setEmail(userDTO.getEmail())
//////                .setFirstName(userDTO.getFirstName())
//////                .setLastName(userDTO.getLastName())
//////                .buildAndCreate(userApi);
//////
//////        UserProfileDTO profileDTO = new UserProfileDTO();
//////        UserProfile userProfile = userApi.getUser(user.getId()).getProfile();
//////        when(userApi.getUser(userId)).thenReturn(user);
//////
//////        this.mockMvc.perform(get("/api/users/profile/{id}", userId).with(jwt()))
//////                .andExpect(status().isOk())
//////                .andExpect(jsonPath("$.login", is(profileDTO.getLogin())))
//////                .andExpect(jsonPath("$.firstName", is(profileDTO.getFirstName())))
//////                .andExpect(jsonPath("$.lastName", is(profileDTO.getLastName())))
//////                .andExpect(jsonPath("$.email", is(profileDTO.getEmail())));
//////
//////        // Verify that the userApi.getUser method was called with the correct parameters
//////        verify(userApi).getUser(userId);
////      User u = userApi.getUser(createMockUser().getId());
////
////        System.out.println(u.getId());
////
////    }
//
//
//
//    @Test
//    public void testCreateUser() throws Exception {
//        User mockUser = new User();
//        mockUser.setProfile(new UserProfile()
//                .email("willie@nelson.com")
//                .firstName("Nelson")
//                .lastName("Willie"));
//
//        when(userService.createUser(anyString(), anyString(), anyString())).thenReturn(mockUser);
//        System.out.println(mockUser.getId());
//        System.out.println(mockUser.getProfile());
//        String s = "<{present=true}>";
//
//        mockMvc.perform(post("/api/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\":\"willie@nelson.com\",\"firstName\":\"Nelson\",\"lastName\":\"Willie\"}")
//                        .with(jwt()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.profile.email", is("willie@nelson.com")))
//                .andExpect(jsonPath("$.profile.firstName", not(emptyOrNullString())))
//                .andExpect(jsonPath("$.profile.lastName", not(emptyOrNullString())));
//
//        verify(userService, times(1)).createUser("willie@nelson.com", "Nelson", "Willie");
//    }
//
//    @Test
//    public void testGetUserById() throws Exception {
//        String userId = "test-id";
//
//        // Assuming you have the TestUserService configured with the mockUser as shown above
//        when(userService.getUser(userId)).thenReturn(mockUser);
//
//        mockMvc.perform(get("/api/users/{id}", userId).with(jwt()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is("test-id")))
//                .andExpect(jsonPath("$.profile.email", is("willie@nelson.com")))
//                .andExpect(jsonPath("$.profile.firstName", is("Nelson")))
//                .andExpect(jsonPath("$.profile.lastName", is("Willie")));
//
//        verify(userService, times(1)).getUser(userId);
//    }
//
//}
