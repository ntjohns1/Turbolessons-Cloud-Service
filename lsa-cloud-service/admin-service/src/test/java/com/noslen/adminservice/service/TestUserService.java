package com.noslen.adminservice.service;
import com.okta.sdk.resource.user.UserBuilder;
import org.openapitools.client.api.UserApi;


public class TestUserService extends UserService {

    public TestUserService(UserApi userApi) {
        super(userApi);
    }
    @Override
    public UserBuilder userBuilder() {
        return super.userBuilder();
    }
}

