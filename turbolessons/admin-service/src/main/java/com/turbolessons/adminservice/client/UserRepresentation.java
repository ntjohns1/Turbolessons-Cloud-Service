package com.turbolessons.adminservice.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * Minimal Jackson mapping of Keycloak's Admin API {@code UserRepresentation}.
 *
 * <p>Only the fields admin-service reads or writes are modeled; unknown fields are
 * ignored so the POJO stays resilient to Keycloak version differences. Custom
 * profile fields (middle name, phones, address, user type, …) are carried in
 * {@link #attributes} as {@code Map<String, List<String>>}, matching Keycloak's
 * multivalued attribute shape.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRepresentation {

    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;
    private Boolean emailVerified;
    private Map<String, List<String>> attributes;

    public UserRepresentation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }
}
