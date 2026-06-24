package com.warpedcitadel.warpedcitadelauth.auth.model;

import java.util.Locale;

public class UserModel {

    private long appUserId;
    private String username;
    private String passwordHash;
    private String email;
    private String token;
    private String passcode;


    public UserModel() {

    }

    public UserModel(String username) {
        this.username = username;
    }

    public UserModel(String username, String email, String token, String passcode) {
        this.username = username;
        this.email = email;
        this.token = token;
        this.passcode = passcode;
    }

    public UserModel(String username, String passwordHash, String email, String token, String passcode) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.token = token;
        this.passcode = passcode;
    }

    public long getAppUserId() {
        return appUserId;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email = email.toLowerCase(Locale.ROOT);
    }

    public String getToken() {
        return token;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setAppUserId(long appUserId) {
        this.appUserId = appUserId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "appUserId=" + appUserId +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
