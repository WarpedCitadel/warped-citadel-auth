package com.warpedcitadel.warpedcitadelauth.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidPasswordFormat implements ConstraintValidator<PasswordFormat, String> {

//    Min. 8 chars, Max. 26 chars, 1 Upper, 1 Lower, 1 Number
    private static final String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,26}$";
    private static final Pattern pattern = Pattern.compile(regex);

    @Override
    public boolean isValid(String passwordHash, ConstraintValidatorContext context){
        String stripPasswordHash = passwordHash.strip();
        if (stripPasswordHash.isBlank() || stripPasswordHash.isEmpty()) {
            return false;
        }
        Matcher matcher = pattern.matcher(stripPasswordHash);
        return matcher.matches();
    }
}
