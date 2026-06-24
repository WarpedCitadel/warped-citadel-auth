package com.warpedcitadel.warpedcitadelauth.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidEmailFormat implements ConstraintValidator<EmailFormat, String> {

    private static final String regex = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*[^@\\s]"
            + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,3})$";
    private static final Pattern pattern = Pattern.compile(regex);

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email.isEmpty() || email.isBlank()){
            return false;
        }
        String stripEmail = email.replaceAll("\\s", "");
        Matcher matcher = pattern.matcher(stripEmail);
        return matcher.matches();
    }
}