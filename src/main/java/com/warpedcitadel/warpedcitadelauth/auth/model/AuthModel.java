package com.warpedcitadel.warpedcitadelauth.auth.model;

public class AuthModel {

    private final String uuid;
    private final String username;
    private final String passwordHash;
    private final String role;
    private final boolean isActive;
    private final boolean isVerified;


    public AuthModel(String uuid, String username, String passwordHash, String role, boolean isActive, boolean isVerified) {
        this.uuid = uuid;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isActive = isActive;
        this.isVerified = isVerified;
    }


    public String getUuid(){
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole(){
        return role;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isVerified() {
        return isVerified;
    }
}
