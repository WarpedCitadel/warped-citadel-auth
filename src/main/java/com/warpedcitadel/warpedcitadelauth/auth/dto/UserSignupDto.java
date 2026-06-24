package com.warpedcitadel.warpedcitadelauth.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.warpedcitadel.warpedcitadelauth.auth.validation.EmailFormat;
import com.warpedcitadel.warpedcitadelauth.auth.validation.PasswordFormat;
import com.warpedcitadel.warpedcitadelauth.auth.validation.UsernameFormat;

public record UserSignupDto(

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @UsernameFormat(message = "Invalid username")
        String username,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @PasswordFormat(message = "Invalid password")
        String password,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @EmailFormat(message = "Invalid email")
        String email
) {}
