package com.warpedcitadel.warpedcitadelauth.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUsernameFormat implements ConstraintValidator<UsernameFormat, String> {

//    Alphanumeric, underscores, dots, or hyphens. 3–20 characters.
//    No symbols at the start/end. No double symbols
    private static final String regex = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,20}[a-zA-Z0-9]$";
    private static final Pattern pattern = Pattern.compile(regex);

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context){
        String stripUsername = username.strip();
        if (stripUsername.isBlank() || stripUsername.isEmpty()) {
            return false;
        }
        Matcher matcher = pattern.matcher(stripUsername);
        return matcher.matches();
    }
}
