package com.noslen.lsaauthservice.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
public class CustomUser {

    private final int id;

    private final String email;
    private final String username;

    @JsonIgnore
    private final String password;

    @JsonCreator
    public CustomUser(int id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }


    public int getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return this.password;
    }


}

