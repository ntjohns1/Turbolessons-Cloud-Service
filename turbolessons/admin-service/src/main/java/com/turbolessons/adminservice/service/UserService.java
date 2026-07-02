package com.turbolessons.adminservice.service;

import com.turbolessons.adminservice.client.GroupRepresentation;
import com.turbolessons.adminservice.client.KeycloakUserClient;
import com.turbolessons.adminservice.client.UserRepresentation;
import com.turbolessons.adminservice.config.KeycloakAdminProperties;
import com.turbolessons.adminservice.dto.UserProfileDTO;
import com.turbolessons.adminservice.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User management backed by the Keycloak Admin REST API (via {@link KeycloakUserClient}).
 *
 * <p>Replaces the Okta SDK implementation that was decommissioned. Custom profile fields
 * map to Keycloak user <em>attributes</em>; per-teacher cohorts map to Keycloak
 * <em>groups</em> ({@code active_student_<teacher>}); {@code createUser} adds the new user
 * to the default student group. The {@link com.turbolessons.adminservice.controller.UserController}
 * HTTP contract ({@code id} + {@code profile}) is preserved.
 */
@Service
public class UserService {

    private final KeycloakUserClient keycloak;
    private final KeycloakAdminProperties props;

    public UserService(KeycloakUserClient keycloak, KeycloakAdminProperties props) {
        this.keycloak = keycloak;
        this.props = props;
    }

    @Cacheable(value = "userCache", key = "'listAllUsers'")
    public List<User> listAllUsers() {
        return keycloak.listUsers().stream()
                .map(this::toUser)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "userCache", key = "'listAllUsersByTeacher:' + #teacherUsername")
    public List<User> listAllUsersByTeacher(String teacherUsername) {
        String groupName = props.getCohortGroupPrefix() + teacherUsername;
        Optional<GroupRepresentation> group = keycloak.findGroupByName(groupName);
        if (group.isEmpty()) {
            return Collections.emptyList();
        }
        return keycloak.listGroupMembers(group.get().getId()).stream()
                .map(this::toUser)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "userCache", key = "#id", unless = "#result == null")
    public User getUser(String id) {
        return keycloak.getUser(id).map(this::toUser).orElse(null);
    }

    public UserProfileDTO getUserProfile(String id) {
        return keycloak.getUser(id).map(this::toProfile).orElse(null);
    }

    @CacheEvict(value = "userCache", allEntries = true)
    public User createUser(String email, String firstName, String lastName, String teacherUsername) {
        UserRepresentation rep = new UserRepresentation();
        rep.setUsername(email);
        rep.setEmail(email);
        rep.setFirstName(firstName);
        rep.setLastName(lastName);
        rep.setEnabled(true);

        String firstInitial = (firstName != null && !firstName.isEmpty())
                ? String.valueOf(firstName.charAt(0)) : "";
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("displayName", Collections.singletonList(firstInitial + (lastName == null ? "" : lastName)));
        attributes.put("userType", Collections.singletonList("student"));
        rep.setAttributes(attributes);

        String userId = keycloak.createUser(rep);

        keycloak.findGroupByName(props.getDefaultStudentGroup())
                .ifPresent(group -> keycloak.addUserToGroup(userId, group.getId()));

        // Enroll in the creating teacher's cohort (active_student_<teacher>), so
        // getUsersByTeacher finds them. Create the cohort group if it's the
        // teacher's first student.
        if (teacherUsername != null && !teacherUsername.isBlank()) {
            String cohort = props.getCohortGroupPrefix() + teacherUsername;
            String groupId = keycloak.findGroupByName(cohort)
                    .map(GroupRepresentation::getId)
                    .orElseGet(() -> keycloak.createGroup(cohort));
            keycloak.addUserToGroup(userId, groupId);
        }

        return keycloak.getUser(userId).map(this::toUser).orElseGet(() -> {
            rep.setId(userId);
            return toUser(rep);
        });
    }

    @CacheEvict(value = "userCache", allEntries = true)
    public void updateUser(String userId, UserProfileDTO userProfileDTO) {
        // Merge onto the existing representation so unspecified fields/attributes survive.
        UserRepresentation rep = keycloak.getUser(userId).orElseGet(UserRepresentation::new);
        applyProfile(rep, userProfileDTO);
        keycloak.updateUser(userId, rep);
    }

    @CacheEvict(value = "userCache", allEntries = true)
    public void deleteUser(String id) {
        keycloak.deleteUser(id);
    }

    // --- mapping -----------------------------------------------------------

    private User toUser(UserRepresentation rep) {
        return new User(rep.getId(), toProfile(rep));
    }

    private UserProfileDTO toProfile(UserRepresentation rep) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setLogin(rep.getUsername());
        dto.setEmail(rep.getEmail());
        dto.setFirstName(rep.getFirstName());
        dto.setLastName(rep.getLastName());
        dto.setDisplayName(attr(rep, "displayName"));
        dto.setMiddleName(attr(rep, "middleName"));
        dto.setMobilePhone(attr(rep, "mobilePhone"));
        dto.setPrimaryPhone(attr(rep, "primaryPhone"));
        dto.setStreetAddress(attr(rep, "streetAddress"));
        dto.setCity(attr(rep, "city"));
        dto.setState(attr(rep, "state"));
        dto.setZipCode(attr(rep, "zipCode"));
        dto.setUserType(attr(rep, "userType"));
        return dto;
    }

    private void applyProfile(UserRepresentation rep, UserProfileDTO dto) {
        if (dto.getLogin() != null) rep.setUsername(dto.getLogin());
        if (dto.getEmail() != null) rep.setEmail(dto.getEmail());
        if (dto.getFirstName() != null) rep.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) rep.setLastName(dto.getLastName());

        Map<String, List<String>> attributes = rep.getAttributes() != null
                ? new HashMap<>(rep.getAttributes()) : new HashMap<>();
        putAttr(attributes, "displayName", dto.getDisplayName());
        putAttr(attributes, "middleName", dto.getMiddleName());
        putAttr(attributes, "mobilePhone", dto.getMobilePhone());
        putAttr(attributes, "primaryPhone", dto.getPrimaryPhone());
        putAttr(attributes, "streetAddress", dto.getStreetAddress());
        putAttr(attributes, "city", dto.getCity());
        putAttr(attributes, "state", dto.getState());
        putAttr(attributes, "zipCode", dto.getZipCode());
        putAttr(attributes, "userType", dto.getUserType());
        rep.setAttributes(attributes);
    }

    private static String attr(UserRepresentation rep, String key) {
        Map<String, List<String>> attributes = rep.getAttributes();
        if (attributes == null) {
            return null;
        }
        List<String> values = attributes.get(key);
        return (values == null || values.isEmpty()) ? null : values.get(0);
    }

    private static void putAttr(Map<String, List<String>> attributes, String key, String value) {
        if (value != null) {
            attributes.put(key, new ArrayList<>(Collections.singletonList(value)));
        }
    }
}
