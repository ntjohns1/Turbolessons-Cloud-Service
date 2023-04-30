package com.noslen.adminservice.util;

import org.openapitools.client.model.User;
import org.openapitools.client.model.UserProfile;

public class UserStub extends User {
    private String id;
    private UserProfile profile;

    public UserStub(String id, UserProfile profile) {
        this.id = id;
        this.profile = profile;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public UserProfile getProfile() {
        return profile;
    }
}
