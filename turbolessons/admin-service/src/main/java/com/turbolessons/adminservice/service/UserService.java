package com.turbolessons.adminservice.service;

import com.turbolessons.adminservice.dto.UserProfileDTO;
import com.turbolessons.adminservice.model.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * User management.
 *
 * <p>The previous implementation called the Okta Management API via the Okta SDK
 * ({@code UserApi}/{@code GroupApi}). That org was decommissioned, so these methods are
 * temporary stubs that keep admin-service deployable: reads return empty/null and writes
 * are rejected with {@link UnsupportedOperationException}.
 *
 * <p>TODO(keycloak): reimplement against the Keycloak Admin REST API
 * ({@code keycloak-admin-client}) — list/get/create/update/delete users and the
 * {@code active_student_*} group lookups, mapping {@link UserProfileDTO} to/from
 * Keycloak {@code UserRepresentation} attributes. See Part 3 of the migration plan.
 */
@Service
public class UserService {

    private static final String NOT_IMPLEMENTED =
            "admin-service user management is temporarily unavailable: pending the Keycloak Admin API migration";

    public List<User> listAllUsers() {
        return Collections.emptyList();
    }

    public List<User> listAllUsersByTeacher(String teacherUsername) {
        return Collections.emptyList();
    }

    public User getUser(String id) {
        return null;
    }

    public UserProfileDTO getUserProfile(String id) {
        return null;
    }

    public User createUser(String email, String firstName, String lastName) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    public void updateUser(String userId, UserProfileDTO userProfileDTO) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    public void deleteUser(String id) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }
}
