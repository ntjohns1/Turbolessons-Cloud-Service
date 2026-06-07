package com.turbolessons.adminservice.model;

import com.turbolessons.adminservice.dto.UserProfileDTO;

/**
 * Local user representation.
 *
 * <p>Temporarily replaces the Okta SDK's {@code org.openapitools.client.model.User}
 * after the Okta org was decommissioned. The shape (id + profile) mirrors what the
 * controllers exposed so the HTTP contract stays roughly stable.
 *
 * <p>TODO(keycloak): back the admin user-management features with the Keycloak Admin
 * REST API ({@code keycloak-admin-client} {@code UserRepresentation}) — see Part 3 of
 * the migration plan.
 */
public class User {

    private String id;
    private UserProfileDTO profile;

    public User() {
    }

    public User(String id, UserProfileDTO profile) {
        this.id = id;
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(UserProfileDTO profile) {
        this.profile = profile;
    }
}
