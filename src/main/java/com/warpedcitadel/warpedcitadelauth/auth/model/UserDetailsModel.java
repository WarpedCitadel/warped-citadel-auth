package com.warpedcitadel.warpedcitadelauth.auth.model;

public class UserDetailsModel {

    private String userUUID;
    private String username;
    private String role;

    public UserDetailsModel(String userUUID, String username, String role) {
        this.userUUID = userUUID;
        this.username = username;
        this.role = role;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
