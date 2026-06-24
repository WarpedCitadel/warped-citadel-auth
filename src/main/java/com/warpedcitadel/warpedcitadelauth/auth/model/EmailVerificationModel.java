package com.warpedcitadel.warpedcitadelauth.auth.model;

public class EmailVerificationModel {

    private long appUserId;
    private String email;
    private String token;
    private String passcode;
    private boolean isUsed;


    public EmailVerificationModel() {

    }

    public EmailVerificationModel(long appUserId, String token, String passcode, boolean isUsed) {
        this.appUserId = appUserId;
        this.token = token;
        this.passcode = passcode;
        this.isUsed = isUsed;
    }

    public EmailVerificationModel(String email, String token, String passcode) {
        this.email = email;
        this.token = token;
        this.passcode = passcode;
    }


    public long getAppUserId() {
        return appUserId;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getPasscode() {
        return passcode;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
